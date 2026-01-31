package com.alessandro.astages.event;

import com.alessandro.astages.AStages;
import com.alessandro.astages.core.AClientModelManager;
import com.alessandro.astages.networking.ANetworking;
import com.alessandro.astages.networking.packet.model.CheckModelsC2SPacket;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.ClientPlayerNetworkEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = AStages.MODID, value = Dist.CLIENT)
public class AClientEventHandler {
    @SubscribeEvent
    public static void onClientLoggedIn(ClientPlayerNetworkEvent.LoggingIn event) {
        ANetworking.sendToServer(new CheckModelsC2SPacket(AClientModelManager.MODELS.getModels()));
    }
}
