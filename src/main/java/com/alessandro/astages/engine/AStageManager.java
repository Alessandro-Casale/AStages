package com.alessandro.astages.engine;

import com.alessandro.astages.api.ALoader;
import com.alessandro.astages.api.constant.AEventPhase;
import com.alessandro.astages.api.event.AddStageEvent;
import com.alessandro.astages.api.nullability.Nullable;
import com.alessandro.astages.api.plugin.ForPlugins;
import com.alessandro.astages.api.store.container.AttributeStore;
import com.alessandro.astages.engine.server.stage.AGenericManager;
import com.alessandro.astages.engine.server.stage.APermanentManager;
import com.alessandro.astages.engine.server.stage.ATemporaryManager;
import net.minecraft.server.level.ServerPlayer;
import net.neoforged.neoforge.server.ServerLifecycleHooks;

import java.util.HashMap;
import java.util.Map;

public class AStageManager {
    @ForPlugins
    public static final Map<Class<?>, AttributeStore> ATTACHED_ATTRIBUTES = new HashMap<>();

    public static final AGenericManager GENERIC_INSTANCE = new AGenericManager();
    public static final APermanentManager PERMANENT_INSTANCE = new APermanentManager();
    public static final ATemporaryManager TEMPORARY_INSTANCE = new ATemporaryManager();

    public static void onReloadStarted() {
         GENERIC_INSTANCE.onReloadStarted();
         PERMANENT_INSTANCE.onReloadStarted();
         TEMPORARY_INSTANCE.onReloadStarted();
    }

    public static void onReloadFinished() {
        GENERIC_INSTANCE.onReloadFinished();
        PERMANENT_INSTANCE.onReloadFinished();
        TEMPORARY_INSTANCE.onReloadFinished();

        if (ServerLifecycleHooks.getCurrentServer() == null) { return; }
        clientSynchronization(null);
    }

    public static void clientSynchronization(@Nullable ServerPlayer player) {
        PERMANENT_INSTANCE.synchronizeWithClient(player);
        TEMPORARY_INSTANCE.synchronizeWithClient(player);
    }

    public static void addStagesViaJavaCode(AEventPhase stage) {
        ALoader.EVENT_BUS.post(new AddStageEvent(stage));
    }
}
