package com.alessandro.astages.integration.kubejs;

import com.alessandro.astages.AStages;
import com.alessandro.astages.integration.Mods;
import com.alessandro.astages.integration.kubejs.util.KubeJSStageEventHandler;
import com.alessandro.astages.integration.kubejs.util.StageEvents;
import dev.latvian.mods.kubejs.KubeJSPlugin;
import dev.latvian.mods.kubejs.event.EventGroup;
import dev.latvian.mods.kubejs.event.EventHandler;
import dev.latvian.mods.kubejs.script.BindingsEvent;
import org.jetbrains.annotations.NotNull;

public class AStageKubeJSPlugin extends KubeJSPlugin {
    static {
        if (Mods.KUBEJS.isLoaded()) {
            KubeJSStageEventHandler.init();
        }
    }

    @Override
    public void registerBindings(@NotNull BindingsEvent event) {
        if (!Mods.KUBEJS.isLoaded()) return;

        event.add("AStages", AStagesKubeJSUtil.class);
    }

    @Override
    public void registerEvents() {
        if (!Mods.KUBEJS.isLoaded()) return;

        StageEvents.GROUP.register();
    }

    @Override
    public void init() {
        if (!Mods.KUBEJS.isLoaded()) return;

        AStages.LOGGER.debug("KUBEJS: INITIALIZED PLUGIN!");
    }
}
