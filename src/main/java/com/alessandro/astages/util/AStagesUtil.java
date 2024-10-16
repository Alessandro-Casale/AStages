package com.alessandro.astages.util;

import com.alessandro.astages.capability.PlayerStageProvider;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import org.jetbrains.annotations.NotNull;

import java.util.concurrent.atomic.AtomicBoolean;

public class AStagesUtil {
    // Util class

    public static boolean isRealPlayer(Player player) {
        return player instanceof ServerPlayer;
    }

    public static boolean hasStage(@NotNull Player player, String stage) {
        AtomicBoolean toReturn = new AtomicBoolean(false);

        player.getCapability(PlayerStageProvider.PLAYER_STAGE).ifPresent(playerStage -> toReturn.set(playerStage.getStages().contains(stage)));

        return toReturn.get();
    }
}
