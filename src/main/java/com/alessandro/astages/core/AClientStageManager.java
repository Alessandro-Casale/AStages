package com.alessandro.astages.core;

import com.alessandro.astages.core.stage.client.AClientGenericManager;
import com.alessandro.astages.plugin.ForPlugins;
import com.alessandro.astages.store.AttributeStore;

import java.util.HashMap;
import java.util.Map;

public class AClientStageManager {
    @ForPlugins public static final Map<Class<?>, AttributeStore> ATTACHED_ATTRIBUTES = new HashMap<>();

    public static final AClientGenericManager GENERIC_INSTANCE = new AClientGenericManager();

    public static void reloadBeforeScripts() {
        GENERIC_INSTANCE.reloadBeforeScripts();
    }
}
