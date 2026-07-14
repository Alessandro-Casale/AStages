package com.alessandro.astages.api.manager;

import com.alessandro.astages.api.holder.AClientHolder;
import com.alessandro.astages.api.holder.AClientRestrictionHolder;
import com.alessandro.astages.api.reload.AReloadable;
import com.alessandro.astages.api.store.ARestrictionType;
import com.alessandro.astages.api.manager.registry.AClientMinimalRegistry;
import com.alessandro.astages.api.restriction.AClientRestriction;

public interface AClientMinimalManager<R extends AClientRestriction<?, ?, ?>, V> extends AReloadable {
    R getRestriction(String id);
    default AClientRestrictionHolder<R> getHolder(String id) {
        return AClientRestrictionHolder.hold(getRestriction(id));
    }

    R getRestriction(AClientHolder holder, V object);
    default AClientRestrictionHolder<R> getHolder(AClientHolder holder, V object) {
        return AClientRestrictionHolder.hold(getRestriction(holder, object));
    }

    void removeRestriction(String id);

    AClientMinimalRegistry<R> getRegistry();

    ARestrictionType associatedType();
}