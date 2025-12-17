package com.alessandro.astages.api.event.player;

import com.alessandro.astages.api.event.custom.MultipleStagesPlayerEvent;
import net.minecraft.world.entity.player.Player;

import java.util.Set;

public class AllStagesAddedPlayerEvent extends MultipleStagesPlayerEvent {
    public AllStagesAddedPlayerEvent(Player player, Set<String> stages) {
        super(player, stages);
    }
}
