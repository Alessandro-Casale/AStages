package com.alessandro.astages.api.reload;

import com.alessandro.astages.api.constant.AOperation;
import com.alessandro.astages.api.constant.AStageSource;
import com.alessandro.astages.api.nullability.NotNullMethodsReturn;

import java.util.Set;

@NotNullMethodsReturn
public class ClientReloadContext {
    private AStageSource source;
    private AOperation operation;
    private Set<String> stagesSynced;

    public ClientReloadContext() { }

    private ClientReloadContext(AStageSource source, AOperation operation, Set<String> stagesSynced) {
        this.source = source;
        this.operation = operation;
        this.stagesSynced = stagesSynced;
    }

    public static ClientReloadContext withStagesSynced(AStageSource source, AOperation operation, Set<String> stagesSynced) {
        return new ClientReloadContext(source, operation, stagesSynced);
    }

    public AStageSource getSource() {
        return source;
    }

    public AOperation getOperation() {
        return operation;
    }

    public Set<String> getStagesSynced() {
        return stagesSynced;
    }
}