package com.alessandro.astages.event;

import com.alessandro.astages.AStages;
import com.alessandro.astages.api.AStagesFolderSystem;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.event.lifecycle.FMLLoadCompleteEvent;

@EventBusSubscriber(modid = AStages.MODID)
public class NeoForgeEventHandler {
    @SubscribeEvent
    public static void onConfig(FMLLoadCompleteEvent event) {
        AStagesFolderSystem.buildConfigPaths();
    }
}
