package com.alessandro.astages.api.stage.implementation;

import com.alessandro.astages.api.nullability.NotNullParams;
import com.alessandro.astages.api.stage.event.ExpiredEvent;

@NotNullParams
public interface AExpirable {
    void postExpiredEvent(ExpiredEvent event);
}
