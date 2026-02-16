package com.alessandro.astages.api.stage;

import com.alessandro.astages.api.nullability.NotNullParamsAndMethodsReturn;
import com.alessandro.astages.core.AStageManager;
import com.alessandro.astages.store.AttributeStore;

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
        var pluginAttributes = AStageManager.ATTACHED_ATTRIBUTES.getOrDefault(Stage.class, null);

        if (pluginAttributes != null) {
            return super.allowedAttributes().combineWith(pluginAttributes);
        } else {
            return super.allowedAttributes();
        }
    }
}