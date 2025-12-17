package com.alessandro.astages.api.event.server;

import com.alessandro.astages.api.event.custom.SingleStageServerEvent;
import net.minecraft.server.MinecraftServer;

public class StageAddedServerEvent extends SingleStageServerEvent {
    public StageAddedServerEvent(MinecraftServer server, String stage) {
        super(server, stage);
    }
}
