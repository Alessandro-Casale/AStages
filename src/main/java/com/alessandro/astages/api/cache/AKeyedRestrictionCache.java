package com.alessandro.astages.api.cache;

import com.alessandro.astages.api.base.IndexedOrderedMultiMap;
import com.alessandro.astages.api.holder.AHolder;
import com.alessandro.astages.api.manager.AMinimalManager;
import com.alessandro.astages.api.manager.registry.AMinimalRegistry;
import com.alessandro.astages.api.nullability.NotNullParams;
import com.alessandro.astages.api.restriction.ARestriction;
import com.alessandro.astages.api.util.AEvaluatorUtils;

import java.util.SortedSet;

@NotNullParams
public abstract class AKeyedRestrictionCache<R extends ARestriction<R, ?, ?>, T> implements ARestrictionCache<R, T> {
    private final AMinimalRegistry<?> registry;
    private final IndexedOrderedMultiMap<T, R> cache = IndexedOrderedMultiMap.create();

    public AKeyedRestrictionCache(AMinimalRegistry<?> registry) {
        this.registry = registry;
    }

    public AKeyedRestrictionCache(AMinimalManager<?, ?> manager) {
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
    public R find(AHolder holder, T target) {
        return AEvaluatorUtils.evaluateRestrictionFromCache(cache, AEvaluatorUtils.calculateEvaluationMode(registry), holder, target);
    }

    @Override
    public void clear() {
        cache.clear();
    }
}
