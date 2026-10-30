package com.alessandro.astages.api.manager;

import com.alessandro.astages.api.cache.ARestrictionCache;
import com.alessandro.astages.api.holder.AClientHolder;
import com.alessandro.astages.api.manager.evaluator.AClientEvaluator;
import com.alessandro.astages.api.manager.registry.AClientRegistry;
import com.alessandro.astages.api.nullability.NotNullParams;
import com.alessandro.astages.api.restriction.AClientRestriction;
import com.alessandro.astages.api.stage.AStageContext;
import com.alessandro.astages.api.store.ARestrictionType;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@NotNullParams
public abstract class AClientManager<R extends AClientRestriction<R, U, V>, U, V> implements AClientMinimalManager<R, V> {
    private final AClientRegistry<R> registry = new AClientRegistry<>();
    private final AClientEvaluator<R, V> evaluator = new AClientEvaluator<>(registry);

    private final List<ARestrictionCache<R, ?>> caches = new ArrayList<>();

    @Override
    public R getRestriction(String id) {
        return registry.getById(id);
    }

    @Override
    public R getRestriction(AStageContext context, V object) {
        return evaluator.evaluate(context, object);
    }

    public void addRestriction(R restriction) {
        registry.register(restriction);

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
    public AClientRegistry<R> getRegistry() {
        return registry;
    }

    @SafeVarargs
    public final void registerCaches(ARestrictionCache<R, ?>... caches) {
        this.caches.addAll(Arrays.asList(caches));
    }

    @Override
    public ARestrictionType associatedType() {
        return null;
    }
}