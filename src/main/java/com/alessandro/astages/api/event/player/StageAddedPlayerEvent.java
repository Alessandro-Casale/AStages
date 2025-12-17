package com.alessandro.astages.api.event.player;

import com.alessandro.astages.api.event.custom.SingleStagePlayerEvent;
import net.minecraft.world.entity.player.Player;

public class StageAddedPlayerEvent extends SingleStagePlayerEvent {
    public StageAddedPlayerEvent(Player player, String stage) {
        super(player, stage);
    }
}
