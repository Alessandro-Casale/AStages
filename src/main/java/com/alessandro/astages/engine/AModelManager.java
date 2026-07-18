package com.alessandro.astages.engine;

import com.alessandro.astages.engine.server.model.ARegisteredModels;

public class AModelManager {
    public static final ARegisteredModels MODELS = new ARegisteredModels();

    public static void onReloadStarted() {
        MODELS.onReloadStarted();
    }

    public static void onReloadFinished() {
        MODELS.onReloadFinished();
    }
}