package com.alessandro.astages.engine.server;

import com.alessandro.astages.api.constant.ASyncOperation;
import com.alessandro.astages.api.manager.AMinimalManager;
import com.alessandro.astages.infrastructure.hook.CommonEventSettings;
import net.neoforged.neoforge.server.ServerLifecycleHooks;

public class RestrictionLifecycleService {
    public static void onReloadStarted() {
        if (ServerLifecycleHooks.getCurrentServer() == null) { return; }

        RestrictionRegistry.getRegisteredManagers()
            .forEach(AMinimalManager::onReloadStarted);

        MiscStorage.clearAll();
    }

    public static void onReloadFinished() {
        RestrictionRegistry.getRegisteredManagers()
            .forEach(AMinimalManager::onReloadFinished);

        if (ServerLifecycleHooks.getCurrentServer() == null) { return; }

        RestrictionSyncService.clientSynchronization(null);
        RestrictionSyncService.reflectSimpleIdsChangesToClients(null, MiscStorage.SIMPLE_IDS, ASyncOperation.ADD);
        RestrictionSyncService.reflectAllStagesChangesToClients(null);

        CommonEventSettings.allInventoryChanged();
    }
}