package com.alessandro.astages.event.custom;

import net.minecraft.world.entity.player.Player;
import net.minecraftforge.event.entity.player.PlayerEvent;

public class StageAddedPlayerEvent extends PlayerEvent {
    public final String stage;

    public StageAddedPlayerEvent(Player player, String stage) {
        super(player);
        this.stage = stage;
    }
}
