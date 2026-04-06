package com.alessandro.astages.api.cache.client;

import com.alessandro.astages.api.base.IndexedOrderedMultiMap;
import com.alessandro.astages.api.cache.AClientRestrictionCache;
import com.alessandro.astages.api.holder.AClientHolder;
import com.alessandro.astages.api.restriction.AClientRestriction;
import com.alessandro.astages.api.util.AClientRestrictionUtils;
import net.minecraft.world.level.block.Block;

import java.util.SortedSet;

public abstract class BlockClientCache<R extends AClientRestriction<R, ?, ?>> implements AClientRestrictionCache<R, Block> {
    private final IndexedOrderedMultiMap<Block, R> cache = IndexedOrderedMultiMap.create();

    @Override
    public void add(Block target, R restriction) {
        cache.put(target, restriction);
    }

    @Override
    public void remove(R restriction) {
        cache.removeValue(restriction);
    }

    @Override
    public SortedSet<R> get(Block target) {
        return cache.get(target);
    }

    @Override
    public R find(AClientHolder holder, Block target) {
        return AClientRestrictionUtils.getRestrictionFromCache(holder, cache, target);
    }

    @Override
    public void clear() {
        cache.clear();
    }
}