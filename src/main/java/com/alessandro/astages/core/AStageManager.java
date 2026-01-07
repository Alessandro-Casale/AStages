package com.alessandro.astages.core;

import com.alessandro.astages.api.nullability.Nullable;
import com.alessandro.astages.core.stage.manager.AGenericManager;
import com.alessandro.astages.core.stage.manager.APermanentManager;
import com.alessandro.astages.core.stage.manager.ATemporaryManager;
import net.minecraft.server.level.ServerPlayer;
import net.neoforged.neoforge.server.ServerLifecycleHooks;

public class AStageManager {
    public static final AGenericManager GENERIC_INSTANCE = new AGenericManager();
    public static final APermanentManager PERMANENT_INSTANCE = new APermanentManager();
    public static final ATemporaryManager TEMPORARY_INSTANCE = new ATemporaryManager();

    public static void reloadBeforeScripts() {
         GENERIC_INSTANCE.reloadBeforeScripts();
         PERMANENT_INSTANCE.reloadBeforeScripts();
         TEMPORARY_INSTANCE.reloadBeforeScripts();
    }

    public static void reloadAfterScripts() {
        GENERIC_INSTANCE.reloadAfterScripts();

        if (ServerLifecycleHooks.getCurrentServer() == null) { return; }
        clientSynchronization(null);
    }

    public static void clientSynchronization(@Nullable ServerPlayer player) {
        GENERIC_INSTANCE.synchronizeWithClient(player);
    }
}
