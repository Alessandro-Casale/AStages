package com.alessandro.astages.api.cache.client;

import com.alessandro.astages.api.cache.AKeyedClientRestrictionCache;
import com.alessandro.astages.api.manager.AClientMinimalManager;
import com.alessandro.astages.api.manager.registry.AClientMinimalRegistry;
import com.alessandro.astages.api.nullability.NotNullParams;
import com.alessandro.astages.api.restriction.AClientRestriction;
import net.minecraft.resources.ResourceLocation;

@NotNullParams
public abstract class ResourceLocationClientCache<R extends AClientRestriction<R, ?, ?>> extends AKeyedClientRestrictionCache<R, ResourceLocation> {
    public ResourceLocationClientCache(AClientMinimalRegistry<?> registry) {
        super(registry);
    }

    public ResourceLocationClientCache(AClientMinimalManager<?, ?> manager) {
        super(manager);
    }
}