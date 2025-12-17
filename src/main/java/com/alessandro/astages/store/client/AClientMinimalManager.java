package com.alessandro.astages.store.client;

import com.alessandro.astages.store.ARestrictionType;

public interface AClientMinimalManager<R> {
    ARestrictionType associatedType();

    R getRestriction(String id);

    void removeRestriction(String id);
}
