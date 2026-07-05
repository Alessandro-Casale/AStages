package com.alessandro.astages.engine.server;

import java.util.HashSet;
import java.util.Set;

public class MiscStorage {
    public static Set<String> ALL_STAGES = new HashSet<>();
    public static Set<String> STAGES_ONLY_FOR_PLAYER = new HashSet<>();
    public static Set<String> STAGES_ONLY_FOR_SERVER = new HashSet<>();
    public static Set<String> ALL_IDS = new HashSet<>();
    public static Set<String> ORE_STAGES = new HashSet<>();
    public static Set<String> SIMPLE_IDS = new HashSet<>();

    public static void clearAll() {
        ALL_STAGES.clear();
        STAGES_ONLY_FOR_PLAYER.clear();
        STAGES_ONLY_FOR_SERVER.clear();
        ALL_IDS.clear();
        ORE_STAGES.clear();
        SIMPLE_IDS.clear();
    }
}