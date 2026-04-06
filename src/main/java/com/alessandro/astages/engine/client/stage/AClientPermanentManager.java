package com.alessandro.astages.engine.client.stage;

import com.alessandro.astages.api.stage.AStageClientBaseManager;
import com.alessandro.astages.api.stage.ClientStage;
import com.alessandro.astages.engine.AClientStageManager;

public class AClientPermanentManager extends AStageClientBaseManager<ClientStage> {
    public void addStage(ClientStage stage) {
        AClientStageManager.GENERIC_INSTANCE.addStageNoCheck(stage);
        addStageInternal(stage.getStage(), stage);
    }
}