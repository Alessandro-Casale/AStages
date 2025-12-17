package com.alessandro.astages.integration.kubejs.util;

import com.alessandro.astages.integration.kubejs.event.StageAddedEventJS;
import com.alessandro.astages.integration.kubejs.event.StageRemovedEventJS;
import dev.latvian.mods.kubejs.event.EventGroup;
import dev.latvian.mods.kubejs.event.EventHandler;
import dev.latvian.mods.kubejs.event.EventTargetType;

public class StageEvents {
    public static final EventGroup GROUP = EventGroup.of("AStageEvents");
    private static final EventTargetType<String> STAGE = EventTargetType.STRING;

    public static final EventHandler STAGE_ADDED = GROUP.server("added", () -> StageAddedEventJS.class).supportsTarget(STAGE);
    public static final EventHandler STAGE_REMOVED = GROUP.server("removed", () -> StageRemovedEventJS.class).supportsTarget(STAGE);
}
