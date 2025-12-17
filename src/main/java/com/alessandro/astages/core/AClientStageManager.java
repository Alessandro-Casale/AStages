package com.alessandro.astages.core;

import com.alessandro.astages.core.stage.client.AClientGenericManager;

public class AClientStageManager {
    public static final AClientGenericManager GENERIC_INSTANCE = new AClientGenericManager();

    public static void reloadBeforeScripts() {
        GENERIC_INSTANCE.reloadBeforeScripts();
    }
}
