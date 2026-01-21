package com.alessandro.astages.simple;

import com.alessandro.astages.AStages;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.server.ServerStartingEvent;
import net.neoforged.neoforge.event.server.ServerStoppingEvent;

@EventBusSubscriber(modid = AStages.MODID)
public class ServerEventHandler {
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
