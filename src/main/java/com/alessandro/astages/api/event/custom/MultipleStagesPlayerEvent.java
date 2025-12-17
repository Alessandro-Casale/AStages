package com.alessandro.astages.api.event.custom;

import net.minecraft.world.entity.player.Player;

import java.util.Set;

public class MultipleStagesPlayerEvent extends PlayerEvent {
    public final Set<String> stages;

    public MultipleStagesPlayerEvent(Player player, Set<String> stages) {
        super(player);
        this.stages = stages;
    }

    public Set<String> getStages() {
        return stages;
    }
}
