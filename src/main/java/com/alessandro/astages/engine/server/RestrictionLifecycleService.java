package com.alessandro.astages.engine.server;

import com.alessandro.astages.api.constant.ASyncOperation;
import com.alessandro.astages.api.constant.ReloadType;
import com.alessandro.astages.api.manager.AMinimalManager;
import com.alessandro.astages.infrastructure.hook.CommonEventSettings;
import com.alessandro.astages.infrastructure.networking.Networking;
import com.alessandro.astages.infrastructure.networking.packet.reload.RequestReloadS2C;
import net.neoforged.neoforge.server.ServerLifecycleHooks;

public class RestrictionLifecycleService {
    public static void reloadBeforeScripts() {
        if (ServerLifecycleHooks.getCurrentServer() == null) { return; }

        RestrictionRegistry.getRegisteredManagers()
            .forEach(AMinimalManager::reloadBeforeScripts);

        MiscStorage.clearAll();

        Networking.sendToAllPlayers(new RequestReloadS2C(ReloadType.CLIENT_BEFORE));
    }

    public static void reloadAfterScripts() {
        RestrictionRegistry.getRegisteredManagers()
            .forEach(AMinimalManager::reloadAfterScripts);

        if (ServerLifecycleHooks.getCurrentServer() == null) { return; }

        RestrictionSyncService.clientSynchronization(null);
        RestrictionSyncService.reflectSimpleIdsChangesToClients(null, MiscStorage.SIMPLE_IDS, ASyncOperation.ADD);
        RestrictionSyncService.reflectAllStagesChangesToClients(null);

        CommonEventSettings.allInventoryChanged();
    }
}