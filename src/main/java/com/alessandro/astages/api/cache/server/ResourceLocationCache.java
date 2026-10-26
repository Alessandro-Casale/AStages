package com.alessandro.astages.api.cache.server;

import com.alessandro.astages.api.cache.AKeyedRestrictionCache;
import com.alessandro.astages.api.manager.AMinimalManager;
import com.alessandro.astages.api.manager.registry.AMinimalRegistry;
import com.alessandro.astages.api.nullability.NotNullParams;
import com.alessandro.astages.api.restriction.ARestriction;
import net.minecraft.resources.ResourceLocation;

@NotNullParams
public abstract class ResourceLocationCache<R extends ARestriction<R, ?, ?>> extends AKeyedRestrictionCache<R, ResourceLocation> {
    public ResourceLocationCache(AMinimalRegistry<?> registry) {
        super(registry);
    }

    public ResourceLocationCache(AMinimalManager<?, ?> manager) {
        super(manager);
    }
}