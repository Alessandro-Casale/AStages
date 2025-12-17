package com.alessandro.astages.api;

import com.alessandro.astages.api.constant.AStageType;
import com.alessandro.astages.api.holder.AClientHolder;
import com.alessandro.astages.api.holder.AStageHolder;
import com.alessandro.astages.api.nullability.NotNullParamsAndMethodsReturn;
import com.alessandro.astages.capability.ClientPlayerStage;
import com.alessandro.astages.capability.ClientServerStage;
import com.alessandro.astages.core.AClientRestrictionManager;

import java.util.Set;

@NotNullParamsAndMethodsReturn
public class AStagesClientUtils {
    public static Set<String> getStages(AClientHolder holder) {
        return holder.getStages().getAllStages();
    }

    public static void setStages(AClientHolder holder, Set<String> stages) {
        holder.perform(
            () -> ClientPlayerStage.setClientStages(stages),
            () -> ClientServerStage.setServerStages(stages)
        );
    }

    public static boolean hasStage(AClientHolder holder, String stage) {
        return getStages(holder).contains(stage);
    }

    public static boolean hasStage(AClientHolder holder, AStageType type, String stage) {
        return holder.getStages().getForType(type).contains(stage);
    }

    public static boolean hasStage(AStageHolder holder, AStageType type, String stage) {
        return holder.getForType(type).contains(stage);
    }

    public static boolean hasAtLeastOneStage(AClientHolder holder, Set<String> stages) {
        return getStages(holder).stream().anyMatch(stages::contains);
    }

    public static boolean hasAllStages(AClientHolder holder, Set<String> stages) {
        return getStages(holder).containsAll(stages);
    }

    public static void addStage(AClientHolder holder, String stage) {
        holder.perform(
            () -> ClientPlayerStage.addClientStage(stage),
            () -> ClientServerStage.addServerStage(stage)
        );
    }

    public static void addStages(AClientHolder holder, Set<String> stages) {
        holder.perform(
            () -> ClientPlayerStage.addClientStages(stages),
            () -> ClientServerStage.addServerStages(stages)
        );
    }

    public static void addAllStages(AClientHolder holder) {
        var stages = AClientRestrictionManager.ALL_STAGES;

        holder.perform(
            () -> ClientPlayerStage.addClientStages(stages),
            () -> ClientServerStage.addServerStages(stages)
        );
    }

    public static void removeStage(AClientHolder holder, String stage) {
        holder.perform(
            () -> ClientPlayerStage.removeClientStage(stage),
            () -> ClientServerStage.removeServerStage(stage)
        );
    }

    public static void removeStages(AClientHolder holder, Set<String> stages) {
        holder.perform(
            () -> ClientPlayerStage.removeClientStages(stages),
            () -> ClientServerStage.removeServerStages(stages)
        );
    }

    public static void removeAllStages(AClientHolder holder) {
        holder.perform(
            () -> {
                var stages = ClientPlayerStage.getClientStages();
                ClientPlayerStage.removeClientStages(stages);
            },
            () -> {
                var stages = ClientServerStage.getServerStages();
                ClientServerStage.removeServerStages(stages);
            }
        );
    }
}
