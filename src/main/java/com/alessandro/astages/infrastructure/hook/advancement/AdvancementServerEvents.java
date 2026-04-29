package com.alessandro.astages.infrastructure.hook.advancement;

import com.alessandro.astages.AStages;
import com.alessandro.astages.api.event.player.StageAddedPlayerEvent;
import com.alessandro.astages.api.event.server.StageAddedServerEvent;
import com.alessandro.astages.api.nullability.NotNullParams;
import com.alessandro.astages.api.util.AServerUtils;
import com.alessandro.astages.infrastructure.advancement.ACriteriaTriggers;
import net.minecraft.server.level.ServerPlayer;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;

@NotNullParams
@EventBusSubscriber(modid = AStages.MODID)
public class AdvancementServerEvents {
    @SubscribeEvent
    public static void onStageAdded(StageAddedPlayerEvent event) {
        ACriteriaTriggers.STAGE_EARN.get().trigger((ServerPlayer) event.getPlayer());
    }

    @SubscribeEvent
    public static void onStageAdded(StageAddedServerEvent event) {
        AServerUtils.forEachPlayer(
            event.getServer(),
            player -> ACriteriaTriggers.STAGE_EARN.get().trigger(player)
        );
    }

    // public static void onAdvancementEarned(AdvancementEvent.AdvancementEarnEvent event) { }
}
