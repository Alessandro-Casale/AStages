package com.alessandro.astages.api.stage.implementation;

import com.alessandro.astages.api.nullability.NotNullParams;
import com.alessandro.astages.api.stage.event.GrantedEvent;

@NotNullParams
public interface AGrantable {
    void postGrantedEvent(GrantedEvent event);
}
