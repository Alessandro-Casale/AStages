package com.alessandro.astages.api.cache.server;

import com.alessandro.astages.api.base.IndexedOrderedMultiMap;
import com.alessandro.astages.api.holder.AHolder;
import com.alessandro.astages.api.restriction.ARestriction;
import com.alessandro.astages.api.cache.ARestrictionCache;
import com.alessandro.astages.api.util.ARestrictionUtils;
import net.minecraft.world.level.block.Block;

import java.util.SortedSet;

public abstract class BlockCache<R extends ARestriction<R, ?, ?>> implements ARestrictionCache<R, Block> {
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
    public R find(AHolder holder, Block target) {
        return ARestrictionUtils.getRestrictionFromCache(holder, cache, target);
    }

    @Override
    public void clear() {
        cache.clear();
    }
}