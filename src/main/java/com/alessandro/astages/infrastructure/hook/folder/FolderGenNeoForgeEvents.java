package com.alessandro.astages.infrastructure.hook.folder;

import com.alessandro.astages.AStages;
import com.alessandro.astages.infrastructure.folder.AStagesFolderSystem;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.event.lifecycle.FMLLoadCompleteEvent;

@EventBusSubscriber(modid = AStages.MODID)
public class FolderGenNeoForgeEvents {
    @SubscribeEvent
    public static void onConfig(FMLLoadCompleteEvent event) {
        AStagesFolderSystem.buildConfigPaths();
    }
}
