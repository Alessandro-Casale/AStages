package com.alessandro.astages.api.cache;

import com.alessandro.astages.api.manager.ACommonManager;
import com.alessandro.astages.api.manager.registry.ACommonRegistry;
import com.alessandro.astages.api.restriction.ACommonRestriction;

public abstract class StringCache<R extends ACommonRestriction<?> & Comparable<R>> extends AKeyedRestrictionCache<R, String> {
    public StringCache(ACommonRegistry<?> registry) {
        super(registry);
    }

    public StringCache(ACommonManager<?, ?> manager) {
        super(manager);
    }
}