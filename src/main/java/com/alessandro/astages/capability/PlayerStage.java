package com.alessandro.astages.capability;

import com.alessandro.astages.AStages;
import com.alessandro.astages.event.custom.StageAddedPlayerEvent;
import com.alessandro.astages.event.custom.StageRemovedPlayerEvent;
import com.alessandro.astages.networking.ModNetworking;
import com.alessandro.astages.networking.packet.StageDataSyncS2CPacket;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.capabilities.AutoRegisterCapability;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@AutoRegisterCapability
public class PlayerStage {
    public enum Status {
        SUCCESS, NOT_PRESENT
    }

    public enum Operation {
        ADD, REMOVE, REMOVE_ALL, GET
    }

    private List<String> stages = new ArrayList<>();

    public void setChangedFor(Player player, @NotNull Operation operation, String stage) {
        ModNetworking.sendToPlayer(new StageDataSyncS2CPacket(stages), (ServerPlayer) player);

        switch (operation) {
            case ADD -> MinecraftForge.EVENT_BUS.post(new StageAddedPlayerEvent(player, stage));
            case REMOVE -> MinecraftForge.EVENT_BUS.post(new StageRemovedPlayerEvent(player, stage));
            case REMOVE_ALL, GET -> AStages.LOGGER.debug("NOT YET IMPLEMENTED!");
        }
    }

    public List<String> getStages() {
        if (stages == null) {
            return Collections.emptyList();
        }

        return stages;
    }

    public void addStage(String stage) {
        if (stages.contains(stage)) { return; }

        stages.add(stage);
    }

    public void removeAllStages() {
        stages = new ArrayList<>();
    }

    public Status removeStage(String stage) {
        return stages.remove(stage) ? Status.SUCCESS : Status.NOT_PRESENT;
    }

    public void copyFrom(@NotNull PlayerStage source) {
        stages = source.stages;
    }

    public void saveNBTData(@NotNull CompoundTag nbt) {
        if (stages == null) { return; }
        if (stages.isEmpty()) { return; }

        nbt.putInt("stage_size", stages.size());

        for (int i = 0; i < stages.size(); i++) {
            nbt.putString("stage_" + i, stages.get(i));
        }
    }

    public void loadNBTData(@NotNull CompoundTag nbt) {
        var size = nbt.getInt("stage_size");

        if (size > 0) {
            stages = new ArrayList<>();

            for (int i = 0; i < size; i++) {
                stages.add(nbt.getString("stage_" + i));
            }
        }
    }
}
