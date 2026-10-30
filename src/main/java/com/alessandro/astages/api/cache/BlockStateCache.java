package com.alessandro.astages.api.cache;

import com.alessandro.astages.api.manager.ACommonManager;
import com.alessandro.astages.api.manager.registry.ACommonRegistry;
import com.alessandro.astages.api.nullability.NotNullParams;
import com.alessandro.astages.api.restriction.ACommonRestriction;
import net.minecraft.world.level.block.state.BlockState;

@NotNullParams
public abstract class BlockStateCache<R extends ACommonRestriction<?> & Comparable<R>> extends AKeyedRestrictionCache<R, BlockState> {
    public BlockStateCache(ACommonRegistry<?> registry) {
        super(registry);
    }

    public BlockStateCache(ACommonManager<?, ?> manager) {
        super(manager);
    }
}