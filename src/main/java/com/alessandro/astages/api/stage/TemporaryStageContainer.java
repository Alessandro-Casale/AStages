package com.alessandro.astages.api.stage;

import com.alessandro.astages.api.nullability.NotNullParamsAndMethodsReturn;
import com.alessandro.astages.api.time.AMutableTime;

@NotNullParamsAndMethodsReturn
public class TemporaryStageContainer {
    private final TemporaryStage stage;
    private final AMutableTime currentTimer;

    public TemporaryStageContainer(TemporaryStage stage) {
        this.stage = stage;
        this.currentTimer = stage.getActualTimer();
    }

    public TemporaryStageContainer(TemporaryStage stage, Integer currentTimer) {
        this.stage = stage;
        this.currentTimer = AMutableTime.fromTicks(currentTimer);
    }

    public boolean subtractTicks(int ticks) {
        return currentTimer.subtractTicks(ticks);
    }

    public TemporaryStage getStage() {
        return stage;
    }

    public AMutableTime getCurrentTimer() {
        return currentTimer;
    }
}
