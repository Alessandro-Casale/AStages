package com.alessandro.astages.api.cache.server;

import com.alessandro.astages.api.base.IndexedOrderedMultiMap;
import com.alessandro.astages.api.cache.ARestrictionCache;
import com.alessandro.astages.api.holder.AHolder;
import com.alessandro.astages.api.nullability.NotNullParams;
import com.alessandro.astages.api.restriction.ARestriction;
import com.alessandro.astages.api.util.ARestrictionUtils;
import net.minecraft.resources.ResourceLocation;

import java.util.SortedSet;

@NotNullParams
public abstract class ResourceLocationCache<R extends ARestriction<R, ?, ?>> implements ARestrictionCache<R, ResourceLocation> {
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
    public R find(AHolder holder, ResourceLocation target) {
        return ARestrictionUtils.getRestrictionFromCache(holder, cache, target);
    }

    @Override
    public void clear() {
        cache.clear();
    }
}