package com.alessandro.astages.api.cache.client;

import com.alessandro.astages.api.cache.AKeyedClientRestrictionCache;
import com.alessandro.astages.api.manager.AClientMinimalManager;
import com.alessandro.astages.api.manager.registry.AClientMinimalRegistry;
import com.alessandro.astages.api.restriction.AClientRestriction;

public abstract class StringClientCache <R extends AClientRestriction<R, ?, ?>> extends AKeyedClientRestrictionCache<R, String> {
    public StringClientCache(AClientMinimalRegistry<?> registry) {
        super(registry);
    }

    public StringClientCache(AClientMinimalManager<?, ?> manager) {
        super(manager);
    }
}