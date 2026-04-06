package com.alessandro.astages.infrastructure.integration.kubejs.bridge;

import com.alessandro.astages.api.ALoader;
import com.alessandro.astages.api.event.player.StageAddedPlayerEvent;
import com.alessandro.astages.api.event.player.StageRemovedPlayerEvent;
import com.alessandro.astages.infrastructure.integration.kubejs.event.KubeJSStageAddedEvent;
import com.alessandro.astages.infrastructure.integration.kubejs.event.KubeJSStageRemovedEvent;
import dev.latvian.mods.kubejs.script.ScriptTypeHolder;

public class KubeJSEventBridge {
    public static void init() {
        ALoader.EVENT_BUS.addListener(KubeJSEventBridge::stageAdded);
        ALoader.EVENT_BUS.addListener(KubeJSEventBridge::stageRemoved);
    }

    public static void stageAdded(StageAddedPlayerEvent event) {
        if (KubeJSStageEvents.STAGE_ADDED.hasListeners()) {
            KubeJSStageEvents.STAGE_ADDED.post((ScriptTypeHolder) event.getPlayer(), new KubeJSStageAddedEvent(event));
        }
    }

    public static void stageRemoved(StageRemovedPlayerEvent event) {
        if (KubeJSStageEvents.STAGE_REMOVED.hasListeners()) {
            KubeJSStageEvents.STAGE_REMOVED.post((ScriptTypeHolder) event.getPlayer(), new KubeJSStageRemovedEvent(event));
        }
    }
}