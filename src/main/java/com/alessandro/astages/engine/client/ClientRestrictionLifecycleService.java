package com.alessandro.astages.engine.client;

import com.alessandro.astages.api.manager.AClientMinimalManager;

public class ClientRestrictionLifecycleService {
    public static void onReloadStarted() {
        ClientRestrictionReloadState.buildingRestrictions();

        ClientRestrictionRegistry.getRegisteredManagers()
            .forEach(AClientMinimalManager::onReloadStarted);

        ClientMiscStorage.clearAll();
    }

    public static void onReloadFinished() {
        ClientRestrictionReloadState.restrictionsAvailable();

        ClientRestrictionRegistry.getRegisteredManagers()
            .forEach(AClientMinimalManager::onReloadFinished);
    }
}