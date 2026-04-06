package com.alessandro.astages.infrastructure.capability;

import com.alessandro.astages.api.nullability.NotNullMethodsReturn;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@NotNullMethodsReturn
@OnlyIn(Dist.CLIENT)
public class ClientPlayerStage {
    private static Set<String> playerStages = new HashSet<>();

    public static Set<String> getClientStages() {
        return playerStages;
    }

    public static void setClientStages(Set<String> stages) {
        ClientPlayerStage.playerStages = stages;
    }

    public static void addClientStage(String stage) {
        playerStages.add(stage);
    }

    public static void addClientStages(Set<String> stages) {
        playerStages.addAll(stages);
    }

    public static void removeClientStage(String stage) {
        playerStages.remove(stage);
    }

    public static void removeClientStages(Set<String> stages) {
        playerStages.removeAll(stages);
    }

    @Deprecated(forRemoval = true)
    public static List<String> getPlayerStages() {
        return new ArrayList<>(playerStages);
    }

    @Deprecated(forRemoval = true)
    public static boolean hasStage(String stage) {
        return playerStages.contains(stage);
    }

    @Deprecated(forRemoval = true)
    public static void addAllStages(List<String> stages) {
        ClientPlayerStage.playerStages.addAll(stages);
    }

    @Deprecated(forRemoval = true)
    public static void removeAllStages(List<String> stages) {
        ClientPlayerStage.playerStages.removeAll(stages);
    }
}
