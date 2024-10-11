package com.alessandro.astages.event;

import com.alessandro.astages.AStages;
import com.alessandro.astages.event.custom.InitialStageAdditionEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.server.ServerStartingEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = AStages.MODID)
public class ServerEventHandler {
    @SubscribeEvent
    public static void onServerLoaded(ServerStartingEvent event) {
        MinecraftForge.EVENT_BUS.post(new InitialStageAdditionEvent());
    }
}
