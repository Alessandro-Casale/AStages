package com.alessandro.astages.api.event.custom;

import net.minecraft.server.MinecraftServer;
import net.neoforged.bus.api.Event;

public class ServerEvent extends Event {
    private final MinecraftServer server;

    public ServerEvent(MinecraftServer server) {
        this.server = server;
    }

    public MinecraftServer getServer() {
        return server;
    }
}
