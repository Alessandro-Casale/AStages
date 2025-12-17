package com.alessandro.astages.api.event.server;

import com.alessandro.astages.api.event.custom.MultipleStagesServerEvent;
import net.minecraft.server.MinecraftServer;

import java.util.Set;

public class StageLoginServerEvent extends MultipleStagesServerEvent {
    public StageLoginServerEvent(MinecraftServer server, Set<String> stages) {
        super(server, stages);
    }
}
