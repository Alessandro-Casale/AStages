package com.alessandro.astages.infrastructure.hook.simple;

import com.alessandro.astages.AStages;
import com.alessandro.astages.engine.ASimpleRestrictionManager;
import com.alessandro.astages.engine.simple.ASimpleMigrationManager;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.server.ServerStartingEvent;
import net.neoforged.neoforge.event.server.ServerStoppingEvent;

@EventBusSubscriber(modid = AStages.MODID)
public class SimpleServerEvents {
    @SubscribeEvent
    public static void serverLoading(ServerStartingEvent event) {
        ASimpleMigrationManager.startMigration();
        ASimpleRestrictionManager.readFromFile();
    }

    @SubscribeEvent
    public static void serverStopped(ServerStoppingEvent event) {
        ASimpleRestrictionManager.writeToFile(true);
    }
}
