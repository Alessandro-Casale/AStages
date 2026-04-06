package com.alessandro.astages.infrastructure.hook.folder;

import com.alessandro.astages.AStages;
import com.alessandro.astages.api.nullability.NotNullParams;
import com.alessandro.astages.infrastructure.folder.AStagesFolderSystem;
import net.neoforged.bus.api.EventPriority;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.server.ServerAboutToStartEvent;

@NotNullParams
@EventBusSubscriber(modid = AStages.MODID)
public class FolderGenServerEvents {
    @SubscribeEvent(priority = EventPriority.HIGHEST)
    public static void onServerStarting(ServerAboutToStartEvent event) {
        var server = event.getServer();
        AStagesFolderSystem.buildServerPaths(server);
    }
}
