package com.alessandro.astages.api.cache.client;

import com.alessandro.astages.api.base.IndexedOrderedMultiMap;
import com.alessandro.astages.api.cache.AClientRestrictionCache;
import com.alessandro.astages.api.holder.AClientHolder;
import com.alessandro.astages.api.restriction.AClientRestriction;
import com.alessandro.astages.api.util.AClientRestrictionUtils;

import java.util.SortedSet;

public abstract class StringClientCache <R extends AClientRestriction<R, ?, ?>> implements AClientRestrictionCache<R, String> {
    private final IndexedOrderedMultiMap<String, R> cache = IndexedOrderedMultiMap.create();

    @Override
    public void add(String target, R restriction) {
        cache.put(target, restriction);
    }

    @Override
    public void remove(R restriction) {
        cache.removeValue(restriction);
    }

    @Override
    public SortedSet<R> get(String target) {
        return cache.get(target);
    }

    @Override
    public R find(AClientHolder holder, String target) {
        return AClientRestrictionUtils.getRestrictionFromCache(holder, cache, target);
    }

    @Override
    public void clear() {
        cache.clear();
    }
}