package com.alessandro.astages.core;

import com.alessandro.astages.api.ALoader;
import com.alessandro.astages.api.constant.AEventPhase;
import com.alessandro.astages.api.event.AddStageEvent;
import com.alessandro.astages.api.nullability.Nullable;
import com.alessandro.astages.core.stage.manager.AGenericManager;
import com.alessandro.astages.core.stage.manager.APermanentManager;
import com.alessandro.astages.core.stage.manager.ATemporaryManager;
import com.alessandro.astages.plugin.ForPlugins;
import com.alessandro.astages.store.AttributeStore;
import net.minecraft.server.level.ServerPlayer;
import net.neoforged.neoforge.server.ServerLifecycleHooks;

import java.util.HashMap;
import java.util.Map;

public class AStageManager {
    @ForPlugins public static final Map<Class<?>, AttributeStore> ATTACHED_ATTRIBUTES = new HashMap<>();

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

    public static void addStagesViaJavaCode(AEventPhase stage) {
        ALoader.EVENT_BUS.post(new AddStageEvent(stage));
    }
}
