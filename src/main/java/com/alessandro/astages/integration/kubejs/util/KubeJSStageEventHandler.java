package com.alessandro.astages.integration.kubejs.util;

import com.alessandro.astages.api.ALoader;
import com.alessandro.astages.api.event.player.StageAddedPlayerEvent;
import com.alessandro.astages.api.event.player.StageRemovedPlayerEvent;
import com.alessandro.astages.integration.kubejs.event.StageAddedEventJS;
import com.alessandro.astages.integration.kubejs.event.StageRemovedEventJS;
import dev.latvian.mods.kubejs.script.ScriptTypeHolder;

public class KubeJSStageEventHandler {
    public static void init() {
        ALoader.EVENT_BUS.addListener(KubeJSStageEventHandler::stageAdded);
        ALoader.EVENT_BUS.addListener(KubeJSStageEventHandler::stageRemoved);
    }

    public static void stageAdded(StageAddedPlayerEvent event) {
        if (StageEvents.STAGE_ADDED.hasListeners()) {
            StageEvents.STAGE_ADDED.post((ScriptTypeHolder) event.getPlayer(), new StageAddedEventJS(event));
        }
    }

    public static void stageRemoved(StageRemovedPlayerEvent event) {
        if (StageEvents.STAGE_REMOVED.hasListeners()) {
            StageEvents.STAGE_REMOVED.post((ScriptTypeHolder) event.getPlayer(), new StageRemovedEventJS(event));
        }
    }
}
