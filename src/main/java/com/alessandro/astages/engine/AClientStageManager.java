package com.alessandro.astages.engine;

import com.alessandro.astages.api.plugin.ForPlugins;
import com.alessandro.astages.api.store.container.AttributeStore;
import com.alessandro.astages.engine.client.stage.AClientGenericManager;
import com.alessandro.astages.engine.client.stage.AClientPermanentManager;
import com.alessandro.astages.engine.client.stage.AClientTemporaryManager;

import java.util.HashMap;
import java.util.Map;

public class AClientStageManager {
    @ForPlugins
    public static final Map<Class<?>, AttributeStore> ATTACHED_ATTRIBUTES = new HashMap<>();

    public static final AClientGenericManager GENERIC_INSTANCE = new AClientGenericManager();
    public static final AClientPermanentManager PERMANENT_INSTANCE = new AClientPermanentManager();
    public static final AClientTemporaryManager TEMPORARY_INSTANCE = new AClientTemporaryManager();

    public static void onReloadStarted() {
        GENERIC_INSTANCE.onReloadStarted();
        PERMANENT_INSTANCE.onReloadStarted();
        TEMPORARY_INSTANCE.onReloadStarted();
    }

    public static void onReloadFinished() {
        GENERIC_INSTANCE.onReloadFinished();
        PERMANENT_INSTANCE.onReloadFinished();
        TEMPORARY_INSTANCE.onReloadFinished();
    }
}
