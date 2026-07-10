package com.alessandro.astages.internal.experimental.reload;

import java.util.HashSet;
import java.util.Set;

public class ReloadRegistry {
    private static final Set<AReloadable> RELOADABLE_INSTANCES = new HashSet<>();

    public static Set<AReloadable> getRegisteredReloadableInstances() {
        return RELOADABLE_INSTANCES;
    }

    public static void register(AReloadable instance) {
        RELOADABLE_INSTANCES.add(instance);
    }
}
