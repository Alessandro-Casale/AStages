package com.alessandro.astages.core.stage.client;

import com.alessandro.astages.api.stage.ClientTemporaryStage;
import com.alessandro.astages.core.AClientStageManager;
import com.alessandro.astages.store.stage.AStageClientBaseManager;

public class AClientTemporaryManager extends AStageClientBaseManager<ClientTemporaryStage> {
    public void addStage(ClientTemporaryStage stage) {
        AClientStageManager.GENERIC_INSTANCE.addStageNoCheck(stage);
        addStageInternal(stage.getStage(), stage);
    }
}