package com.alessandro.astages.infrastructure.hook.restriction;

import com.alessandro.astages.AStages;
import com.alessandro.astages.api.holder.AHolder;
import com.alessandro.astages.api.nullability.NotNullParams;
import com.alessandro.astages.api.util.APlayerUtils;
import com.alessandro.astages.engine.ARestrictionManager;
import com.alessandro.astages.engine.server.restriction.AMobRestriction;
import com.alessandro.astages.engine.store.Attributes;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.player.Player;
import net.neoforged.bus.api.EventPriority;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.EntityJoinLevelEvent;
import net.neoforged.neoforge.event.entity.living.FinalizeSpawnEvent;
import net.neoforged.neoforge.event.entity.player.AttackEntityEvent;
import net.neoforged.neoforge.event.entity.player.PlayerInteractEvent;

@NotNullParams
@EventBusSubscriber(modid = AStages.MODID)
public class MobServerEvents {
    /**
     * This event is used to capture the MobSpawnType. <br> <br>
     * IMPORTANT: This event can fire on asynchronous worker threads (e.g., during chunk generation).
     * We only attach a data component here and avoid any world-access logic or player searches to prevent
     * thread deadlocks between the WorldGen worker and the Main Server thread.
     */
    @SubscribeEvent
    public static void onEntityJoin(FinalizeSpawnEvent event) {
        if (event.getLevel().isClientSide()) {
            return;
        }

//        event.getEntity()
//            .setData(AProvider.SPAWN_TYPE, Optional.of(event.getSpawnType()));
    }

    /**
     * This event fires when the entity is actually added to the level.
     * It runs on the Main Server thread, making it safe to perform proximity checks for players
     * and access game stages. We retrieve the SpawnType information stored earlier via data components.
     */
    @SubscribeEvent(priority = EventPriority.LOWEST)
    public static void checkMobSpawning(EntityJoinLevelEvent event) {
        if (event.getLevel().isClientSide()) {
            return;
        }

        if (event.getEntity() instanceof Mob mob) {
            var x = mob.getBlockX();
            var y = mob.getBlockY();
            var z = mob.getBlockZ();
            var pos = new BlockPos(x, y, z);

            var entityType = mob.getType();
            var spawnType = mob.getSpawnType();

            var level = event.getLevel();
            Player nearestPlayer = APlayerUtils.getNearestPlayer(level, pos);
            var restriction = ARestrictionManager.MOB_INSTANCE.getRestriction(AHolder.serverAndPlayer(nearestPlayer), entityType);

            if (restriction != null) {
                AStages.LOGGER.debug("Restriction: {}, {}, {}", restriction.getId(), restriction.get(Attributes.MOB_SPAWNING), restriction.getDisabledSpawnTypes());

                if (restriction.isDisabled(Attributes.MOB_SPAWNING)) {
                    preventSpawning(event, restriction);
                    AStages.LOGGER.debug("Mob Spawning");
                    return;
                }

                if (restriction.getDisabledSpawnTypes().contains(spawnType)) {
                    preventSpawning(event, restriction);
                    AStages.LOGGER.debug("Spawn Type");
                    return;
                }

                if (!restriction.isValueNull(Attributes.DIMENSION)) {
                    if (restriction.get(Attributes.DIMENSION).equals(level.dimension().location())) {
                        preventSpawning(event, restriction);
                        AStages.LOGGER.debug("Dimension");
                        return;
                    }
                }

                var biome = level.getBiome(pos).getKey();
                if (biome != null) {
                    var biomeRS = biome.location();
                    if (restriction.getRestrictedBiomes().contains(biomeRS)) {
                        preventSpawning(event, restriction);
                        AStages.LOGGER.debug("Biome");
                        return;
                    }
                }

                var lightLevel = level.getLightEmission(pos);
                if (!restriction.isValueNull(Attributes.MIN_LIGHT_LEVEL) && !restriction.isValueNull(Attributes.MAX_LIGHT_LEVEL)) {
                    if (restriction.get(Attributes.MIN_LIGHT_LEVEL) < lightLevel && lightLevel < restriction.get(Attributes.MAX_LIGHT_LEVEL)) {
                        preventSpawning(event, restriction);
                        AStages.LOGGER.debug("Light1");
//                     return;
                    }
                } else if (!restriction.isValueNull(Attributes.MIN_LIGHT_LEVEL) && restriction.isValueNull(Attributes.MAX_LIGHT_LEVEL)) {
                    if (restriction.get(Attributes.MIN_LIGHT_LEVEL) < lightLevel) {
                        preventSpawning(event, restriction);
                        AStages.LOGGER.debug("Light2");
//                     return;
                    }
                } else if (restriction.isValueNull(Attributes.MIN_LIGHT_LEVEL) && !restriction.isValueNull(Attributes.MAX_LIGHT_LEVEL)) {
                    if (lightLevel < restriction.get(Attributes.MAX_LIGHT_LEVEL)) {
                        preventSpawning(event, restriction);
                        AStages.LOGGER.debug("Light3");
//                     return;
                    }
                }
            }
        }
    }

    private static void preventSpawning(EntityJoinLevelEvent event, AMobRestriction restriction) {
        // If prevent spawn, you can place the replacer!
        var level = event.getLevel();

        if (!restriction.isValueNull(Attributes.REPLACE)) {
            Entity newEntity = restriction.get(Attributes.REPLACE).create(level);

            if (newEntity != null) {
                if (newEntity instanceof LivingEntity) {
                    if (restriction.isEnabled(Attributes.SPAWN_WITH_DIFFERENT_EQUIPMENT)) {
                        for (var wrapper : restriction.getEquipments()) {
                            ((LivingEntity) newEntity).setItemSlot(wrapper.slot(), wrapper.stack());
                        }
                    }
                }

                newEntity.setPos(event.getEntity().getBlockX(), event.getEntity().getBlockY(), event.getEntity().getBlockZ());
                level.addFreshEntity(newEntity);
            } else {
                AStages.LOGGER.warn("Features disabled in this level to spawn the replacer for restriction with id {}!", restriction.getId());
            }
        }

        event.setCanceled(true);
    }

    @SubscribeEvent(priority = EventPriority.HIGH)
    public static void onPlayerInteract(PlayerInteractEvent.EntityInteract event) {
        var player = event.getEntity();
        var entityType = event.getTarget().getType();

        var restriction = ARestrictionManager.MOB_INSTANCE.getRestriction(AHolder.serverAndPlayer(player), entityType);

        if (restriction != null && restriction.isDisabled(Attributes.RIGHT_CLICK_INTERACTIONS)) {
            event.setCanceled(true);
            player.displayClientMessage(restriction.get(Attributes.Mob.INTERACTION_MESSAGE).get(), true);
        }
    }

    @SubscribeEvent(priority = EventPriority.HIGH)
    public static void onPlayerAttack(AttackEntityEvent event) {
        var player = event.getEntity();
        var entityType = event.getTarget().getType();

        var restriction = ARestrictionManager.MOB_INSTANCE.getRestriction(AHolder.serverAndPlayer(player), entityType);

        if (restriction != null && restriction.isDisabled(Attributes.ATTACKING)) {
            event.setCanceled(true);
            player.displayClientMessage(restriction.get(Attributes.Mob.ATTACK_MESSAGE).get(), true);
        }
    }
}
