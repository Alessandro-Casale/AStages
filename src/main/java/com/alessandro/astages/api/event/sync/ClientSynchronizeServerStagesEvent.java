package com.alessandro.astages.api.event.sync;

import com.alessandro.astages.api.constant.AOperation;

import java.util.Set;

public class ClientSynchronizeServerStagesEvent extends ClientStageEvent {
    public ClientSynchronizeServerStagesEvent(Set<String> serverStagesSynced, AOperation operation) {
        super(serverStagesSynced, operation);
    }
}
