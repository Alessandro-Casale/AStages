package com.alessandro.astages.event.custom;

import com.alessandro.astages.api.constant.AOperation;
import com.alessandro.astages.event.custom.types.ClientStageEvent;

import java.util.Set;

public class ClientSynchronizeServerStagesEvent extends ClientStageEvent {
    public ClientSynchronizeServerStagesEvent(Set<String> serverStagesSynced, AOperation operation) {
        super(serverStagesSynced, operation);
    }
}
