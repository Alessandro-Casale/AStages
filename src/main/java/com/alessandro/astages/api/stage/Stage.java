package com.alessandro.astages.api.stage;

import com.alessandro.astages.api.nullability.NotNullParamsAndMethodsReturn;
import com.alessandro.astages.engine.AStageManager;
import com.alessandro.astages.api.store.container.AttributeStore;

@NotNullParamsAndMethodsReturn
public class Stage extends BaseStage<Stage> {
    public Stage(String stage) {
        super(stage);
    }

    public Stage(String stage, String description) {
        super(stage, description);
    }

    @Override
    public AttributeStore allowedAttributes() {
        return AttributeStore.compose()
            .withSuper(super.allowedAttributes())
            .withPlugin(AStageManager.ATTACHED_ATTRIBUTES, Stage.class)
            .build();
    }
}