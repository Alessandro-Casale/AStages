package com.alessandro.astages.plugin.container;

import com.alessandro.astages.store.ARestrictionType;
import com.alessandro.astages.store.server.AMinimalManager;
import com.alessandro.astages.api.nullability.NotNullMethodsReturn;

import java.util.HashMap;
import java.util.Map;

@NotNullMethodsReturn
public class ManagerContainer {
    private final Map<ARestrictionType, AMinimalManager<?>> MANAGERS = new HashMap<>();

    public static ManagerContainer initialize() {
        return new ManagerContainer();
    }

    public void register(ARestrictionType type, AMinimalManager<?> manager) {
        MANAGERS.put(type, manager);
    }

    public Map<ARestrictionType, AMinimalManager<?>> get() {
        return MANAGERS;
    }
}
