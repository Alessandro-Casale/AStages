package com.alessandro.astages.internal.debug;

import com.alessandro.astages.AStages;
import com.alessandro.astages.api.AResourceLocation;
import com.alessandro.astages.api.constant.AEventPhase;
import com.alessandro.astages.api.event.AddRestrictionEvent;
import com.alessandro.astages.api.event.AddStageEvent;
import com.alessandro.astages.api.time.ATime;
import com.alessandro.astages.api.util.ARestrictionUtils;
import com.alessandro.astages.api.util.AStagesUtils;
import com.alessandro.astages.infrastructure.config.AStagesCommon;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.item.Items;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.tick.PlayerTickEvent;

@EventBusSubscriber(modid = AStages.MODID)
public class DebugEvents {
    @SubscribeEvent
    public static void addRestriction(AddRestrictionEvent event) {
        if (!AStagesCommon.ENABLE_TEST_MODE.get()) { return; }
        if (event.getEventPhase() != AEventPhase.BEFORE_JS) { return; }

        ARestrictionUtils.addRestrictionForItem("astages:item1", "stage_item_1", Items.ACACIA_BOAT);
        ARestrictionUtils.addRestrictionForItem("astages:item2", "stage_item_2", Items.ACACIA_PLANKS)
            .setCanBeStoredInInventory(true)
            .setCanPickedUp(true)
            .setCanBeStoredInContainers(false);
        ARestrictionUtils.addRestrictionForTag("astages:item3", "stage_item_3", AResourceLocation.fromTag("#forge:ingots/iron"));

        ARestrictionUtils.addRestrictionForMob("astages:mob1", "stage_mob_1", EntityType.BEE)
            .setDimension(AResourceLocation.parse("minecraft:overworld"))
            .restrictSpawnType(MobSpawnType.SPAWN_EGG)
            .restrictBiomeSpawn(AResourceLocation.parse("minecraft:plains"));
    }

    @SubscribeEvent
    public static void addStage(AddStageEvent event) {
        if (!AStagesCommon.ENABLE_TEST_MODE.get()) { return; }
        if (event.getEventPhase() != AEventPhase.BEFORE_JS) { return; }

        AStagesUtils.customizeStage("stage_permanent");

        AStagesUtils.customizeTemporaryStage("stage_temporary", new ATime("1m"))
            .whenGranted(e -> e.getPlayer())
            .everyTick(e -> {
                var server = e.getServer();

                if (server != null) {
                    server.sendSystemMessage(Component.literal("Tick!"));
                }
            })
            .whenExpired(e -> e.getPlayer());
    }

    @SubscribeEvent
    public static void playerTick(PlayerTickEvent.Pre event) {
        if (!AStagesCommon.ENABLE_TEST_MODE.get()) { return; }

//        AStages.LOGGER.debug(AClientStageManager.PERMANENT_INSTANCE.getStages().toString());
//        AStages.LOGGER.debug(AClientStageManager.TEMPORARY_INSTANCE.getStages().toString());
//        AStages.LOGGER.debug(AStageManager.PERMANENT_INSTANCE.getStages().toString());
//        AStages.LOGGER.debug(AStageManager.TEMPORARY_INSTANCE.getStages().toString());
    }
}