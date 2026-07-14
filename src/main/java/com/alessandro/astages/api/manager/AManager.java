package com.alessandro.astages.api.manager;

import com.alessandro.astages.api.cache.ARestrictionCache;
import com.alessandro.astages.api.holder.AHolder;
import com.alessandro.astages.api.manager.evaluator.AEvaluator;
import com.alessandro.astages.api.manager.registry.ARegistry;
import com.alessandro.astages.api.nullability.NotNullParams;
import com.alessandro.astages.api.restriction.ARestriction;
import com.alessandro.astages.api.store.ARestrictionType;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Base class for almost all Managers related to AStages!
 *
 * @param <R> The restriction associated to this manager
 * @param <U> For restrict method object type
 * @param <V> For isRestricted method object type
 */
@NotNullParams
public abstract class AManager<R extends ARestriction<R, U, V>, U, V> implements AMinimalManager<R, V> {
    private final ARegistry<R> registry = new  ARegistry<>();
    private final AEvaluator<R, V> evaluator = new AEvaluator<>(registry);

    private final List<ARestrictionCache<R, ?>> caches = new ArrayList<>();

    @Override
    public R getRestriction(String id) {
        return registry.getById(id);
    }

    @Override
    public R getRestriction(AHolder holder, V object) {
        return evaluator.evaluate(holder, object);
    }

    public void addRestriction(R restriction) {
        registry.register(restriction, considerGlobalStages());

        for (var cache : caches) {
            cache.index(restriction);
        }
    }

    @Override
    public void removeRestriction(String id) {
        var restriction = registry.remove(id);

        for (var cache : caches) {
            cache.remove(restriction);
        }
    }

    @Override
    public void onReloadStarted() {
        registry.clear();

        for (var cache : caches) {
            cache.clear();
        }
    }

    @Override
    public void onReloadFinished() { }

    @Override
    public ARegistry<R> getRegistry() {
        return registry;
    }

    @SafeVarargs
    public final void registerCaches(ARestrictionCache<R, ?>... caches) {
        this.caches.addAll(Arrays.asList(caches));
    }

    public boolean considerGlobalStages() {
        return true;
    }

    @Override
    public ARestrictionType associatedType() {
        return null;
    }
}