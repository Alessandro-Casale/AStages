package com.alessandro.astages.integration.kubejs.util;

import com.alessandro.astages.event.custom.InitialStageAdditionEvent;
import com.alessandro.astages.event.custom.StageAddedPlayerEvent;
import com.alessandro.astages.event.custom.StageRemovedPlayerEvent;
import com.alessandro.astages.integration.kubejs.event.StageAddedEventJS;
import com.alessandro.astages.integration.kubejs.event.StageManagerEventJS;
import com.alessandro.astages.integration.kubejs.event.StageRemovedEventJS;
import dev.latvian.mods.kubejs.bindings.event.StartupEvents;
import dev.latvian.mods.kubejs.event.StartupEventJS;
import dev.latvian.mods.kubejs.script.ScriptTypeHolder;
import net.minecraftforge.common.MinecraftForge;

public class KubeJSStageEventHandler {
    public static void init() {
        MinecraftForge.EVENT_BUS.addListener(KubeJSStageEventHandler::stageAdded);
        MinecraftForge.EVENT_BUS.addListener(KubeJSStageEventHandler::stageRemoved);
        MinecraftForge.EVENT_BUS.addListener(KubeJSStageEventHandler::stageManager);
    }
//
//    public static void serverLoaded(StartupEvents)

    public static void stageManager(InitialStageAdditionEvent event) {
        if (StageEvents.STAGE_MANAGER.hasListeners()) {
            StageEvents.STAGE_MANAGER.post(new StageManagerEventJS(event));
        }
    }

    public static void stageAdded(StageAddedPlayerEvent event) {
        if (StageEvents.STAGE_ADDED.hasListeners()) {
            StageEvents.STAGE_ADDED.post((ScriptTypeHolder) event.getEntity(), event.stage, new StageAddedEventJS(event));
        }
    }

    public static void stageRemoved(StageRemovedPlayerEvent event) {
        if (StageEvents.STAGE_REMOVED.hasListeners()) {
            StageEvents.STAGE_REMOVED.post((ScriptTypeHolder) event.getEntity(), event.stage, new StageRemovedEventJS(event));
        }
    }
}
