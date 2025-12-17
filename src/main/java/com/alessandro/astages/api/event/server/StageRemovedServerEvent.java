package com.alessandro.astages.api.event.server;

import com.alessandro.astages.api.event.custom.SingleStageServerEvent;
import net.minecraft.server.MinecraftServer;

public class StageRemovedServerEvent extends SingleStageServerEvent {
    public StageRemovedServerEvent(MinecraftServer server, String stage) {
        super(server, stage);
    }
}
