package com.alessandro.astages.api.stage.event;

import net.minecraft.server.MinecraftServer;
import net.minecraft.world.entity.player.Player;

public class GenericEvent {
    private final Player player;
    private final MinecraftServer server;
    private final boolean isClientSide;

    public GenericEvent(Player player, MinecraftServer server, boolean isClientSide) {
        this.player = player;
        this.server = server;
        this.isClientSide = isClientSide;
    }

    public Player getPlayer() {
        return player;
    }

    public MinecraftServer getServer() {
        return server;
    }

    public boolean isClientSide() {
        return isClientSide;
    }

    public boolean isPlayerAvailable() {
        return player != null;
    }

    public boolean isServerAvailable() {
        return server != null;
    }
}
