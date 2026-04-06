package com.alessandro.astages.api.holder;

import com.alessandro.astages.api.constant.AStageType;
import com.alessandro.astages.api.nullability.NotNullMethodsReturn;
import com.alessandro.astages.infrastructure.capability.ClientPlayerStage;
import com.alessandro.astages.infrastructure.capability.ClientServerStage;

@NotNullMethodsReturn
public class AClientHolder {
    private final boolean isServer;
    private final boolean isPlayer;

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

    public AStageHolder getStages() {
        if (isServer && isPlayer) { // Server stages is prioritized!
            return AStageHolder.init()
                .hold(AStageType.PLAYER, ClientPlayerStage.getClientStages())
                .hold(AStageType.SERVER, ClientServerStage.getServerStages());
        }

        if (isPlayer) {
            return AStageHolder.initAndHold(AStageType.PLAYER, ClientPlayerStage.getClientStages());
        }

        if (isServer) {
            return AStageHolder.initAndHold(AStageType.SERVER, ClientServerStage.getServerStages());
        }

        return AStageHolder.init();
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
