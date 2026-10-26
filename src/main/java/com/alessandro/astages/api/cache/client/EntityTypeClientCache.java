package com.alessandro.astages.api.cache.client;

import com.alessandro.astages.api.cache.AKeyedClientRestrictionCache;
import com.alessandro.astages.api.manager.AClientMinimalManager;
import com.alessandro.astages.api.manager.registry.AClientMinimalRegistry;
import com.alessandro.astages.api.restriction.AClientRestriction;
import net.minecraft.world.entity.EntityType;

public abstract class EntityTypeClientCache<R extends AClientRestriction<R, ?, ?>> extends AKeyedClientRestrictionCache<R, EntityType<?>> {
    public EntityTypeClientCache(AClientMinimalRegistry<?> registry) {
        super(registry);
    }

    public EntityTypeClientCache(AClientMinimalManager<?, ?> manager) {
        super(manager);
    }
}