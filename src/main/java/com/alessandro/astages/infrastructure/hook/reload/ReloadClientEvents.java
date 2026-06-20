package com.alessandro.astages.infrastructure.hook.reload;

import com.alessandro.astages.AStages;
import com.alessandro.astages.api.plugin.AStagesPlugin;
import com.alessandro.astages.api.reload.ClientReloadContext;
import com.alessandro.astages.api.reload.ClientReloadPhase;
import com.alessandro.astages.engine.PluginManager;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.ClientPlayerNetworkEvent;

@EventBusSubscriber(modid = AStages.MODID, value = Dist.CLIENT)
public class ReloadClientEvents {
    @SubscribeEvent
    public static void clientConnected(ClientPlayerNetworkEvent.LoggingIn event) {
        var context = new ClientReloadContext();
        PluginManager.callMethod(ClientReloadPhase.PLAYER_CONNECTED, context, AStagesPlugin::onClientReload);
    }

    @SubscribeEvent
    public static void clientDisconnected(ClientPlayerNetworkEvent.LoggingOut event) {
        var context = new ClientReloadContext();
        PluginManager.callMethod(ClientReloadPhase.PLAYER_DISCONNECTED, context, AStagesPlugin::onClientReload);
    }
}