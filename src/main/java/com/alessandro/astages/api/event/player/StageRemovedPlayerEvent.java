package com.alessandro.astages.api.event.player;

import com.alessandro.astages.api.event.custom.SingleStagePlayerEvent;
import net.minecraft.world.entity.player.Player;

public class StageRemovedPlayerEvent extends SingleStagePlayerEvent {
    public StageRemovedPlayerEvent(Player player, String stage) {
        super(player, stage);
    }
}
