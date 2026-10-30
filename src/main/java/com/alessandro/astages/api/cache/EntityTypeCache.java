package com.alessandro.astages.api.cache;

import com.alessandro.astages.api.manager.ACommonManager;
import com.alessandro.astages.api.manager.registry.ACommonRegistry;
import com.alessandro.astages.api.nullability.NotNullParams;
import com.alessandro.astages.api.restriction.ACommonRestriction;
import net.minecraft.world.entity.EntityType;

@NotNullParams
public abstract class EntityTypeCache<R extends ACommonRestriction<?> & Comparable<R>> extends AKeyedRestrictionCache<R, EntityType<?>> {
    public EntityTypeCache(ACommonRegistry<?> registry) {
        super(registry);
    }

    public EntityTypeCache(ACommonManager<?, ?> manager) {
        super(manager);
    }
}