package com.alessandro.astages.infrastructure.integration.kubejs.event;

import com.alessandro.astages.api.event.player.StageRemovedPlayerEvent;
import dev.latvian.mods.kubejs.player.KubePlayerEvent;
import net.minecraft.world.entity.player.Player;

public class KubeJSStageRemovedEvent implements KubePlayerEvent {
    StageRemovedPlayerEvent event;

    public KubeJSStageRemovedEvent(StageRemovedPlayerEvent event) {
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
