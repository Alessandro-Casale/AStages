package com.alessandro.astages.engine.client;

import com.alessandro.astages.api.develop.Info;
import com.alessandro.astages.api.manager.AClientMinimalManager;
import com.alessandro.astages.api.nullability.NotNullParamsAndMethodsReturn;
import com.alessandro.astages.api.nullability.Nullable;
import com.alessandro.astages.api.store.ARestrictionType;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@NotNullParamsAndMethodsReturn
public class ClientRestrictionRegistry {
    @Info("Both Internal and Plugin!")
    public static final Map<ARestrictionType, AClientMinimalManager<?, ?>> MANAGERS = new HashMap<>();

    public static @Nullable AClientMinimalManager<?, ?> getManager(ARestrictionType type) {
        return MANAGERS.getOrDefault(type, null);
    }

    public static Collection<AClientMinimalManager<?, ?>> getRegisteredManagers() {
        return MANAGERS.values();
    }

    public static void register(ARestrictionType type, AClientMinimalManager<?, ?> manager) {
        MANAGERS.put(type, manager);
    }

    @SuppressWarnings("unchecked")
    public static <T> @Nullable T getRestriction(String id, ARestrictionType type) {
        AClientMinimalManager<?, ?> manager = getManager(type);
        return manager != null ? (T) manager.getRestriction(id) : null;
    }

    public static void removeRestriction(String id, ARestrictionType type) {
        AClientMinimalManager<?, ?> manager = MANAGERS.get(type);
        if (manager != null) manager.removeRestriction(id);
    }
}