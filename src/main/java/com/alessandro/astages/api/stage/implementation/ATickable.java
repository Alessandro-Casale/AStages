package com.alessandro.astages.api.stage.implementation;

import com.alessandro.astages.api.nullability.NotNullParams;
import com.alessandro.astages.api.stage.event.TickEvent;

@NotNullParams
public interface ATickable {
    void postTickEvent(TickEvent event);
}
