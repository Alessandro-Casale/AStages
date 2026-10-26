package com.alessandro.astages.api.cache.client;

import com.alessandro.astages.api.cache.AKeyedClientRestrictionCache;
import com.alessandro.astages.api.manager.AClientMinimalManager;
import com.alessandro.astages.api.manager.registry.AClientMinimalRegistry;
import com.alessandro.astages.api.nullability.NotNullParams;
import com.alessandro.astages.api.restriction.AClientRestriction;
import net.minecraft.resources.ResourceKey;

@NotNullParams
public abstract class ResourceKeyClientCache<R extends AClientRestriction<R, ?, ?>, K> extends AKeyedClientRestrictionCache<R, ResourceKey<K>> {
    public ResourceKeyClientCache(AClientMinimalRegistry<?> registry) {
        super(registry);
    }

    public ResourceKeyClientCache(AClientMinimalManager<?, ?> manager) {
        super(manager);
    }
}