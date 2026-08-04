package com.alessandro.astages.infrastructure.hook.restriction;

import com.alessandro.astages.AStages;
import com.alessandro.astages.api.holder.AHolder;
import com.alessandro.astages.api.nullability.NotNullParams;
import com.alessandro.astages.api.util.AInventoryUtils;
import com.alessandro.astages.engine.ARestrictionManager;
import com.alessandro.astages.engine.collision.StructureCollisionManager;
import com.alessandro.astages.engine.store.Attributes;
import com.alessandro.astages.engine.util.EventGuards;
import com.alessandro.astages.infrastructure.capability.PlacedBlocksInStructureData;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.Registries;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.levelgen.structure.Structure;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.player.AttackEntityEvent;
import net.neoforged.neoforge.event.entity.player.PlayerInteractEvent;
import net.neoforged.neoforge.event.level.BlockEvent;
import net.neoforged.neoforge.event.level.ExplosionEvent;
import net.neoforged.neoforge.event.server.ServerStartedEvent;

@NotNullParams
@EventBusSubscriber(modid = AStages.MODID)
public class StructureServerEvents {
    private static Registry<Structure> REGISTRY_ACCESS = null;

    @SubscribeEvent
    public static void onServerStarted(ServerStartedEvent event) {
        REGISTRY_ACCESS = event.getServer().registryAccess().registry(Registries.STRUCTURE).orElse(null);
    }

    @SubscribeEvent
    public static void onBlockBroken(BlockEvent.BreakEvent event) {
        if (!EventGuards.isValidPlayer(event.getPlayer())) { return; }
        if (event.getPlayer().getServer() == null) { return; }

        var player = event.getPlayer();
        var level = player.level();
        var dimension = level.dimension();
        var pos = event.getPos();

        var structures = StructureCollisionManager.SERVER_INSTANCE.getStructuresForBlockPos(dimension, pos);

        for (var structure : structures) {
            var structureId = REGISTRY_ACCESS.getKey(structure.getStructure());
            if (structureId == null) { continue; }

            var restriction = ARestrictionManager.STRUCTURE_INSTANCE.getRestriction(AHolder.serverAndPlayer(player), structureId);

            var blockPlacedByPlayer = PlacedBlocksInStructureData.getData(player.getServer(), structureId.toString()).isBlockPlacedByPlayer(event.getPos());
            if (blockPlacedByPlayer) {
                PlacedBlocksInStructureData.getData(player.getServer(), structure.toString()).remove(event.getPos());
                continue;
            }

            if (restriction != null && restriction.isDisabled(Attributes.BLOCK_BREAKING)) {
                if (!restriction.isBlockBreakable(event.getState())) {
                    event.setCanceled(true);
                    restriction.displayMessage(Attributes.Structure.BREAKING_MESSAGE, structureId, player);
                    break;
                }
            }
        }
    }

    @SubscribeEvent
    public static void onItemUsed(PlayerInteractEvent.RightClickBlock event) {
        if (!EventGuards.isValidPlayer(event.getEntity())) { return; }

        var player = event.getEntity();
        var level = event.getLevel();
        var dimension = level.dimension();
        var pos = event.getPos();

        var structures = StructureCollisionManager.SERVER_INSTANCE.getStructuresForBlockPos(dimension, pos);
        for (var structure : structures) {
            var structureId = REGISTRY_ACCESS.getKey(structure.getStructure());
            if (structureId == null) { continue; }

            var restriction = ARestrictionManager.STRUCTURE_INSTANCE.getRestriction(AHolder.serverAndPlayer(player), structureId);
            if (restriction != null && restriction.isDisabled(Attributes.GENERIC_INTERACTIONS)) {
                var clickedBlock = event.getLevel().getBlockState(event.getPos());

                if (!restriction.isBlockInteractable(clickedBlock)) {
                    event.setCanceled(true);
                    restriction.displayMessage(Attributes.Structure.INTERACT_MESSAGE, structureId, player);
                    AInventoryUtils.updateSelectedSlot(player);
                    break;
                }
            }
        }
    }

    @SubscribeEvent
    public static void onEntityHurt(AttackEntityEvent event) {
        if (!EventGuards.isValidPlayer(event.getEntity())) { return; }

        var player = event.getEntity();
        var target = event.getTarget();
        var level = player.level();
        var dimension = level.dimension();
        var pos = target.getOnPos().above();

        var structures = StructureCollisionManager.SERVER_INSTANCE.getStructuresForBlockPos(dimension, pos);
        for (var structure : structures) {
            var structureId = REGISTRY_ACCESS.getKey(structure.getStructure());
            if (structureId == null) {
                continue;
            }

            var restriction = ARestrictionManager.STRUCTURE_INSTANCE.getRestriction(AHolder.serverAndPlayer(player), structureId);

            if (restriction != null && restriction.isDisabled(Attributes.ATTACKING)) {
                if (!restriction.isEntityTargetable(target.getType())) {
                    event.setCanceled(true);
                    restriction.displayMessage(Attributes.Structure.ATTACK_MESSAGE, structureId, player);
                    break;
                }
            }
        }
    }

    @SubscribeEvent
    public static void onBlockPlaced(BlockEvent.EntityPlaceEvent event) {
        if (event.getEntity() instanceof ServerPlayer player) {
            if (!EventGuards.isValidPlayer(player)) { return; }
            if (player.getServer() == null) { return; }

            var level = player.level();
            var dimension = level.dimension();
            var pos = event.getPos();

            var structures = StructureCollisionManager.SERVER_INSTANCE.getStructuresForBlockPos(dimension, pos);
            for (var structure : structures) {
                var structureId = REGISTRY_ACCESS.getKey(structure.getStructure());
                if (structureId == null) { continue; }

                var restriction = ARestrictionManager.STRUCTURE_INSTANCE.getRestriction(AHolder.serverAndPlayer(player), structureId);
                PlacedBlocksInStructureData.getData(player.getServer(), structureId.toString()).add(event.getPos());

                if (restriction != null && restriction.isDisabled(Attributes.BLOCK_PLACING)) {
                    if (!restriction.isBlockPlaceable(event.getPlacedBlock())) {
                        event.setCanceled(true);
                        restriction.displayMessage(Attributes.Structure.PLACE_MESSAGE, structureId, player);
                        AInventoryUtils.updateSelectedSlot(player);
                        break;
                    }
                }
            }
        }
    }

    @SubscribeEvent
    public static void onExplosionDetonation(ExplosionEvent.Detonate event) {
        var explosion = event.getExplosion();
        var entity = explosion.getIndirectSourceEntity();
        if (entity instanceof ServerPlayer player) {
            if (!EventGuards.isValidPlayer(player)) { return; }

            var level = player.level();
            var dimension = level.dimension();
            var pos = BlockPos.containing(explosion.center());

            var structures = StructureCollisionManager.SERVER_INSTANCE.getStructuresForBlockPos(dimension, pos);

            for (var structure : structures) {
                var structureId = REGISTRY_ACCESS.getKey(structure.getStructure());
                if (structureId == null) {
                    continue;
                }

                var restriction = ARestrictionManager.STRUCTURE_INSTANCE.getRestriction(AHolder.serverAndPlayer(player), structureId);
                if (restriction != null) {
                    if (restriction.isDisabled(Attributes.EXPLOSIONS_AFFECT_BLOCKS)) { event.getAffectedBlocks().clear(); }
                    if (restriction.isDisabled(Attributes.EXPLOSIONS_AFFECT_ENTITIES)) { event.getAffectedEntities().clear(); }

                    break;
                }
            }
        }
    }
}
