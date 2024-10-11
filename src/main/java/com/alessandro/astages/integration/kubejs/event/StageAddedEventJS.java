package com.alessandro.astages.integration.kubejs.event;

import com.alessandro.astages.event.custom.StageAddedPlayerEvent;
import dev.latvian.mods.kubejs.player.PlayerEventJS;
import net.minecraft.world.entity.player.Player;

public class StageAddedEventJS extends PlayerEventJS {
    StageAddedPlayerEvent event;

    public StageAddedEventJS(StageAddedPlayerEvent event) {
        this.event = event;
    }

    @Override
    public Player getEntity() {
        return event.getEntity();
    }

    public String getStage() {
        return event.stage;
    }
}
