package com.alessandro.astages.infrastructure.integration.kubejs.util;

import com.alessandro.astages.api.holder.AClientHolder;
import com.alessandro.astages.api.nullability.NotNullParamsAndMethodsReturn;
import com.alessandro.astages.api.util.AStagesClientUtils;
import net.minecraft.world.entity.player.Player;

import java.util.Set;

@NotNullParamsAndMethodsReturn
public class KubeJSClientUtils {
    // Player Stages
    public static void addStageToPlayer(String stage) {
        AStagesClientUtils.addStage(AClientHolder.player(), stage);
    }

    public static void addStagesToPlayer(Set<String> stages) {
        AStagesClientUtils.addStages(AClientHolder.player(), stages);
    }

    public static void removeStageFromPlayer(String stage) {
        AStagesClientUtils.removeStage(AClientHolder.player(), stage);
    }

    public static void removeStagesFromPlayer(Set<String> stages) {
        AStagesClientUtils.removeStages(AClientHolder.player(), stages);
    }

    public static Set<String> getStagesFromPlayer(Player player) {
        return AStagesClientUtils.getStages(AClientHolder.player());
    }

    public static void removeAllStagesFromPlayer(Player player) {
        AStagesClientUtils.removeAllStages(AClientHolder.player());
    }

    public static boolean playerHasStage(String stage) {
        return AStagesClientUtils.hasStage(AClientHolder.player(), stage);
    }

    public static boolean playerHasAtLeastOneStage(Set<String> stages) {
        return AStagesClientUtils.hasAtLeastOneStage(AClientHolder.player(), stages);
    }

    public static boolean playerHasAllStages(Set<String> stages) {
        return AStagesClientUtils.hasAllStages(AClientHolder.player(), stages);
    }

    // Server Stages
    public static void addStageToServer(String stage) {
        AStagesClientUtils.addStage(AClientHolder.server(), stage);
    }

    public static void addStagesToServer(Set<String> stages) {
        AStagesClientUtils.addStages(AClientHolder.server(), stages);
    }

    public static void removeStageFromServer(String stage) {
        AStagesClientUtils.removeStage(AClientHolder.server(), stage);
    }

    public static void removeStagesFromServer(Set<String> stages) {
        AStagesClientUtils.removeStages(AClientHolder.server(), stages);
    }

    public static void removeAllStagesFromServer() {
        AStagesClientUtils.removeAllStages(AClientHolder.server());
    }

    public static Set<String> getStagesFromServer() {
        return AStagesClientUtils.getStages(AClientHolder.server());
    }

    public static boolean serverHasStage(String stage) {
        return AStagesClientUtils.hasStage(AClientHolder.server(), stage);
    }

    public static boolean serverHasAtLeastOneStage(Set<String> stages) {
        return AStagesClientUtils.hasAtLeastOneStage(AClientHolder.server(), stages);
    }

    public static boolean serverHasAllStages(Set<String> stages) {
        return AStagesClientUtils.hasAllStages(AClientHolder.server(), stages);
    }
}
