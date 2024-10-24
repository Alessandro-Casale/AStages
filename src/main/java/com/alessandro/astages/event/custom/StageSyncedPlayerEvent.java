package com.alessandro.astages.event.custom;

import com.alessandro.astages.capability.PlayerStage;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.Cancelable;
import net.minecraftforge.eventbus.api.Event;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Cancelable
public class StageSyncedPlayerEvent extends PlayerEvent {
    final PlayerStage.Operation operation;
    final List<String> stagesSynced;

    public StageSyncedPlayerEvent(Player player, PlayerStage.Operation operation, String stageSynced) {
//        super(player);
//        this.operation = operation;
//        List<String> list =  new ArrayList<>();
//        list.add(stageSynced);
//        this.stagesSynced = list;

        this(player, operation, Collections.singletonList(stageSynced));
    }

    public StageSyncedPlayerEvent(Player player, PlayerStage.Operation operation, List<String> stagesSynced) {
        super(player);
        this.operation = operation;
        this.stagesSynced = stagesSynced;
    }

    public PlayerStage.Operation getOperation() {
        return operation;
    }

    public List<String> getStagesSynced() {
        return stagesSynced;
    }
}
