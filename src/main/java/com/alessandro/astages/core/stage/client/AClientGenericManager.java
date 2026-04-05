package com.alessandro.astages.core.stage.client;

import com.alessandro.astages.api.develop.Info;
import com.alessandro.astages.api.stage.ClientBaseStage;
import com.alessandro.astages.api.stage.ClientStage;
import com.alessandro.astages.store.stage.AStageClientBaseManager;

public class AClientGenericManager extends AStageClientBaseManager<ClientBaseStage<?>> {
    public void addStage(String key, ClientStage stage) {
        addStageInternal(key, stage);
    }

    @Info("Only for consistency with server-side AGenericManager")
    public void addStageNoCheck(ClientBaseStage<?> stage) {
        addStageInternal(stage.getStage(), stage);
    }
}