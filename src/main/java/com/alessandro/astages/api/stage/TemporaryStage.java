package com.alessandro.astages.api.stage;

import com.alessandro.astages.api.nullability.NotNullParamsAndMethodsReturn;
import com.alessandro.astages.api.stage.event.ExpiredEvent;
import com.alessandro.astages.api.stage.event.TickEvent;
import com.alessandro.astages.api.stage.implementation.AExpirable;
import com.alessandro.astages.api.stage.implementation.ATickable;
import com.alessandro.astages.api.time.AMutableTime;

import java.util.function.Consumer;

@NotNullParamsAndMethodsReturn
public class TemporaryStage extends BaseStage<TemporaryStage> implements ATickable, AExpirable {
    private final AMutableTime timer;

    private boolean hasCustomTickEvent = false;
    private Consumer<TickEvent> tickEvent;
    private boolean hasCustomExpiredEvent = false;
    private Consumer<ExpiredEvent> expiredEvent;

    public TemporaryStage(String stage, AMutableTime initialTimer) {
        super(stage);
        this.timer = initialTimer;
    }

    public TemporaryStage(String stage, String description, AMutableTime initialTimer) {
        super(stage, description);
        this.timer = initialTimer;
    }

    public AMutableTime getActualTimer() {
        return timer;
    }

    @Override
    public boolean hasCustomTickEvent() {
        return hasCustomTickEvent;
    }

    public TemporaryStage everyTick(Consumer<TickEvent> consumer) {
        tickEvent = consumer;
        hasCustomTickEvent = true;
        return this;
    }

    @Override
    public void postTickEvent(TickEvent event) {
        tickEvent.accept(event);
    }

    @Override
    public boolean hasCustomExpiredEvent() {
        return hasCustomExpiredEvent;
    }

    public TemporaryStage whenExpired(Consumer<ExpiredEvent> consumer) {
        expiredEvent = consumer;
        hasCustomExpiredEvent = true;
        return this;
    }

    @Override
    public void postExpiredEvent(ExpiredEvent event) {
        expiredEvent.accept(event);
    }
}
