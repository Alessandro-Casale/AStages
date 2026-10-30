package com.alessandro.astages.api.holder;

import com.alessandro.astages.api.constant.AStageType;
import com.alessandro.astages.api.nullability.NotNullParamsAndMethodsReturn;
import com.alessandro.astages.api.nullability.Nullable;
import com.alessandro.astages.api.stage.AStageContext;
import com.alessandro.astages.infrastructure.capability.ClientPlayerStage;
import com.alessandro.astages.infrastructure.capability.ClientServerStage;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@NotNullParamsAndMethodsReturn
public class AClientHolder implements AStageContext {
    private final boolean isServer;
    private final boolean isPlayer;

    private @Nullable List<AStageContext> types;

    private AClientHolder(boolean isServer, boolean isPlayer) {
        this.isServer = isServer;
        this.isPlayer = isPlayer;
    }

    public static AClientHolder player() {
        return new AClientHolder(false, true);
    }

    public static AClientHolder server() {
        return new AClientHolder(true, false);
    }

    public static AClientHolder serverAndPlayer() {
        return new AClientHolder(true, true);
    }

    public boolean isServerActive() {
        return isServer;
    }

    public boolean isPlayerActive() {
        return isPlayer;
    }

    @Override
    public List<AStageContext> types() {
        if (types == null) {
            var types = new ArrayList<AStageContext>(2);
            if (isServer) { types.add(AStageContext.of(ClientServerStage.getServerStages())); }
            if (isPlayer) { types.add(AStageContext.of(ClientPlayerStage.getClientStages())); }

            this.types = List.copyOf(types);
        }

        return types;
    }

    @Override
    public boolean isUnlocked(String stage) {
        for (var part : types()) {
            if (part.isUnlocked(stage)) { return true; }
        }

        return false;
    }

    public Set<String> getUnlockedStages() {
        var stages = new HashSet<String>();

        if (isServer) { stages.addAll(ClientServerStage.getServerStages()); }
        if (isPlayer) { stages.addAll(ClientPlayerStage.getClientStages()); }

        return stages;
    }

    @Deprecated(forRemoval = true, since = "3.0.0")
    public AStageHolder getStages() {
        var holder = AStageHolder.init();

        if (isPlayer) {
            holder.hold(AStageType.PLAYER, ClientPlayerStage.getClientStages());
        }

        if (isServer) {
            holder.hold(AStageType.SERVER, ClientServerStage.getServerStages());
        }

        return holder;
    }

    public void perform(Runnable forPlayer, Runnable forServer) {
        if (isServer) {
            forServer.run();
        }

        if (isPlayer) {
            forPlayer.run();
        }
    }
}
