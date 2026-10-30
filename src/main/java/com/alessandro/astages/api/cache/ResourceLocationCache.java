package com.alessandro.astages.api.cache;

import com.alessandro.astages.api.manager.ACommonManager;
import com.alessandro.astages.api.manager.registry.ACommonRegistry;
import com.alessandro.astages.api.nullability.NotNullParams;
import com.alessandro.astages.api.restriction.ACommonRestriction;
import net.minecraft.resources.ResourceLocation;

@NotNullParams
public abstract class ResourceLocationCache<R extends ACommonRestriction<?> & Comparable<R>> extends AKeyedRestrictionCache<R, ResourceLocation> {
    public ResourceLocationCache(ACommonRegistry<?> registry) {
        super(registry);
    }

    public ResourceLocationCache(ACommonManager<?, ?> manager) {
        super(manager);
    }
}