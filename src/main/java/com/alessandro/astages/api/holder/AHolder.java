package com.alessandro.astages.api.holder;

import com.alessandro.astages.AStages;
import com.alessandro.astages.api.constant.AStageType;
import com.alessandro.astages.api.develop.UnderDevelopment;
import com.alessandro.astages.api.nullability.NotNullParamsAndMethodsReturn;
import com.alessandro.astages.api.nullability.Nullable;
import com.alessandro.astages.infrastructure.capability.OfflinePlayerStage;
import com.alessandro.astages.infrastructure.capability.ServerStage;
import com.alessandro.astages.infrastructure.config.AStagesCommon;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.entity.player.Player;
import net.neoforged.neoforge.server.ServerLifecycleHooks;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.UUID;
import java.util.function.Consumer;

@UnderDevelopment("Use getFirst()!")
@NotNullParamsAndMethodsReturn
public class AHolder {
    private final boolean isServer;
    private boolean isPlayer;
    private final boolean isMultiple;

    private final List<UUID> uuids = new ArrayList<>();

    private AHolder(boolean isServer, boolean isPlayer, boolean isMultiple) {
        this.isServer = isServer;
        this.isPlayer = isPlayer;
        this.isMultiple = isMultiple;
    }

    public static AHolder player(Player player) {
        var toReturn = new AHolder(false, true, false);
        toReturn.addPlayer(player);
        return toReturn;
    }

    public static AHolder player(UUID uuid) {
        return new AHolder(false, true, false).addPlayer(uuid);
    }

    public static AHolder players(List<Player> players) {
        var toReturn = new AHolder(false, true, true);
        players.forEach(toReturn::addPlayer);
        return toReturn;
    }

    public static AHolder server() {
        return new AHolder(true, false, false);
    }

    public static AHolder serverAndPlayer(@Nullable Player player) {
        var holder = new AHolder(true, true, false);

        if (player != null) {
            holder.addPlayer(player);
        } else {
            holder.setPlayer(false);

            if (AStagesCommon.ENABLE_DEV_LOGS.get()) {
                AStages.LOGGER.debug("Encountered null player, skipped adding it to holder!");
            }
        }

        return holder;
    }

    private AHolder addPlayer(UUID uuid) {
        uuids.add(uuid);
        return this;
    }

    private AHolder addPlayer(Player player) {
        uuids.add(player.getUUID());
        return this;
    }

    public boolean isServerActive() {
        return isServer;
    }

    public boolean isPlayerActive() {
        return isPlayer;
    }

    public AStageHolder getStages() {
        var holder = AStageHolder.init();

        if (isPlayer && !isMultiple) {
            holder.hold(AStageType.PLAYER, OfflinePlayerStage.getPlayerStagesFromCache(uuids.getFirst()));
        } else if (isPlayer) {
            var stageSet = new HashSet<String>();
            for (UUID uuid : uuids) { stageSet.addAll(OfflinePlayerStage.getPlayerStagesFromCache(uuid)); }
            holder.hold(AStageType.PLAYER, stageSet);
        }

        if (isServer) {
            holder.hold(AStageType.SERVER, ServerStage.getServerStages());
        }

        return holder;
    }

    public void perform(Consumer<UUID> forPlayer, Consumer<MinecraftServer> forServer) {
        advancedPerform(forPlayer, players -> {
            for (var player : players) {
                forPlayer.accept(player);
            }
        }, forServer);
    }

    public void advancedPerform(Consumer<UUID> forPlayer, Consumer<List<UUID>> forPlayers, Consumer<MinecraftServer> forServer) {
        if (isServer) {
            forServer.accept(ServerLifecycleHooks.getCurrentServer());
        }

        if (isPlayer && !isMultiple) {
            forPlayer.accept(uuids.getFirst());
        }

        if (isPlayer && isMultiple) {
            forPlayers.accept(uuids);
        }
    }

    private AHolder setPlayer(boolean player) {
        isPlayer = player;
        return this;
    }
}
