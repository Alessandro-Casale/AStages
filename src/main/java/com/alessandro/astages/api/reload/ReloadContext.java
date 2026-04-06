package com.alessandro.astages.api.reload;

import net.minecraft.server.MinecraftServer;

public class ReloadContext {
    private final MinecraftServer server;

    public ReloadContext(MinecraftServer server) {
        this.server = server;
    }

    public MinecraftServer getServer() {
        return server;
    }
}