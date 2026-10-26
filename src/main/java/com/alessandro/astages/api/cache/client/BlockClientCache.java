package com.alessandro.astages.api.cache.client;

import com.alessandro.astages.api.cache.AKeyedClientRestrictionCache;
import com.alessandro.astages.api.manager.AClientMinimalManager;
import com.alessandro.astages.api.manager.registry.AClientMinimalRegistry;
import com.alessandro.astages.api.restriction.AClientRestriction;
import net.minecraft.world.level.block.Block;

public abstract class BlockClientCache<R extends AClientRestriction<R, ?, ?>> extends AKeyedClientRestrictionCache<R, Block> {
    public BlockClientCache(AClientMinimalManager<?, ?> manager) {
        super(manager);
    }

    public BlockClientCache(AClientMinimalRegistry<?> registry) {
        super(registry);
    }
}