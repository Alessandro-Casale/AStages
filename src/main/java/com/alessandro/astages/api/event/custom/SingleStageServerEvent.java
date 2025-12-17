package com.alessandro.astages.api.event.custom;

import net.minecraft.server.MinecraftServer;

public class SingleStageServerEvent extends ServerEvent {
    public final String stage;

    public SingleStageServerEvent(MinecraftServer server, String stage) {
        super(server);
        this.stage = stage;
    }

    public String getStage() {
        return stage;
    }
}
