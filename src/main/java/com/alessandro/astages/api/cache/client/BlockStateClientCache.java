package com.alessandro.astages.api.cache.client;

import com.alessandro.astages.api.base.IndexedOrderedMultiMap;
import com.alessandro.astages.api.cache.AClientRestrictionCache;
import com.alessandro.astages.api.holder.AClientHolder;
import com.alessandro.astages.api.restriction.AClientRestriction;
import com.alessandro.astages.api.util.AClientRestrictionUtils;
import net.minecraft.world.level.block.state.BlockState;

import java.util.SortedSet;

public abstract class BlockStateClientCache<R extends AClientRestriction<R, ?, ?>> implements AClientRestrictionCache<R, BlockState> {
    private final IndexedOrderedMultiMap<BlockState, R> cache = IndexedOrderedMultiMap.create();

    @Override
    public void add(BlockState target, R restriction) {
        cache.put(target, restriction);
    }

    @Override
    public void remove(R restriction) {
        cache.removeValue(restriction);
    }

    @Override
    public SortedSet<R> get(BlockState target) {
        return cache.get(target);
    }

    @Override
    public R find(AClientHolder holder, BlockState target) {
        return AClientRestrictionUtils.getRestrictionFromCache(holder, cache, target);
    }

    @Override
    public void clear() {
        cache.clear();
    }
}