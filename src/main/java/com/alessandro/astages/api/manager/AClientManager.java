package com.alessandro.astages.api.manager;

import com.alessandro.astages.api.cache.AClientRestrictionCache;
import com.alessandro.astages.api.holder.AClientHolder;
import com.alessandro.astages.api.manager.evaluator.AClientEvaluator;
import com.alessandro.astages.api.manager.registry.AClientRegistry;
import com.alessandro.astages.api.nullability.NotNullParams;
import com.alessandro.astages.api.restriction.AClientRestriction;
import com.alessandro.astages.api.store.ARestrictionType;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@NotNullParams
public abstract class AClientManager<R extends AClientRestriction<R, U, V>, U, V> implements AClientMinimalManager<R, V> {
    private final AClientRegistry<R> registry = new AClientRegistry<>();
    private final AClientEvaluator<R, V> evaluator = new AClientEvaluator<>(registry);

    private final List<AClientRestrictionCache<R, ?>> caches = new ArrayList<>();

    @Override
    public R getRestriction(String id) {
        return registry.getById(id);
    }

    @Override
    public R getRestriction(AClientHolder holder, V object) {
        return evaluator.evaluate(holder, object);
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
    public void reloadBeforeScripts() {
        registry.clear();

        for (var cache : caches) {
            cache.clear();
        }
    }

    @Override
    public void reloadAfterScripts() { }

    @Override
    public AClientRegistry<R> getRegistry() {
        return registry;
    }

    @SafeVarargs
    public final void registerCaches(AClientRestrictionCache<R, ?>... caches) {
        this.caches.addAll(Arrays.asList(caches));
    }

    @Override
    public ARestrictionType associatedType() {
        return null;
    }
}