package com.alessandro.astages.api.cache;

import com.alessandro.astages.api.base.IndexedOrderedMultiMap;
import com.alessandro.astages.api.holder.AClientHolder;
import com.alessandro.astages.api.manager.AClientMinimalManager;
import com.alessandro.astages.api.manager.registry.AClientMinimalRegistry;
import com.alessandro.astages.api.nullability.NotNullParams;
import com.alessandro.astages.api.restriction.AClientRestriction;
import com.alessandro.astages.api.util.AClientEvaluatorUtils;

import java.util.SortedSet;

@NotNullParams
public abstract class AKeyedClientRestrictionCache<R extends AClientRestriction<R, ?, ?>, T> implements AClientRestrictionCache<R, T> {
    private final AClientMinimalRegistry<?> registry;
    private final IndexedOrderedMultiMap<T, R> cache = IndexedOrderedMultiMap.create();

    public AKeyedClientRestrictionCache(AClientMinimalRegistry<?> registry) {
        this.registry = registry;
    }

    public AKeyedClientRestrictionCache(AClientMinimalManager<?, ?> manager) {
        this.registry = manager.getRegistry();
    }

    @Override
    public void add(T target, R restriction) {
        cache.put(target, restriction);
    }

    @Override
    public void remove(R restriction) {
        cache.removeValue(restriction);
    }

    @Override
    public SortedSet<R> get(T target) {
        return cache.get(target);
    }

    @Override
    public R find(AClientHolder holder, T target) {
        return AClientEvaluatorUtils.evaluateRestrictionFromCache(cache, AClientEvaluatorUtils.calculateEvaluationMode(registry), holder, target);
    }

    @Override
    public void clear() {
        cache.clear();
    }
}
