package com.alessandro.astages;

import com.alessandro.astages.api.ALoader;
import com.alessandro.astages.api.AResourceLocation;
import com.alessandro.astages.api.constant.AEventPhase;
import com.alessandro.astages.api.constant.AOperation;
import com.alessandro.astages.api.constant.AStageSource;
import com.alessandro.astages.api.constant.ASyncOperation;
import com.alessandro.astages.api.event.update.ClientItemUpdateEvent;
import com.alessandro.astages.api.event.update.ClientOreUpdateEvent;
import com.alessandro.astages.api.event.update.ClientRecipeUpdateEvent;
import com.alessandro.astages.api.holder.AClientHolder;
import com.alessandro.astages.api.holder.AHolder;
import com.alessandro.astages.api.nullability.NotNullParamsAndMethodsReturn;
import com.alessandro.astages.api.plugin.AStagesPlugin;
import com.alessandro.astages.api.reload.ClientReloadContext;
import com.alessandro.astages.api.reload.ClientReloadPhase;
import com.alessandro.astages.api.reload.McReloadPhase;
import com.alessandro.astages.api.reload.ReloadContext;
import com.alessandro.astages.api.util.AStagesClientUtils;
import com.alessandro.astages.api.util.AStagesUtils;
import com.alessandro.astages.engine.*;
import com.alessandro.astages.engine.server.MiscStorage;
import com.alessandro.astages.engine.server.RestrictionEventService;
import com.alessandro.astages.engine.server.RestrictionSyncService;
import com.alessandro.astages.infrastructure.hook.CommonEventSettings;
import com.alessandro.astages.infrastructure.integration.RecipeViewerMods;
import com.alessandro.astages.infrastructure.integration.emi.EmiItemStagesPlugin;
import com.alessandro.astages.infrastructure.integration.emi.EmiRecipeStagesPlugin;
import com.alessandro.astages.infrastructure.integration.jei.JeiItemStagesPlugin;
import com.alessandro.astages.infrastructure.integration.jei.JeiRecipeStagesPlugin;
import com.alessandro.astages.infrastructure.integration.rei.ReiItemStagesPlugin;
import com.alessandro.astages.infrastructure.integration.rei.ReiRecipeStagesPlugin;
import com.alessandro.astages.infrastructure.networking.Networking;
import com.alessandro.astages.infrastructure.networking.packet.reload.RequestReloadS2C;
import dev.emi.emi.registry.EmiStackList;
import dev.emi.emi.runtime.EmiReloadManager;
import dev.emi.emi.search.EmiSearch;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;

import java.util.Set;

@SuppressWarnings("unused")
@NotNullParamsAndMethodsReturn
public class AInternalPlugin implements AStagesPlugin {
    @Override
    public void onReload(McReloadPhase phase, ReloadContext context) {
        switch (phase) {
            case WORLD_LOAD_STARTED -> invokeOnReloadStarted();
            case WORLD_LOAD_FINISHED -> invokeOnReloadFinished();
            case RELOAD_STARTED -> {
                invokeOnReloadStarted();
                Networking.sendTo(null, new RequestReloadS2C(ClientReloadPhase.RELOAD_STARTED));
            }
            case RELOAD_FINISHED -> {
                invokeOnReloadFinished();
                Networking.sendTo(null, new RequestReloadS2C(ClientReloadPhase.RELOAD_FINISHED));
            }
            case PLAYER_LOGGED_IN -> {
                var player = context.player();
                invokeOnPlayerLoggedIn(player);
                Networking.sendTo(player, new RequestReloadS2C(ClientReloadPhase.PLAYER_LOGGED_IN));
            }
        }
    }

    @Override
    public void onClientReload(ClientReloadPhase phase, ClientReloadContext context) {
        switch (phase) {
            case PLAYER_CONNECTED, RELOAD_STARTED -> invokeOnClientReloadStarted();
            case PLAYER_LOGGED_IN, RELOAD_FINISHED -> invokeOnClientReloadFinished();

            case INSTANCE_LOAD_STARTED, ASSETS_RELOAD_STARTED -> invokeOnClientAssetsReloadStarted();
            case INSTANCE_LOAD_FINISHED, ASSETS_RELOAD_FINISHED -> invokeOnClientAssetsReloadFinished();

            case STAGES_SYNCED ->
                invokeOnClientStagesSynced(context.getSource(), context.getOperation(), context.getStagesSynced());

            case ITEM_RESTRICTION_MARKED_AS_DIRTY -> {
                AClientRestrictionManager.ITEM_INSTANCE.getRegistry().clearProperties();
                ALoader.EVENT_BUS.post(new ClientItemUpdateEvent());
            }
            case RECIPE_RESTRICTION_MARKED_AS_DIRTY ->
                ALoader.EVENT_BUS.post(new ClientRecipeUpdateEvent());
            case ORE_RESTRICTION_MARKED_AS_DIRTY ->
                ALoader.EVENT_BUS.post(new ClientOreUpdateEvent());
        }
    }

    @Override
    public ResourceLocation id() {
        return AResourceLocation.fromNamespaceAndPath("internal");
    }

