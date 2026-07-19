package com.alessandro.astages.infrastructure.hook.stage;

import com.alessandro.astages.AStages;
import com.alessandro.astages.api.constant.AStageSource;
import com.alessandro.astages.api.event.sync.ClientSynchronizeServerStagesEvent;
import com.alessandro.astages.api.event.sync.ClientSynchronizeStagesEvent;
import com.alessandro.astages.api.nullability.NotNullParams;
import com.alessandro.astages.api.plugin.AStagesPlugin;
import com.alessandro.astages.api.reload.ClientReloadContext;
import com.alessandro.astages.api.reload.ClientReloadPhase;
import com.alessandro.astages.engine.PluginManager;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;

@NotNullParams
@EventBusSubscriber(modid = AStages.MODID, value = Dist.CLIENT)
public class ClientStagesEvents {
    @SubscribeEvent
    public static void onPlayerStagesSynced(ClientSynchronizeStagesEvent event) {
        var context = ClientReloadContext.withStagesSynced(AStageSource.PLAYER, event.getOperation(), event.getStagesSynced());
        PluginManager.callMethod(ClientReloadPhase.STAGES_SYNCED, context, AStagesPlugin::onClientReload, AStagesPlugin::getDescriptionForClientReload);
    }

    @SubscribeEvent
    public static void onServerStagesSynced(ClientSynchronizeServerStagesEvent event) {
        var context = ClientReloadContext.withStagesSynced(AStageSource.SERVER, event.getOperation(), event.getStagesSynced());
        PluginManager.callMethod(ClientReloadPhase.STAGES_SYNCED, context, AStagesPlugin::onClientReload, AStagesPlugin::getDescriptionForClientReload);
    }
}
