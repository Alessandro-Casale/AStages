package com.alessandro.astages.api.event.player;

import com.alessandro.astages.api.event.custom.MultipleStagesPlayerEvent;
import net.minecraft.world.entity.player.Player;

import java.util.Set;

public class AllStagesRemovedPlayerEvent extends MultipleStagesPlayerEvent {
    public AllStagesRemovedPlayerEvent(Player player, Set<String> stages) {
        super(player, stages);
    }
}
