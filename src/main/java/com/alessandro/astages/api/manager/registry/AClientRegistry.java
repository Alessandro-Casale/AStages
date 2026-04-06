package com.alessandro.astages.api.manager.registry;

import com.alessandro.astages.api.nullability.NotNullParamsAndMethodsReturn;
import com.alessandro.astages.api.nullability.Nullable;
import com.alessandro.astages.api.restriction.AClientRestriction;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

@NotNullParamsAndMethodsReturn
public class AClientRegistry<R extends AClientRestriction<?, ?, ?>> implements AClientMinimalRegistry<R> {
    private final Set<R> restrictions = new HashSet<>();
    private final Map<String, R> restrictionsById = new HashMap<>();
    private final Map<String, Set<R>> restrictionsByStage = new HashMap<>();

    @Override
    public Set<R> getRestrictions() {
        return restrictions;
    }

    @Override
    public @Nullable R getById(String id) {
        return restrictionsById.getOrDefault(id, null);
    }

    public void register(R restriction) {
        restrictions.add(restriction);
        restrictionsById.put(restriction.getId(), restriction);

        restrictionsByStage
            .computeIfAbsent(restriction.getStage(), k -> new HashSet<>())
            .add(restriction);
    }

    @Override
    public @Nullable R remove(String id) {
        var restriction =  getById(id);

        if (restriction != null) {
            restrictionsById.remove(id);
            restrictions.remove(restriction);

            restrictionsByStage
                .get(restriction.getStage())
                .remove(restriction);

            if (restrictionsByStage.get(restriction.getStage()).isEmpty()) {
                restrictionsByStage.remove(restriction.getStage());
            }
        }

        return restriction;
    }

    @Override
    public void clear() {
        restrictions.clear();
        restrictionsById.clear();
        restrictionsByStage.clear();
    }

    @Override
    public Set<String> getIds() {
        return restrictionsById.keySet();
    }

    @Override
    public Set<String> getStages() {
        return restrictionsByStage.keySet();
    }
}