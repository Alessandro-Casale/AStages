package com.alessandro.astages.capability;

import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;

import java.util.HashSet;
import java.util.Set;

@OnlyIn(Dist.CLIENT)
public class ClientServerStage {
    private static Set<String> serverStages = new HashSet<>();

    public static Set<String> getServerStages() {
        return serverStages;
    }

    public static void setServerStages(Set<String> stages) {
        ClientServerStage.serverStages = stages;
    }

    public static void addServerStage(String stage) {
        serverStages.add(stage);
    }

    public static void addServerStages(Set<String> stages) {
        serverStages.addAll(stages);
    }

    public static void removeServerStage(String stage) {
        serverStages.remove(stage);
    }

    public static void removeServerStages(Set<String> stages) {
        serverStages.removeAll(stages);
    }
}
