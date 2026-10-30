package com.alessandro.astages.api.cache;

import com.alessandro.astages.api.base.IndexedOrderedMultiMap;
import com.alessandro.astages.api.manager.ACommonManager;
import com.alessandro.astages.api.manager.registry.ACommonRegistry;
import com.alessandro.astages.api.nullability.NotNullParams;
import com.alessandro.astages.api.restriction.ACommonRestriction;
import com.alessandro.astages.api.stage.AStageContext;
import com.alessandro.astages.api.util.AEvaluatorUtils;

import java.util.SortedSet;
import java.util.function.Supplier;

@NotNullParams
public abstract class AKeyedRestrictionCache<R extends ACommonRestriction<?> & Comparable<R>, T> implements ARestrictionCache<R, T> {
    private final Supplier<ACommonRegistry<?>> registry;
    private final IndexedOrderedMultiMap<T, R> cache = IndexedOrderedMultiMap.create();

    public AKeyedRestrictionCache(ACommonRegistry<?> registry) {
        this.registry = () -> registry;
    }

    public AKeyedRestrictionCache(ACommonManager<?, ?> manager) {
        this.registry = manager::getRegistry;
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
    public R find(AStageContext context, T target) {
        return AEvaluatorUtils.evaluateRestrictionFromCache(cache, AEvaluatorUtils.calculateEvaluationMode(registry.get()), context, target);
    }

    @Override
    public void clear() {
        cache.clear();
    }
}
