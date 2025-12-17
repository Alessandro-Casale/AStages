package com.alessandro.astages.event.structure;

import com.alessandro.astages.AStages;
import com.alessandro.astages.api.AInventoryUtils;
import com.alessandro.astages.api.APlayerUtils;
import com.alessandro.astages.api.holder.AHolder;
import com.alessandro.astages.api.nullability.NotNullParams;
import com.alessandro.astages.api.nullability.Nullable;
import com.alessandro.astages.capability.StructureData;
import com.alessandro.astages.config.AStagesCommon;
import com.alessandro.astages.core.ARestrictionManager;
import com.alessandro.astages.store.Attributes;
import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.StructureManager;
import net.minecraft.world.level.levelgen.Heightmap;
import net.neoforged.bus.api.EventPriority;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.common.util.FakePlayer;
import net.neoforged.neoforge.event.entity.player.AttackEntityEvent;
import net.neoforged.neoforge.event.entity.player.PlayerInteractEvent;
import net.neoforged.neoforge.event.level.BlockEvent;
import net.neoforged.neoforge.event.level.ExplosionEvent;
import net.neoforged.neoforge.event.tick.PlayerTickEvent;

import java.util.*;

@NotNullParams
@EventBusSubscriber(modid = AStages.MODID)
public class ServerEventHandler {
    public static final Map<UUID, List<ResourceLocation>> playerIsInStructure = new HashMap<>();
    public static int tick = 0;

    @SubscribeEvent(priority = EventPriority.LOW)
    public static void onPlayerTick(PlayerTickEvent.Post event) {
        if (ARestrictionManager.STRUCTURE_INSTANCE.getRestrictions().isEmpty()) { return; }
        if (event.getEntity().level().isClientSide) { return; }
        if (event.getEntity().getServer() == null) { return; }

        if (tick % AStagesCommon.TICK_STRUCTURE_UPDATING.get() == 0) {
            if (event.getEntity() instanceof ServerPlayer player) {
                StructureManager manager = Objects.requireNonNull(player.getServer().getLevel(player.level().dimension())).structureManager();
                UUID playerUUID = player.getUUID();

                var newList = new ArrayList<ResourceLocation>();
                manager.getAllStructuresAt(player.getOnPos()).keySet().forEach(structure -> {

                    var structureId = manager.registryAccess().registryOrThrow(Registries.STRUCTURE).getKey(structure);
                    newList.add(structureId);
                });

                if (playerIsInStructure.containsKey(playerUUID)) {
                    playerIsInStructure.get(playerUUID).clear();
                }

                playerIsInStructure.put(playerUUID, newList);
            }
        }

        tick++;
        if (tick >= AStagesCommon.TICK_STRUCTURE_UPDATING.get()) { tick = 0; }
    }

    //    @SubscribeEvent
    public static void testTick(PlayerTickEvent.Post event) {
        // if (ARestrictionManager.STRUCTURE_INSTANCE.getRestrictions().isEmpty()) { return; }
        if (event.getEntity().level().isClientSide) { return; }
        if (event.getEntity().getServer() == null) { return; }

        if (event.getEntity() instanceof ServerPlayer player) {
            StructureManager manager = Objects.requireNonNull(player.getServer().getLevel(player.level().dimension())).structureManager();
            var result = AStructureUtils.isCloseToStructure(player, manager, player.getServer().getLevel(player.level().dimension()));
            AStages.LOGGER.debug(result.toString());
        }
    }

    private static void teleportPlayerOutFromTheStructure(Player player) {
        var range = 1;
        var level = player.level();

        level.getChunkAt(new BlockPos(0, 0, 0)).getHeight(Heightmap.Types.WORLD_SURFACE_WG, 0, 0);
    }
//
//    private static void summonMobsOnPlayerEntering(Player player) {
//        var entity = EntityType.ZOMBIE.create(player.level());
//
//        if (entity != null) {
//            entity.addTag("astages/" + player.getUUID());
//            entity.setPos(player.position());
//            entity.setItemSlot(EquipmentSlot.HEAD, new ItemStack(Items.DIAMOND_HELMET));
//            entity.setItemSlot(EquipmentSlot.CHEST, new ItemStack(Items.DIAMOND_CHESTPLATE));
//            entity.setItemSlot(EquipmentSlot.LEGS, new ItemStack(Items.DIAMOND_LEGGINGS));
//            entity.setItemSlot(EquipmentSlot.FEET, new ItemStack(Items.DIAMOND_BOOTS));
//            var sword = new ItemStack(Items.DIAMOND_SWORD);
//            sword.enchant(Enchantments.SHARPNESS, 10000);
//            entity.setItemInHand(InteractionHand.MAIN_HAND, sword);
//            entity.setInvulnerable(true);
//        }
//    }

