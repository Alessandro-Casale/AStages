package com.alessandro.astages.engine;

import com.alessandro.astages.api.manager.AClientMinimalManager;
import com.alessandro.astages.api.manager.AManagerContainer;
import com.alessandro.astages.api.manager.ClientManagerInstance;
import com.alessandro.astages.api.nullability.Nullable;
import com.alessandro.astages.api.plugin.ForPlugins;
import com.alessandro.astages.api.store.ARestrictionType;
import com.alessandro.astages.api.store.container.AttributeStore;
import com.alessandro.astages.engine.client.ClientRestrictionLifecycleService;
import com.alessandro.astages.engine.client.ClientRestrictionRegistry;
import com.alessandro.astages.engine.client.ClientRestrictionReloadState;
import com.alessandro.astages.engine.client.manager.AClientItemManager;
import com.alessandro.astages.engine.client.manager.AClientMobManager;
import com.alessandro.astages.engine.client.manager.AClientOreManager;
import com.alessandro.astages.engine.client.manager.AClientRecipeManager;

import java.util.HashMap;
import java.util.Map;

@AManagerContainer
public class AClientRestrictionManager {
    // ----------------------------------------
    //            MANAGER INSTANCES
    // ----------------------------------------
    @ClientManagerInstance public static final AClientItemManager ITEM_INSTANCE = new AClientItemManager();
    @ClientManagerInstance public static final AClientRecipeManager RECIPE_INSTANCE = new AClientRecipeManager();
    @ClientManagerInstance public static final AClientOreManager ORE_INSTANCE = new AClientOreManager();
    @ClientManagerInstance public static final AClientMobManager MOB_INSTANCE = new AClientMobManager();

    // ----------------------------------------
    //            PLUGIN EXTENSIONS
    // ----------------------------------------
    @ForPlugins public static final Map<Class<?>, AttributeStore> ATTACHED_ATTRIBUTES = new HashMap<>();

    // ----------------------------------------
    //            REGISTRY ACCESS
    // ----------------------------------------
    public static void removeRestriction(String id, ARestrictionType type) {
        ClientRestrictionRegistry.removeRestriction(id, type);
    }

    public static <T> @Nullable T getRestrictionById(String id, ARestrictionType type) {
        return ClientRestrictionRegistry.getRestriction(id, type);
    }

    public static AClientMinimalManager<?, ?> getManagerFromType(ARestrictionType type) {
        return ClientRestrictionRegistry.getManager(type);
    }

    // ----------------------------------------
    //                LIFECYCLE
    // ----------------------------------------
    public static void onReloadStarted() {
        ClientRestrictionLifecycleService.onReloadStarted();
    }

    public static void onReloadFinished() {
        ClientRestrictionLifecycleService.onReloadFinished();
    }

    // ----------------------------------------
    //              RELOAD STATE
    // ----------------------------------------
    public static boolean isReloadFinished() {
        return ClientRestrictionReloadState.isReloadFinished();
    }

    public static boolean areScriptsAvailable() {
        return ClientRestrictionReloadState.areScriptsAvailable();
    }

    public static boolean didJeiFinishReloading() {
        return ClientRestrictionReloadState.didJeiFinishReloading();
    }

    public static boolean ableToUpdateJeiUI() {
        return ClientRestrictionReloadState.ableToUpdateJeiUI();
    }
}