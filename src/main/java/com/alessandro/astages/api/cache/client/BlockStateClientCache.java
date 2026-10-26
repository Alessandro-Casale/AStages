package com.alessandro.astages.api.cache.client;

import com.alessandro.astages.api.cache.AKeyedClientRestrictionCache;
import com.alessandro.astages.api.manager.AClientMinimalManager;
import com.alessandro.astages.api.manager.registry.AClientMinimalRegistry;
import com.alessandro.astages.api.restriction.AClientRestriction;
import net.minecraft.world.level.block.state.BlockState;

public abstract class BlockStateClientCache<R extends AClientRestriction<R, ?, ?>> extends AKeyedClientRestrictionCache<R, BlockState> {
    public BlockStateClientCache(AClientMinimalRegistry<?> registry) {
        super(registry);
    }

    public BlockStateClientCache(AClientMinimalManager<?, ?> manager) {
        super(manager);
    }
}