package com.alessandro.astages.infrastructure.hook;

import com.alessandro.astages.AStages;
import com.alessandro.astages.api.constant.AEventPhase;
import com.alessandro.astages.api.constant.AOperation;
import com.alessandro.astages.api.constant.ASyncOperation;
import com.alessandro.astages.api.develop.UnderDevelopment;
import com.alessandro.astages.api.event.reload.ReloadScriptEvent;
import com.alessandro.astages.api.holder.AHolder;
import com.alessandro.astages.api.nullability.NotNullParams;
import com.alessandro.astages.api.plugin.AStagesPlugin;
import com.alessandro.astages.api.reload.AStagesReloadPhase;
import com.alessandro.astages.api.reload.ReloadContext;
import com.alessandro.astages.api.util.AStagesUtils;
import com.alessandro.astages.engine.ARestrictionManager;
import com.alessandro.astages.engine.ASimpleRestrictionManager;
import com.alessandro.astages.engine.AStageManager;
import com.alessandro.astages.engine.PluginManager;
import com.alessandro.astages.engine.server.MiscStorage;
import com.alessandro.astages.engine.server.RestrictionEventService;
import com.alessandro.astages.engine.server.RestrictionSyncService;
import net.minecraft.server.level.ServerPlayer;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.player.PlayerEvent;
import net.neoforged.neoforge.server.ServerLifecycleHooks;

@NotNullParams
@EventBusSubscriber(modid = AStages.MODID)
public class ManagerServerEvents {
    @UnderDevelopment
    @SubscribeEvent
    public static void beforeScripts(ReloadScriptEvent.BeforeScriptsLoaded event) {
        if (event.getScriptType() == ReloadScriptEvent.EventScriptType.SERVER) {
            ReloadContext context = new ReloadContext(ServerLifecycleHooks.getCurrentServer());

            PluginManager.callMethod(AStagesReloadPhase.INITIAL, context, AStagesPlugin::onReload);

            PluginManager.callMethod(AStagesReloadPhase.BEFORE_MANAGERS_BEFORE_SCRIPTS_RELOAD, context, AStagesPlugin::onReload);
            AStageManager.reloadBeforeScripts();
            ARestrictionManager.reloadBeforeScripts();
            ASimpleRestrictionManager.reloadBeforeScripts();
            PluginManager.callMethod(AStagesReloadPhase.AFTER_MANAGERS_BEFORE_SCRIPTS_RELOAD, context, AStagesPlugin::onReload);

            PluginManager.callMethod(AStagesReloadPhase.BEFORE_JAVA_REGISTRATION_BEFORE_SCRIPTS_RELOAD, context, AStagesPlugin::onReload);
            AStageManager.addStagesViaJavaCode(AEventPhase.BEFORE_JS);
            RestrictionEventService.addRestrictionsViaJavaCode(AEventPhase.BEFORE_JS);
            PluginManager.callMethod(AStagesReloadPhase.AFTER_JAVA_REGISTRATION_BEFORE_SCRIPTS_RELOAD, context, AStagesPlugin::onReload);

            PluginManager.callMethod(AStagesReloadPhase.BEFORE_SCRIPTS_RELOAD, context, AStagesPlugin::onReload);
        }
    }

    @UnderDevelopment
    @SubscribeEvent
    public static void afterScripts(ReloadScriptEvent.AfterScriptsLoaded event) {
        if (event.getScriptType() == ReloadScriptEvent.EventScriptType.SERVER) {
            ReloadContext context = new ReloadContext(ServerLifecycleHooks.getCurrentServer());

            PluginManager.callMethod(AStagesReloadPhase.AFTER_SCRIPTS_RELOAD, context, AStagesPlugin::onReload);

            PluginManager.callMethod(AStagesReloadPhase.BEFORE_JAVA_REGISTRATION_AFTER_SCRIPTS_RELOAD, context, AStagesPlugin::onReload);
            AStageManager.addStagesViaJavaCode(AEventPhase.AFTER_JS);
            RestrictionEventService.addRestrictionsViaJavaCode(AEventPhase.AFTER_JS);
            PluginManager.callMethod(AStagesReloadPhase.AFTER_JAVA_REGISTRATION_AFTER_SCRIPTS_RELOAD, context, AStagesPlugin::onReload);

            PluginManager.callMethod(AStagesReloadPhase.BEFORE_MANAGERS_AFTER_SCRIPTS_RELOAD, context, AStagesPlugin::onReload);
            AStageManager.reloadAfterScripts();
            ARestrictionManager.reloadAfterScripts();
            PluginManager.callMethod(AStagesReloadPhase.AFTER_MANAGERS_AFTER_SCRIPTS_RELOAD, context, AStagesPlugin::onReload);

            PluginManager.callMethod(AStagesReloadPhase.END, context, AStagesPlugin::onReload);
        }
    }

    @SubscribeEvent
    public static void onPlayerLoggedIn(PlayerEvent.PlayerLoggedInEvent event) {
        if (!event.getEntity().level().isClientSide && event.getEntity() instanceof ServerPlayer serverPlayer) {
            var playerStages = AStagesUtils.getStages(AHolder.player(serverPlayer));
            AStagesUtils.synchronizeWithClient(AHolder.player(serverPlayer), serverPlayer, AOperation.LOGIN, playerStages, true);

            RestrictionSyncService.clearClientOnLogin(serverPlayer);
            AStageManager.clientSynchronization(serverPlayer);
            RestrictionSyncService.reflectServerStagesChangesToClients(serverPlayer);
            RestrictionSyncService.reflectSimpleIdsChangesToClients(serverPlayer, MiscStorage.SIMPLE_IDS, ASyncOperation.ADD);
            RestrictionSyncService.reflectAllStagesChangesToClients(serverPlayer);
            RestrictionSyncService.clientSynchronization(serverPlayer);
        }
    }
}