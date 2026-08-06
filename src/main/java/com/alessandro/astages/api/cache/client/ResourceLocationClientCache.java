package com.alessandro.astages.api.cache.client;

import com.alessandro.astages.api.base.IndexedOrderedMultiMap;
import com.alessandro.astages.api.cache.AClientRestrictionCache;
import com.alessandro.astages.api.holder.AClientHolder;
import com.alessandro.astages.api.nullability.NotNullParams;
import com.alessandro.astages.api.restriction.AClientRestriction;
import com.alessandro.astages.api.util.AClientRestrictionUtils;
import net.minecraft.resources.ResourceLocation;

import java.util.SortedSet;

@NotNullParams
public abstract class ResourceLocationClientCache<R extends AClientRestriction<R, ?, ?>> implements AClientRestrictionCache<R, ResourceLocation> {
    private final IndexedOrderedMultiMap<ResourceLocation, R> cache = IndexedOrderedMultiMap.create();

    @Override
    public void add(ResourceLocation target, R restriction) {
        cache.put(target, restriction);
    }

    @Override
    public void remove(R restriction) {
        cache.removeValue(restriction);
    }

    @Override
    public SortedSet<R> get(ResourceLocation target) {
        return cache.get(target);
    }

    @Override
    public R find(AClientHolder holder, ResourceLocation target) {
        return AClientRestrictionUtils.getRestrictionFromCache(holder, cache, target);
    }

    @Override
    public void clear() {
        cache.clear();
    }
}