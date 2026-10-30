package com.alessandro.astages.api.cache;

import com.alessandro.astages.api.manager.ACommonManager;
import com.alessandro.astages.api.manager.registry.ACommonRegistry;
import com.alessandro.astages.api.nullability.NotNullParams;
import com.alessandro.astages.api.restriction.ACommonRestriction;
import net.minecraft.world.level.block.Block;

@NotNullParams
public abstract class BlockCache<R extends ACommonRestriction<?> & Comparable<R>> extends AKeyedRestrictionCache<R, Block> {
    public BlockCache(ACommonRegistry<?> registry) {
        super(registry);
    }

    public BlockCache(ACommonManager<?, ?> manager) {
        super(manager);
    }
}