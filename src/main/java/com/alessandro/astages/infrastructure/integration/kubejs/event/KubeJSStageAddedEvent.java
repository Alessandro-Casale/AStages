package com.alessandro.astages.infrastructure.integration.kubejs.event;

import com.alessandro.astages.api.event.player.StageAddedPlayerEvent;
import dev.latvian.mods.kubejs.player.KubePlayerEvent;
import net.minecraft.world.entity.player.Player;

public class KubeJSStageAddedEvent implements KubePlayerEvent {
    StageAddedPlayerEvent event;

    public KubeJSStageAddedEvent(StageAddedPlayerEvent event) {
        this.event = event;
    }

    @Override
    public Player getEntity() {
        return event.getPlayer();
    }

    public String getStage() {
        return event.stage;
    }
}
