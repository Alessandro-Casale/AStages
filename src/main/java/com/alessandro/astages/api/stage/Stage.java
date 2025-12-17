package com.alessandro.astages.api.stage;

import com.alessandro.astages.api.nullability.NotNullParamsAndMethodsReturn;

@NotNullParamsAndMethodsReturn
public class Stage extends BaseStage<Stage> {
    public Stage(String stage) {
        super(stage);
    }

    public Stage(String stage, String description) {
        super(stage, description);
    }
}
