package com.alessandro.astages.api.event.sync;

import com.alessandro.astages.api.constant.AOperation;
import com.alessandro.astages.api.develop.Info;

import java.util.Set;

@Info("Sync event for client!")
public class ClientSynchronizeStagesEvent extends ClientStageEvent {
    public ClientSynchronizeStagesEvent(Set<String> stagesSynced, AOperation operation) {
        super(stagesSynced, operation);
    }
}
