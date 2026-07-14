package com.alessandro.astages.api.manager;

import com.alessandro.astages.api.holder.AHolder;
import com.alessandro.astages.api.holder.ARestrictionHolder;
import com.alessandro.astages.api.reload.AReloadable;
import com.alessandro.astages.api.restriction.ARestriction;
import com.alessandro.astages.api.store.ARestrictionType;
import com.alessandro.astages.api.manager.registry.AMinimalRegistry;

public interface AMinimalManager<R extends ARestriction<?, ?, ?>, V> extends AReloadable {
    R getRestriction(String id);
    default ARestrictionHolder<R> getHolder(String id) {
        return ARestrictionHolder.hold(getRestriction(id));
    }

    R getRestriction(AHolder holder, V object);
    default ARestrictionHolder<R> getHolder(AHolder holder, V object) {
        return ARestrictionHolder.hold(getRestriction(holder, object));
    }

    void removeRestriction(String id);

    AMinimalRegistry<R> getRegistry();

    ARestrictionType associatedType();
}