    public void invokeOnReloadStarted() {
        AStageManager.onReloadStarted();
        ARestrictionManager.onReloadStarted();
        AModelManager.onReloadStarted();
        ASimpleRestrictionManager.onReloadStarted();
        AStageManager.addStagesViaJavaCode(AEventPhase.RELOAD_STARTED);
        RestrictionEventService.addRestrictionsViaJavaCode(AEventPhase.RELOAD_STARTED);
    }

    public void invokeOnReloadFinished() {
        AStageManager.addStagesViaJavaCode(AEventPhase.RELOAD_FINISHED);
        RestrictionEventService.addRestrictionsViaJavaCode(AEventPhase.RELOAD_FINISHED);
        AModelManager.onReloadFinished();
        AStageManager.onReloadFinished();
        ARestrictionManager.onReloadFinished();
    }

    public void invokeOnPlayerLoggedIn(ServerPlayer player) {
        var playerStages = AStagesUtils.getStages(AHolder.player(player));
        AStagesUtils.synchronizeWithClient(AHolder.player(player), player, AOperation.LOGIN, playerStages);

        RestrictionSyncService.clearClientOnLogin(player);
        AStageManager.clientSynchronization(player);
        RestrictionSyncService.reflectServerStagesChangesToClients(player);
        RestrictionSyncService.reflectSimpleIdsChangesToClients(player, MiscStorage.SIMPLE_IDS, ASyncOperation.ADD);
        RestrictionSyncService.reflectAllStagesChangesToClients(player);
        RestrictionSyncService.clientSynchronization(player);
        CommonEventSettings.allInventoryChanged();
    }

    public void invokeOnClientReloadStarted() {
        AClientStageManager.onReloadStarted();
        AClientRestrictionManager.onReloadStarted();

        if (RecipeViewerMods.isViewerActive(RecipeViewerMods.JEI)) {
            JeiItemStagesPlugin.onReloadStarted();
            JeiRecipeStagesPlugin.onReloadStarted();
        }

        if (RecipeViewerMods.isViewerActive(RecipeViewerMods.REI)) {
            ReiItemStagesPlugin.onReloadStarted();
            ReiRecipeStagesPlugin.onReloadStarted();
        }

        if (RecipeViewerMods.isViewerActive(RecipeViewerMods.EMI)) {
            EmiItemStagesPlugin.onReloadStarted();
            EmiRecipeStagesPlugin.onReloadStarted();
        }
    }

    public void invokeOnClientReloadFinished() {
        AClientStageManager.onReloadFinished();
        AClientRestrictionManager.onReloadFinished();

        if (RecipeViewerMods.isViewerActive(RecipeViewerMods.JEI)) {
            JeiItemStagesPlugin.onReloadFinished();
            JeiRecipeStagesPlugin.onReloadFinished();
        }

        if (RecipeViewerMods.isViewerActive(RecipeViewerMods.REI)) {
            ReiItemStagesPlugin.onReloadFinished();
            ReiRecipeStagesPlugin.onReloadFinished();
        }

        if (RecipeViewerMods.isViewerActive(RecipeViewerMods.EMI)) {
            EmiItemStagesPlugin.onReloadFinished();
            EmiRecipeStagesPlugin.onReloadFinished();

            var playerStages = AStagesClientUtils.getStages(AClientHolder.serverAndPlayer());
            if (AClientRestrictionManager.RECIPE_INSTANCE.hasRestrictionsFor(playerStages)) {
                EmiReloadManager.reload();
            } else {
                EmiStackList.bakeFiltered();
                EmiSearch.update();
            }
        }
    }

    public void invokeOnClientAssetsReloadStarted() {
        AClientModelManager.onReloadStarted();
    }

    public void invokeOnClientAssetsReloadFinished() {
        AClientModelManager.onReloadFinished();
    }

    public void invokeOnClientStagesSynced(AStageSource source, AOperation operation, Set<String> syncedStages) {
        if (operation == AOperation.LOGIN) { return; }

        if (RecipeViewerMods.isViewerActive(RecipeViewerMods.JEI)) {
            JeiItemStagesPlugin.onStagesChanged(operation, syncedStages);
            JeiRecipeStagesPlugin.onStagesChanged(operation, syncedStages);
        }

        if (RecipeViewerMods.isViewerActive(RecipeViewerMods.REI)) {
            ReiItemStagesPlugin.onStagesChanged(operation, syncedStages);
            ReiRecipeStagesPlugin.onStageChanged(operation, syncedStages);
        }

        if (RecipeViewerMods.isViewerActive(RecipeViewerMods.EMI)) {
            EmiItemStagesPlugin.onStagesChanged(operation, syncedStages);
            EmiRecipeStagesPlugin.onStageChanged(operation, syncedStages);

            if (AClientRestrictionManager.RECIPE_INSTANCE.hasRestrictionsFor(syncedStages)) {
                EmiReloadManager.reload();
            } else {
                EmiStackList.bakeFiltered();
                EmiSearch.update();
            }
        }
    }
}