package com.alessandro.astages.internal.legacy;

import net.minecraft.world.entity.player.Player;
import net.neoforged.neoforge.event.entity.player.PlayerEvent;

import java.util.List;

@Deprecated(forRemoval = true)
public class StageGetPlayerEvent extends PlayerEvent {
    public final List<String> stages;

    public StageGetPlayerEvent(Player player, List<String> stages) {
        super(player);
        this.stages = stages;
    }
}
