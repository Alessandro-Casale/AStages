package com.alessandro.astages.engine.server;

import com.alessandro.astages.api.develop.Info;
import com.alessandro.astages.api.manager.AMinimalManager;
import com.alessandro.astages.api.nullability.NotNullParamsAndMethodsReturn;
import com.alessandro.astages.api.nullability.Nullable;
import com.alessandro.astages.api.store.ARestrictionType;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@NotNullParamsAndMethodsReturn
public class RestrictionRegistry {
    @Info("Both Internal and Plugin!")
    private static final Map<ARestrictionType, AMinimalManager<?, ?>> MANAGERS = new HashMap<>();

    public static @Nullable AMinimalManager<?, ?> getManager(ARestrictionType type) {
        return MANAGERS.getOrDefault(type, null);
    }

    public static Collection<AMinimalManager<?, ?>> getRegisteredManagers() {
        return MANAGERS.values();
    }

    public static void register(ARestrictionType type, AMinimalManager<?, ?> manager) {
        MANAGERS.put(type, manager);
    }

    @SuppressWarnings("unchecked")
    public static <T> @Nullable T getRestriction(String id, ARestrictionType type) {
        AMinimalManager<?, ?> manager = getManager(type);
        return manager != null ? (T) manager.getRestriction(id) : null;
    }

    public static void removeRestriction(String id, ARestrictionType type) {
        AMinimalManager<?, ?> manager = MANAGERS.get(type);
        if (manager != null) manager.removeRestriction(id);
    }
}