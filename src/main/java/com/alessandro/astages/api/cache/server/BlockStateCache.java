package com.alessandro.astages.api.cache.server;

import com.alessandro.astages.api.cache.AKeyedRestrictionCache;
import com.alessandro.astages.api.manager.AMinimalManager;
import com.alessandro.astages.api.manager.registry.AMinimalRegistry;
import com.alessandro.astages.api.nullability.NotNullParams;
import com.alessandro.astages.api.restriction.ARestriction;
import net.minecraft.world.level.block.state.BlockState;

@NotNullParams
public abstract class BlockStateCache<R extends ARestriction<R, ?, ?>> extends AKeyedRestrictionCache<R, BlockState> {
    public BlockStateCache(AMinimalRegistry<?> registry) {
        super(registry);
    }

    public BlockStateCache(AMinimalManager<?, ?> manager) {
        super(manager);
    }
}