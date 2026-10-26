package com.alessandro.astages.api.cache.server;

import com.alessandro.astages.api.cache.AKeyedRestrictionCache;
import com.alessandro.astages.api.manager.AMinimalManager;
import com.alessandro.astages.api.manager.registry.AMinimalRegistry;
import com.alessandro.astages.api.nullability.NotNullParams;
import com.alessandro.astages.api.restriction.ARestriction;
import net.minecraft.world.level.block.Block;

@NotNullParams
public abstract class BlockCache<R extends ARestriction<R, ?, ?>> extends AKeyedRestrictionCache<R, Block> {
    public BlockCache(AMinimalRegistry<?> registry) {
        super(registry);
    }

    public BlockCache(AMinimalManager<?, ?> manager) {
        super(manager);
    }
}