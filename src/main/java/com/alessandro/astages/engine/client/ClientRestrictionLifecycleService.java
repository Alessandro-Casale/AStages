package com.alessandro.astages.engine.client;

import com.alessandro.astages.api.manager.AClientMinimalManager;

public class ClientRestrictionLifecycleService {
    public static void reloadBeforeScripts() {
        ClientRestrictionReloadState.areScriptsAvailable(false);

        ClientRestrictionRegistry.getRegisteredManagers()
            .forEach(AClientMinimalManager::reloadBeforeScripts);

        ClientMiscStorage.clearAll();
    }

    public static void reloadAfterScripts() {
        ClientRestrictionReloadState.areScriptsAvailable(true);
    }
}