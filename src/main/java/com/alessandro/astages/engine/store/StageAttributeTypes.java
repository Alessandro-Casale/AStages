package com.alessandro.astages.engine.store;

import com.alessandro.astages.api.stage.event.ExpiredEvent;
import com.alessandro.astages.api.stage.event.GrantedEvent;
import com.alessandro.astages.api.stage.event.TickEvent;
import com.alessandro.astages.api.store.AttributeType;
import com.google.common.reflect.TypeToken;

import java.util.function.Consumer;

public class StageAttributeTypes {
    public static final AttributeType<Consumer<GrantedEvent>> GRANTED_EVENT = AttributeType.create(new TypeToken<>() { });
    public static final AttributeType<Consumer<TickEvent>> TICK_EVENT = AttributeType.create(new TypeToken<>() { });
    public static final AttributeType<Consumer<ExpiredEvent>> EXPIRED_EVENT = AttributeType.create(new TypeToken<>() { });
}