package com.alessandro.astages.api.event.custom;

import net.minecraft.world.entity.player.Player;
import net.neoforged.bus.api.Event;

public class PlayerEvent extends Event {
    private final Player player;

    public PlayerEvent(Player player) {
        this.player = player;
    }

    public Player getPlayer() {
        return player;
    }
}
