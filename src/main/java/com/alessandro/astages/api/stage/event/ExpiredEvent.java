package com.alessandro.astages.api.stage.event;

import net.minecraft.server.MinecraftServer;
import net.minecraft.world.entity.player.Player;

public class ExpiredEvent extends GenericEvent {
    public ExpiredEvent(Player player, MinecraftServer server, boolean isClientSide) {
        super(player, server, isClientSide);
    }
}
