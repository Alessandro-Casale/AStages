package com.alessandro.astages.api.event.custom;

import net.minecraft.server.MinecraftServer;

import java.util.Set;

public class MultipleStagesServerEvent extends ServerEvent {
    public final Set<String> stages;

    public MultipleStagesServerEvent(MinecraftServer server, Set<String> stages) {
        super(server);
        this.stages = stages;
    }

    public Set<String> getStages() {
        return stages;
    }
}
