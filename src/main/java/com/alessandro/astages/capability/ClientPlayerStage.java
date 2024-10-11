package com.alessandro.astages.capability;

import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import java.util.ArrayList;
import java.util.List;

@OnlyIn(Dist.CLIENT)
public class ClientPlayerStage {
    private static List<String> playerStages = new ArrayList<>();

    public static void set(List<String> stages) {
        ClientPlayerStage.playerStages = stages;
    }

    public static List<String> getPlayerStages() {
        return playerStages;
    }
}
