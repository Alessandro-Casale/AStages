package com.alessandro.astages.api.event.custom;

import net.minecraft.world.entity.player.Player;

public class SingleStagePlayerEvent extends PlayerEvent {
    public final String stage;

    public SingleStagePlayerEvent(Player player, String stage) {
        super(player);
        this.stage = stage;
    }

    public String getStage() {
        return stage;
    }
}
