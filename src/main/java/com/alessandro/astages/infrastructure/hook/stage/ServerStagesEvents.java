package com.alessandro.astages.infrastructure.hook.stage;

import com.alessandro.astages.AStages;
import com.alessandro.astages.api.develop.Info;
import com.alessandro.astages.api.util.AFileIOUtils;
import com.alessandro.astages.infrastructure.capability.ServerStage;
import net.neoforged.bus.api.EventPriority;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.server.ServerStartingEvent;
import net.neoforged.neoforge.event.server.ServerStoppingEvent;

@EventBusSubscriber(modid = AStages.MODID)
public class ServerStagesEvents {
    @SubscribeEvent
    public static void onServerStarting(ServerStartingEvent event) {
        ServerStage.setCache(
            AFileIOUtils.readHashSetOrDefault(
                ServerStage.getPermanentStagesFile(),
                String.class
            )
        );
    }

    @SubscribeEvent
    public static void onServerStopping(ServerStoppingEvent event) {
        ServerStage.markAsDirty();
        ServerStage.clearCache();
    }

    @Info("Migration purpose only!")
    @SubscribeEvent(priority = EventPriority.HIGHEST)
    public static void onServerStartingHighest(ServerStartingEvent event) {
        var file = ServerStage.getPermanentStagesFile();
        var stageList = AFileIOUtils.readList(file, String.class);

        if (stageList == null) {
            var oldList = ServerStage.getServerStagesFromData(event.getServer());
            AFileIOUtils.writeFileContent(file, oldList);
        }
    }
}
