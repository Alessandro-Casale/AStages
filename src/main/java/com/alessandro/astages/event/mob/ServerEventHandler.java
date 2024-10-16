package com.alessandro.astages.event.mob;

import com.alessandro.astages.AStages;
import com.alessandro.astages.core.ARestrictionManager;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraftforge.event.entity.living.MobSpawnEvent;
import net.minecraftforge.eventbus.api.Event;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

@Mod.EventBusSubscriber(modid = AStages.MODID)
public class ServerEventHandler {
    @SubscribeEvent
    public static void checkMobSpawning(MobSpawnEvent.@NotNull PositionCheck event) {

//        if (event.getSpawnType() == MobSpawnType.SPAWNER)
//            return;

        var restriction = ARestrictionManager.MOB_INSTANCE.getRestriction(event.getEntity().getType());

        if (restriction != null) {
            if (event.getSpawnType() == MobSpawnType.SPAWNER && !restriction.disableSpawner) {
                event.setResult(Event.Result.DENY);
                return;
            }

            if (restriction.isDimensionSet && restriction.dimension != null) {
                event.setResult(Event.Result.DENY);
                return;
            }

            if (restriction.shouldReplace && restriction.replacing != null) {
                var level = event.getLevel().getLevel();

                Entity newEntity = Objects.requireNonNull(restriction.replacing.create(level));
                newEntity.setPos(event.getX(), event.getY(), event.getZ());
                level.addFreshEntity(newEntity);

                event.setResult(Event.Result.DENY);
                return;
            }

            event.setResult(Event.Result.DENY);
        }
    }
}
