package com.alessandro.astages.store.server;

import com.alessandro.astages.store.ARestrictionType;

public interface AMinimalManager<R> {
    ARestrictionType associatedType();

    R getRestriction(String id);

    void removeRestriction(String id);

    void reloadBeforeScripts();

    void reloadAfterScripts();
}
