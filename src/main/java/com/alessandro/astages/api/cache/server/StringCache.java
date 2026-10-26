package com.alessandro.astages.api.cache.server;

import com.alessandro.astages.api.cache.AKeyedRestrictionCache;
import com.alessandro.astages.api.manager.AMinimalManager;
import com.alessandro.astages.api.manager.registry.AMinimalRegistry;
import com.alessandro.astages.api.restriction.ARestriction;

public abstract class StringCache<R extends ARestriction<R, ?, ?>> extends AKeyedRestrictionCache<R, String> {
    public StringCache(AMinimalRegistry<?> registry) {
        super(registry);
    }

    public StringCache(AMinimalManager<?, ?> manager) {
        super(manager);
    }
}