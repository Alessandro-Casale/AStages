package com.alessandro.astages.engine;

import com.alessandro.astages.engine.client.model.AClientRegisteredModels;

public class AClientModelManager {
    public static final AClientRegisteredModels MODELS = new AClientRegisteredModels();

    public static void onReloadStarted() {
        MODELS.onReloadStarted();
    }

    public static void onReloadFinished() {
        MODELS.onReloadFinished();
    }
}