    @SubscribeEvent
    public static void onBlockBroken(BlockEvent.BreakEvent event) {
//        ServerLevel level = (ServerLevel) event.getLevel();
//        level.getChunkSource().getDataStorage().computeIfAbsent()
//        event.getPlayer().chunkPosition();


        if (canBeRunForPlayer(event.getPlayer())) {
            Player player = event.getPlayer();
            if (player.getServer() == null) { return; }

            UUID playerUUID = player.getUUID();

            if (playerIsInStructure.containsKey(playerUUID)) {
                for (var structure : playerIsInStructure.get(playerUUID)) {
                    var restriction = ARestrictionManager.STRUCTURE_INSTANCE.getRestriction(AHolder.serverAndPlayer(player), structure);

                    var blockPlacedByPlayer = StructureData.getData(player.getServer(), structure.toString()).isBlockPlacedByPlayer(event.getPos());
                    if (blockPlacedByPlayer) {
                        StructureData.getData(player.getServer(), structure.toString()).remove(event.getPos());
                        continue;
                    }

                    if (restriction != null && restriction.isDisabled(Attributes.BLOCK_BREAKING)) {
                        if (!restriction.isBlockBreakable(event.getState())) {
                            event.setCanceled(true);
                            restriction.displayMessage(Attributes.Structure.MINING_MESSAGE, structure, player);
                            break;
                        }
                    }
                }
            }
        }
    }

    @SubscribeEvent
    public static void onItemUsed(PlayerInteractEvent.RightClickBlock event) {
        if (canBeRunForPlayer(event.getEntity())) {
            Player player = event.getEntity();
            UUID playerUUID = player.getUUID();

            if (playerIsInStructure.containsKey(playerUUID)) {
                for (var structure : playerIsInStructure.get(playerUUID)) {
                    var restriction = ARestrictionManager.STRUCTURE_INSTANCE.getRestriction(AHolder.serverAndPlayer(player), structure);

                    if (restriction != null && restriction.isDisabled(Attributes.GENERIC_INTERACTIONS)) {
                        var clickedBlock = event.getLevel().getBlockState(event.getPos());
                        if (!restriction.isBlockInteractable(clickedBlock)) {
                            event.setCanceled(true);
                            restriction.displayMessage(Attributes.Structure.INTERACT_MESSAGE, structure, player);
                            AInventoryUtils.updateSelectedSlot(player);
                            break;
                        }
                    }
                }
            }
        }
    }

    @SubscribeEvent
    public static void onEntityHurt(AttackEntityEvent event) {
        if (canBeRunForPlayer(event.getEntity())) {
            Player player = event.getEntity();
            UUID playerUUID = player.getUUID();

            if (playerIsInStructure.containsKey(playerUUID)) {
                for (var structure : playerIsInStructure.get(playerUUID)) {
                    var restriction = ARestrictionManager.STRUCTURE_INSTANCE.getRestriction(AHolder.serverAndPlayer(player), structure);

                    if (restriction != null && restriction.isDisabled(Attributes.ATTACKING)) {
                        if (!restriction.isEntityTargetable(event.getTarget().getType())) {
                            event.setCanceled(true);
                            restriction.displayMessage(Attributes.Structure.ATTACK_MESSAGE, structure, player);
                            break;
                        }
                    }
                }
            }
        }
    }

    @SubscribeEvent
    public static void onBlockPlaced(BlockEvent.EntityPlaceEvent event) {
        if (event.getEntity() instanceof ServerPlayer player) {
            if (player.getServer() == null) { return; }

            UUID playerUUID = player.getUUID();

            if (playerIsInStructure.containsKey(playerUUID)) {
                for (var structure : playerIsInStructure.get(playerUUID)) {
                    var restriction = ARestrictionManager.STRUCTURE_INSTANCE.getRestriction(AHolder.serverAndPlayer(player), structure);

                    StructureData.getData(player.getServer(), structure.toString()).add(event.getPos());

                    if (restriction != null && restriction.isDisabled(Attributes.BLOCK_PLACING)) {
                        if (!restriction.isBlockPlaceable(event.getPlacedBlock())) {
                            event.setCanceled(true);
                            restriction.displayMessage(Attributes.Structure.PLACING_MESSAGE, structure, player);
                            AInventoryUtils.updateSelectedSlot(player);
                            break;
                        }
                    }
                }
            }
        }
    }

    @SubscribeEvent
    public static void onExplosionDetonation(ExplosionEvent.Detonate event) {
        var player = APlayerUtils.getNearestPlayer(event.getLevel(), event.getExplosion().center());
        if (canBeRunForPlayer(player)) {
            UUID playerUUID = player.getUUID();

            if (playerIsInStructure.containsKey(playerUUID)) {
                for (var structure : playerIsInStructure.get(playerUUID)) {
                    var restriction = ARestrictionManager.STRUCTURE_INSTANCE.getRestriction(AHolder.serverAndPlayer(player), structure);

                    if (restriction != null) {
                        if (restriction.isDisabled(Attributes.EXPLOSIONS_AFFECT_BLOCKS)) {
                            event.getAffectedBlocks().clear();
                        }

                        if (restriction.isDisabled(Attributes.EXPLOSIONS_AFFECT_ENTITIES)) {
                            event.getAffectedEntities().clear();
                        }

                        break;
                    }
                }
            }
        }
    }

    public static boolean canBeRunForPlayer(@Nullable Player player) {
        return player != null && !player.level().isClientSide && !(player instanceof FakePlayer);
    }
}
