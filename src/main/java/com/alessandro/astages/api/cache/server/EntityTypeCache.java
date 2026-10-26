package com.alessandro.astages.api.cache.server;

import com.alessandro.astages.api.cache.AKeyedRestrictionCache;
import com.alessandro.astages.api.manager.AMinimalManager;
import com.alessandro.astages.api.manager.registry.AMinimalRegistry;
import com.alessandro.astages.api.nullability.NotNullParams;
import com.alessandro.astages.api.restriction.ARestriction;
import net.minecraft.world.entity.EntityType;

@NotNullParams
public abstract class EntityTypeCache<R extends ARestriction<R, ?, ?>> extends AKeyedRestrictionCache<R, EntityType<?>> {
    public EntityTypeCache(AMinimalRegistry<?> registry) {
        super(registry);
    }

    public EntityTypeCache(AMinimalManager<?, ?> manager) {
        super(manager);
    }
}