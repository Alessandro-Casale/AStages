package com.alessandro.astages.api.cache.server;

import com.alessandro.astages.api.base.IndexedOrderedMultiMap;
import com.alessandro.astages.api.cache.ARestrictionCache;
import com.alessandro.astages.api.holder.AHolder;
import com.alessandro.astages.api.restriction.ARestriction;
import com.alessandro.astages.api.util.ARestrictionUtils;
import net.minecraft.world.entity.EntityType;

import java.util.SortedSet;

public abstract class EntityTypeCache<R extends ARestriction<R, ?, ?>> implements ARestrictionCache<R, EntityType<?>> {
    private final IndexedOrderedMultiMap<EntityType<?>, R> cache = IndexedOrderedMultiMap.create();

    @Override
    public void add(EntityType<?> entityType, R restriction) {
        cache.put(entityType, restriction);
    }

    @Override
    public void remove(R restriction) {
        cache.removeValue(restriction);
    }

    @Override
    public SortedSet<R> get(EntityType<?> target) {
        return cache.get(target);
    }

    @Override
    public R find(AHolder holder, EntityType<?> target) {
        return ARestrictionUtils.getRestrictionFromCache(holder, cache, target);
    }

    @Override
    public void clear() {
        cache.clear();
    }
}