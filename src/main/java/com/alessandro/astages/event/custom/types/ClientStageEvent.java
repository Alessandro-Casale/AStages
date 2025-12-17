package com.alessandro.astages.event.custom.types;

import com.alessandro.astages.api.constant.AOperation;
import net.neoforged.bus.api.Event;

import java.util.Set;

public class ClientStageEvent extends Event {
    private final Set<String> stagesSynced;
    private final AOperation operation;

    public ClientStageEvent(Set<String> stagesSynced, AOperation operation) {
        this.stagesSynced = stagesSynced;
        this.operation = operation;
    }

    public Set<String> getStagesSynced() {
        return stagesSynced;
    }

    public AOperation getOperation() {
        return operation;
    }
}
