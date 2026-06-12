package com.alessandro.astages.internal.experimental;

import com.alessandro.astages.AStages;
import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.StructureManager;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.neoforged.neoforge.event.tick.PlayerTickEvent;
import org.jetbrains.annotations.NotNull;

import java.util.*;

public class AStructureUtils {
    private static final int[][] delta = {
        { -1, -1 },
        { -1, 0 },
        { -1, +1 },
        { 0, -1 },
        // { 0, 0 },
        { 0, +1 },
        { +1, -1 },
        { +1, 0 },
        { +1, +1 },
    };

    public static boolean isInsideStructure(ServerPlayer player, @NotNull List<ResourceLocation> structures) {
        return !structures.isEmpty();
    }

    public static List<VoxelShape> isCloseToStructure(ServerPlayer player, StructureManager manager, ServerLevel level) {
        var toReturn = new ArrayList<VoxelShape>();

        for (var pos : delta) {
            var deltaX = pos[0];
            var deltaZ = pos[1];
            var newPos = player.getOnPos().mutable().move(deltaX, 1, deltaZ);

            manager.getAllStructuresAt(newPos).forEach((s, longs) -> {
                var structure = level.registryAccess().registryOrThrow(Registries.STRUCTURE).getKey(s);
                if (structure != null) {
//                    var restriction = ARestrictionManager.STRUCTURE_INSTANCE.getRestriction(structure, player, player.getServer());

//                    if (restriction != null) {
                        var finalS = manager.getStructureWithPieceAt(newPos, s);
                        if (finalS.isValid()) {
                            var shape = Shapes.create(AABB.of(finalS.getBoundingBox()));
                            toReturn.add(shape);
                        }
//                    }
                }
            });
        }

        return toReturn;
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

//    public static final Map<UUID, List<ResourceLocation>> playerIsInStructure = new HashMap<>();
//    public static int tick = 0;

//    @SubscribeEvent(priority = EventPriority.LOW)
//    public static void onPlayerTick(PlayerTickEvent.Post event) {
//        if (ARestrictionManager.STRUCTURE_INSTANCE.getRegistry().getRestrictions().isEmpty()) { return; }
//        if (event.getEntity().level().isClientSide) { return; }
//        if (event.getEntity().getServer() == null) { return; }
//
//        if (tick % AStagesCommon.TICK_STRUCTURE_UPDATING.get() == 0) {
//            if (event.getEntity() instanceof ServerPlayer player) {
//                StructureManager manager = Objects.requireNonNull(player.getServer().getLevel(player.level().dimension())).structureManager();
//                UUID playerUUID = player.getUUID();
//
//                var newList = new ArrayList<ResourceLocation>();
//                manager.getAllStructuresAt(player.getOnPos()).keySet().forEach(structure -> {
//
//                    var structureId = manager.registryAccess().registryOrThrow(Registries.STRUCTURE).getKey(structure);
//                    newList.add(structureId);
//                });
//
//                if (playerIsInStructure.containsKey(playerUUID)) {
//                    playerIsInStructure.get(playerUUID).clear();
//                }
//
//                playerIsInStructure.put(playerUUID, newList);
//            }
//        }
//
//        tick++;
//        if (tick >= AStagesCommon.TICK_STRUCTURE_UPDATING.get()) { tick = 0; }
//    }
}
