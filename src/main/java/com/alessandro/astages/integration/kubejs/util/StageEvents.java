package com.alessandro.astages.integration.kubejs.util;

import com.alessandro.astages.integration.kubejs.event.StageAddedEventJS;
import com.alessandro.astages.integration.kubejs.event.StageManagerEventJS;
import com.alessandro.astages.integration.kubejs.event.StageRemovedEventJS;
import dev.latvian.mods.kubejs.event.EventGroup;
import dev.latvian.mods.kubejs.event.EventHandler;
import dev.latvian.mods.kubejs.event.Extra;

public class StageEvents {
    public static final EventGroup GROUP = EventGroup.of("AStageEvents");

    public static final EventHandler STAGE_ADDED = GROUP.server("added", () -> StageAddedEventJS.class).extra(Extra.ID);
    public static final EventHandler STAGE_REMOVED = GROUP.server("removed", () -> StageRemovedEventJS.class).extra(Extra.ID);
    public static final EventHandler STAGE_MANAGER = GROUP.server("manager", () -> StageManagerEventJS.class);
}
