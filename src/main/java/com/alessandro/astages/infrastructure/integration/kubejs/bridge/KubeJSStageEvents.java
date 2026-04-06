package com.alessandro.astages.infrastructure.integration.kubejs.bridge;

import com.alessandro.astages.infrastructure.integration.kubejs.event.KubeJSStageAddedEvent;
import com.alessandro.astages.infrastructure.integration.kubejs.event.KubeJSStageRemovedEvent;
import dev.latvian.mods.kubejs.event.EventGroup;
import dev.latvian.mods.kubejs.event.EventHandler;
import dev.latvian.mods.kubejs.event.EventTargetType;

public class KubeJSStageEvents {
    public static final EventGroup GROUP = EventGroup.of("AStageEvents");
    private static final EventTargetType<String> STAGE = EventTargetType.STRING;

    public static final EventHandler STAGE_ADDED = GROUP.server("added", () -> KubeJSStageAddedEvent.class).supportsTarget(STAGE);
    public static final EventHandler STAGE_REMOVED = GROUP.server("removed", () -> KubeJSStageRemovedEvent.class).supportsTarget(STAGE);
}