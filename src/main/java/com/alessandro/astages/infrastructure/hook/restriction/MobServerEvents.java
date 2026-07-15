package com.alessandro.astages.infrastructure.hook.restriction;

import com.alessandro.astages.AStages;
import com.alessandro.astages.api.holder.AHolder;
import com.alessandro.astages.api.nullability.NotNullParams;
import com.alessandro.astages.api.util.APlayerUtils;
import com.alessandro.astages.engine.ARestrictionManager;
import com.alessandro.astages.engine.server.restriction.AMobRestriction;
import com.alessandro.astages.engine.store.Attributes;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.player.Player;
import net.neoforged.bus.api.EventPriority;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.EntityJoinLevelEvent;
import net.neoforged.neoforge.event.entity.player.AttackEntityEvent;
import net.neoforged.neoforge.event.entity.player.PlayerInteractEvent;

@NotNullParams
@EventBusSubscriber(modid = AStages.MODID)
public class MobServerEvents {
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
            var pos = mob.blockPosition();
            var entityType = mob.getType();
            var spawnType = mob.getSpawnType();

            var level = event.getLevel();
            Player nearestPlayer = APlayerUtils.getNearestPlayer(level, pos);
            var restriction = ARestrictionManager.MOB_INSTANCE.getRestriction(AHolder.serverAndPlayer(nearestPlayer), entityType);

            if (restriction != null) {
                if (restriction.isDisabled(Attributes.MOB_SPAWNING)) {
                    preventSpawning(event, restriction);
                    return;
                }

                if (restriction.getDisabledSpawnTypes().contains(spawnType)) {
                    preventSpawning(event, restriction);
                    return;
                }

                var biome = level.getBiome(pos).getKey();
                if (biome != null) {
                    if (restriction.getIgnoredBiomes().contains(biome.location())) {
                        return;
                    }
                }

                var dimensionRS = level.dimension().location();
                if (restriction.getRestrictedDimensions().contains(dimensionRS)) {
                    preventSpawning(event, restriction);
                    return;
                }

                if (biome != null) {
                    if (restriction.getRestrictedBiomes().contains(biome.location())) {
                        preventSpawning(event, restriction);
                        return;
                    }
                }

                var lightLevel = level.getLightEmission(pos);
                if (!restriction.isValueNull(Attributes.MIN_LIGHT_LEVEL) && !restriction.isValueNull(Attributes.MAX_LIGHT_LEVEL)) {
                    if (restriction.get(Attributes.MIN_LIGHT_LEVEL) < lightLevel && lightLevel < restriction.get(Attributes.MAX_LIGHT_LEVEL)) {
                        preventSpawning(event, restriction);
//                     return;
                    }
                } else if (!restriction.isValueNull(Attributes.MIN_LIGHT_LEVEL) && restriction.isValueNull(Attributes.MAX_LIGHT_LEVEL)) {
                    if (restriction.get(Attributes.MIN_LIGHT_LEVEL) < lightLevel) {
                        preventSpawning(event, restriction);
//                     return;
                    }
                } else if (restriction.isValueNull(Attributes.MIN_LIGHT_LEVEL) && !restriction.isValueNull(Attributes.MAX_LIGHT_LEVEL)) {
                    if (lightLevel < restriction.get(Attributes.MAX_LIGHT_LEVEL)) {
                        preventSpawning(event, restriction);
//                     return;
                    }
                }
            }
        }
    }

    private static void preventSpawning(EntityJoinLevelEvent event, AMobRestriction restriction) {
        if (!restriction.isValueNull(Attributes.REPLACE)) {
            var level = event.getLevel();
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
        } else if (restriction.isEnabled(Attributes.SPAWN_WITH_DIFFERENT_EQUIPMENT)) {
            for (var wrapper : restriction.getEquipments()) {
                ((LivingEntity) event.getEntity()).setItemSlot(wrapper.slot(), wrapper.stack());
            }

            return;
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
