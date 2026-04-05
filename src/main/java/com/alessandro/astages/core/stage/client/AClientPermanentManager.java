package com.alessandro.astages.core.stage.client;

import com.alessandro.astages.api.stage.ClientStage;
import com.alessandro.astages.core.AClientStageManager;
import com.alessandro.astages.store.stage.AStageClientBaseManager;

public class AClientPermanentManager extends AStageClientBaseManager<ClientStage> {
    public void addStage(ClientStage stage) {
        AClientStageManager.GENERIC_INSTANCE.addStageNoCheck(stage);
        addStageInternal(stage.getStage(), stage);
    }
}