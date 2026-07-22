package com.alessandro.astages.infrastructure.hook.reload;

import com.alessandro.astages.AStages;
import com.alessandro.astages.api.nullability.NotNullParams;
import com.alessandro.astages.api.plugin.AStagesPlugin;
import com.alessandro.astages.api.reload.McReloadPhase;
import com.alessandro.astages.api.reload.ReloadContext;
import com.alessandro.astages.engine.PluginManager;
import net.minecraft.server.level.ServerPlayer;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.player.PlayerEvent;
import net.neoforged.neoforge.event.server.*;

@NotNullParams
@EventBusSubscriber(modid = AStages.MODID)
public class ReloadServerEvents {
    @SubscribeEvent
    public static void serverAboutToStart(ServerAboutToStartEvent event) {
        var context = new ReloadContext(event.getServer());
        PluginManager.callMethod(McReloadPhase.SERVER_ABOUT_TO_START, context, AStagesPlugin::onReload, AStagesPlugin::getDescriptionForReload);
    }

    @SubscribeEvent
    public static void serverStarting(ServerStartingEvent event) {
        var context = new ReloadContext(event.getServer());
        PluginManager.callMethod(McReloadPhase.SERVER_STARTING, context, AStagesPlugin::onReload, AStagesPlugin::getDescriptionForReload);
    }

    @SubscribeEvent
    public static void serverStarted(ServerStartedEvent event) {
        var context = new ReloadContext(event.getServer());
        PluginManager.callMethod(McReloadPhase.SERVER_STARTED, context, AStagesPlugin::onReload, AStagesPlugin::getDescriptionForReload);
    }

    @SubscribeEvent
    public static void serverStopping(ServerStoppingEvent event) {
        var context = new ReloadContext(event.getServer());
        PluginManager.callMethod(McReloadPhase.SERVER_STOPPING, context, AStagesPlugin::onReload, AStagesPlugin::getDescriptionForReload);
    }

    @SubscribeEvent
    public static void serverStopped(ServerStoppedEvent event) {
        var context = new ReloadContext(event.getServer());
        PluginManager.callMethod(McReloadPhase.SERVER_STOPPED, context, AStagesPlugin::onReload, AStagesPlugin::getDescriptionForReload);
    }

    @SubscribeEvent
    public static void realPlayerLoggedIn(PlayerEvent.PlayerLoggedInEvent event) {
        if (!event.getEntity().level().isClientSide && event.getEntity() instanceof ServerPlayer player) {
            var context = new ReloadContext(player);
            PluginManager.callMethod(McReloadPhase.PLAYER_LOGGED_IN, context, AStagesPlugin::onReload, AStagesPlugin::getDescriptionForReload);
        }
    }

    @SubscribeEvent
    public static void realPlayerLoggedOut(PlayerEvent.PlayerLoggedOutEvent event) {
        if (!event.getEntity().level().isClientSide && event.getEntity() instanceof ServerPlayer player) {
            var context = new ReloadContext(player);
            PluginManager.callMethod(McReloadPhase.PLAYER_LOGGED_OUT, context, AStagesPlugin::onReload, AStagesPlugin::getDescriptionForReload);
        }
    }
}