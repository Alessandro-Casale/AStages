package com.alessandro.astages.api.cache;

import com.alessandro.astages.api.manager.ACommonManager;
import com.alessandro.astages.api.manager.registry.ACommonRegistry;
import com.alessandro.astages.api.nullability.NotNullParams;
import com.alessandro.astages.api.restriction.ACommonRestriction;
import net.minecraft.resources.ResourceKey;

@NotNullParams
public abstract class ResourceKeyCache<R extends ACommonRestriction<?> & Comparable<R>, K> extends AKeyedRestrictionCache<R, ResourceKey<K>> {
    public ResourceKeyCache(ACommonRegistry<?> registry) {
        super(registry);
    }

    public ResourceKeyCache(ACommonManager<?, ?> manager) {
        super(manager);
    }
}