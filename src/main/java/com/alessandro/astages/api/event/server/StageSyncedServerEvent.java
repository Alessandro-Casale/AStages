package com.alessandro.astages.api.event.server;

import com.alessandro.astages.api.constant.AOperation;
import com.alessandro.astages.api.event.custom.ServerEvent;
import com.alessandro.astages.api.util.ASetUtils;
import net.minecraft.server.MinecraftServer;
import net.neoforged.bus.api.ICancellableEvent;

import java.util.Set;

public class StageSyncedServerEvent extends ServerEvent implements ICancellableEvent {
    final AOperation operation;
    final Set<String> stagesSynced;

    public StageSyncedServerEvent(MinecraftServer server, AOperation operation, String stageSynced) {
        this(server, operation, ASetUtils.singleton(stageSynced));
    }

    public StageSyncedServerEvent(MinecraftServer server, AOperation operation, Set<String> stagesSynced) {
        super(server);
        this.operation = operation;
        this.stagesSynced = stagesSynced;
    }

    public AOperation getOperation() {
        return operation;
    }

    public Set<String> getStagesSynced() {
        return stagesSynced;
    }
}
