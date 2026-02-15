package com.alessandro.astages.api.stage;

import com.alessandro.astages.api.nullability.NotNullParamsAndMethodsReturn;
import com.alessandro.astages.api.stage.event.ExpiredEvent;
import com.alessandro.astages.api.stage.event.TickEvent;
import com.alessandro.astages.api.stage.implementation.AExpirable;
import com.alessandro.astages.api.stage.implementation.ATickable;
import com.alessandro.astages.api.time.AMutableTime;
import com.alessandro.astages.api.time.ATime;
import com.alessandro.astages.store.AttributeStore;
import com.alessandro.astages.store.StageAttributes;

import java.util.function.Consumer;

@NotNullParamsAndMethodsReturn
public class TemporaryStage extends BaseStage<TemporaryStage> implements ATickable, AExpirable {
    private final AMutableTime timer;

    public TemporaryStage(String stage, AMutableTime initialTimer) {
        super(stage);
        this.timer = initialTimer;
    }

    public TemporaryStage(String stage, ATime initialTimer) {
        super(stage);
        this.timer = AMutableTime.fromFixed(initialTimer);
    }

    public TemporaryStage(String stage, String description, AMutableTime initialTimer) {
        super(stage, description);
        this.timer = initialTimer;
    }

    @Override
    public AttributeStore allowedAttributes() {
        var attributeStore = new AttributeStore()
            .addAttribute(StageAttributes.TICK_EVENT)
            .addAttribute(StageAttributes.EXPIRED_EVENT);

        return super.allowedAttributes().combineWith(attributeStore);
    }

    public AMutableTime getActualTimer() {
        return timer;
    }

    public TemporaryStage everyTick(Consumer<TickEvent> consumer) {
        set(StageAttributes.TICK_EVENT, consumer);
        return this;
    }

    @Override
    public void postTickEvent(TickEvent event) {
        get(StageAttributes.TICK_EVENT).accept(event);
    }

    public TemporaryStage whenExpired(Consumer<ExpiredEvent> consumer) {
        set(StageAttributes.EXPIRED_EVENT, consumer);
        return this;
    }

    @Override
    public void postExpiredEvent(ExpiredEvent event) {
        get(StageAttributes.EXPIRED_EVENT).accept(event);
    }
}