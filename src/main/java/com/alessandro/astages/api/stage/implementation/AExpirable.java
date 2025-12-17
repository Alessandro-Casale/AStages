package com.alessandro.astages.api.stage.implementation;

import com.alessandro.astages.api.nullability.NotNullParams;
import com.alessandro.astages.api.stage.event.ExpiredEvent;

@NotNullParams
public interface AExpirable {
    boolean hasCustomExpiredEvent();
    void postExpiredEvent(ExpiredEvent event);
}
