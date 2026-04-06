package com.alessandro.astages;

import com.alessandro.astages.api.AResourceLocation;
import com.alessandro.astages.api.constant.AEventPhase;
import com.alessandro.astages.api.constant.AOperation;
import com.alessandro.astages.api.constant.ASyncOperation;
import com.alessandro.astages.api.holder.AHolder;
import com.alessandro.astages.api.nullability.NotNullParamsAndMethodsReturn;
import com.alessandro.astages.api.plugin.AStagesPlugin;
import com.alessandro.astages.api.reload.McReloadPhase;
import com.alessandro.astages.api.reload.ReloadContext;
import com.alessandro.astages.api.util.AStagesUtils;
import com.alessandro.astages.engine.ARestrictionManager;
import com.alessandro.astages.engine.ASimpleRestrictionManager;
import com.alessandro.astages.engine.AStageManager;
import com.alessandro.astages.engine.server.MiscStorage;
import com.alessandro.astages.engine.server.RestrictionEventService;
import com.alessandro.astages.engine.server.RestrictionSyncService;
import com.alessandro.astages.infrastructure.hook.CommonEventSettings;
import net.minecraft.resources.ResourceLocation;

@SuppressWarnings("unused")
@NotNullParamsAndMethodsReturn
public class AInternalPlugin implements AStagesPlugin {
    @Override
    public void onReload(McReloadPhase phase, ReloadContext context) {
        if (phase == McReloadPhase.WORLD_LOAD_STARTED || phase == McReloadPhase.RELOAD_STARTED) {
            AStageManager.reloadBeforeScripts();
            ARestrictionManager.reloadBeforeScripts();
            ASimpleRestrictionManager.reloadBeforeScripts();
            AStageManager.addStagesViaJavaCode(AEventPhase.BEFORE_JS);
            RestrictionEventService.addRestrictionsViaJavaCode(AEventPhase.BEFORE_JS);
            return;
        }

        if (phase == McReloadPhase.WORLD_LOAD_FINISHED || phase == McReloadPhase.RELOAD_FINISHED) {
            AStageManager.addStagesViaJavaCode(AEventPhase.AFTER_JS);
            RestrictionEventService.addRestrictionsViaJavaCode(AEventPhase.AFTER_JS);
            AStageManager.reloadAfterScripts();
            ARestrictionManager.reloadAfterScripts();
            return;
        }

        if (phase == McReloadPhase.PLAYER_LOGGED_IN) {
            var player = context.player();

            var playerStages = AStagesUtils.getStages(AHolder.player(player));
            AStagesUtils.synchronizeWithClient(AHolder.player(player), player, AOperation.LOGIN, playerStages, true);

            RestrictionSyncService.clearClientOnLogin(player);
            AStageManager.clientSynchronization(player);
            RestrictionSyncService.reflectServerStagesChangesToClients(player);
            RestrictionSyncService.reflectSimpleIdsChangesToClients(player, MiscStorage.SIMPLE_IDS, ASyncOperation.ADD);
            RestrictionSyncService.reflectAllStagesChangesToClients(player);
            RestrictionSyncService.clientSynchronization(player);
            CommonEventSettings.allInventoryChanged();
        }
    }

    @Override
    public ResourceLocation id() {
        return AResourceLocation.fromNamespaceAndPath("internal");
    }
}