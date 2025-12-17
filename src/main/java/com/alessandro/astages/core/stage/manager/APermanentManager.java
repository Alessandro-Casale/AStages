package com.alessandro.astages.core.stage.manager;

import com.alessandro.astages.api.stage.Stage;
import com.alessandro.astages.core.AStageManager;
import com.alessandro.astages.store.stage.AStageBaseManager;

public class APermanentManager extends AStageBaseManager<Stage> {
    public void addStage(Stage stage) {
        if (AStageManager.GENERIC_INSTANCE.checkForDuplicates(stage)) {
            AStageManager.GENERIC_INSTANCE.addStageNoCheck(stage);
            addStageInternal(stage.getStage(), stage);
        }
    }
}
