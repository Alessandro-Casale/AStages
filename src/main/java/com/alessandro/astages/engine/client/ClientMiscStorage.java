package com.alessandro.astages.engine.client;

import com.alessandro.astages.api.develop.Info;

import java.util.HashSet;
import java.util.Set;

public class ClientMiscStorage {
    public static final Set<String> ORE_STAGES = new HashSet<>();

    @Info("For automatic command completion")
    public static final Set<String> ALL_STAGES = new HashSet<>();
    @Info("For automatic command completion")
    public static final Set<String> DIMENSION_IDS = new HashSet<>();
    @Info("For automatic command completion")
    public static final Set<String> SIMPLE_IDS = new HashSet<>();

    public static void clearAll() {
        ORE_STAGES.clear();
        ALL_STAGES.clear();
        DIMENSION_IDS.clear();
        SIMPLE_IDS.clear();
    }

    public static boolean isOreStage(String stage) {
        return ORE_STAGES.contains(stage);
    }

    public static boolean areOreStages(Set<String> stages) {
        for (String stage : ORE_STAGES) {
            if (stages.contains(stage)) {
                return true;
            }
        }

        return false;
    }
}