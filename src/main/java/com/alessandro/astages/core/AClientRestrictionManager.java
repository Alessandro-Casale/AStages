package com.alessandro.astages.core;

import com.alessandro.astages.api.ALoader;
import com.alessandro.astages.api.develop.Info;
import com.alessandro.astages.core.client.manager.AClientItemManager;
import com.alessandro.astages.core.client.manager.AClientMobManager;
import com.alessandro.astages.core.client.manager.AClientOreManager;
import com.alessandro.astages.core.client.manager.AClientRecipeManager;
import com.alessandro.astages.event.custom.actions.ClientItemUpdateEvent;
import com.alessandro.astages.event.custom.actions.ClientRecipeUpdateEvent;
import com.alessandro.astages.store.ARestrictionType;
import com.alessandro.astages.store.client.AClientMinimalManager;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class AClientRestrictionManager {
    public static final Map<ARestrictionType, AClientMinimalManager<?>> ASSOCIATION_MAP = new HashMap<>();

    public static final AClientItemManager ITEM_INSTANCE = new AClientItemManager();
    public static final AClientRecipeManager RECIPE_INSTANCE = new AClientRecipeManager();
    public static final AClientOreManager ORE_INSTANCE = new AClientOreManager();
    public static final AClientMobManager MOB_INSTANCE = new AClientMobManager();

    public static final Set<String> ORE_STAGES = new HashSet<>();

    @Info("For automatic command completion")
    public static final Set<String> ALL_STAGES = new HashSet<>();
    @Info("For automatic command completion")
    public static final Set<String> DIMENSION_IDS = new HashSet<>();
    @Info("For automatic command completion")
    public static final Set<String> SIMPLE_IDS = new HashSet<>();

    private static boolean areScriptsAvailable = false;
    private static boolean didJeiFinishReloading = false;
    private static boolean isReloading = false;

    static {
        ASSOCIATION_MAP.put(ITEM_INSTANCE.associatedType(), ITEM_INSTANCE);
        ASSOCIATION_MAP.put(RECIPE_INSTANCE.associatedType(), RECIPE_INSTANCE);
        ASSOCIATION_MAP.put(ORE_INSTANCE.associatedType(), ORE_INSTANCE);
        ASSOCIATION_MAP.put(MOB_INSTANCE.associatedType(), MOB_INSTANCE);
    }

    public static void reloadBeforeScripts() {
        AClientRestrictionManager.areScriptsAvailable(false);

        ITEM_INSTANCE.reloadBeforeScripts();
        RECIPE_INSTANCE.reloadBeforeScripts();
        ORE_INSTANCE.reloadBeforeScripts();
        MOB_INSTANCE.reloadBeforeScripts();

        ORE_STAGES.clear();
        DIMENSION_IDS.clear();
        SIMPLE_IDS.clear();
    }

    public static void reloadAfterScripts() {
        AClientRestrictionManager.areScriptsAvailable(true);
    }

    public static boolean isOreStage(String stage) {
        return ORE_STAGES.contains(stage);
    }

    public static boolean areOreStages(Set<String> stages) {
        for (String stage : ORE_STAGES) {
            if (stages.contains(stage)) {
                return true;
            }
        }

        return false;
    }

    public static void areScriptsAvailable(boolean areScriptsAvailable) {
        AClientRestrictionManager.areScriptsAvailable = areScriptsAvailable;

        if (AClientRestrictionManager.areScriptsAvailable) {
            if (AClientRestrictionManager.didJeiFinishReloading) {
                isReloading = false;
            }

            ALoader.EVENT_BUS.post(new ClientItemUpdateEvent());
            ALoader.EVENT_BUS.post(new ClientRecipeUpdateEvent());
        }
    }

    public static void jeiStartedReload() {
        AClientRestrictionManager.didJeiFinishReloading = false;
    }

    public static void jeiFinishedReload() {
        AClientRestrictionManager.didJeiFinishReloading = true;

        if (AClientRestrictionManager.areScriptsAvailable) {
            isReloading = false;
        }

        ALoader.EVENT_BUS.post(new ClientItemUpdateEvent());
        ALoader.EVENT_BUS.post(new ClientRecipeUpdateEvent());
    }

    public static void reloadStarted() {
        AClientRestrictionManager.isReloading = true;
        AClientRestrictionManager.areScriptsAvailable = false;
        AClientRestrictionManager.didJeiFinishReloading = false;
    }

    public static boolean isReloadFinished() {
        return !AClientRestrictionManager.isReloading;
    }

    public static boolean areScriptsAvailable() {
        return areScriptsAvailable;
    }

    public static boolean didJeiFinishReloading() {
        return didJeiFinishReloading;
    }

    public static boolean ableToUpdateJeiUI() {
        return AClientRestrictionManager.areScriptsAvailable() && AClientRestrictionManager.didJeiFinishReloading() && AClientRestrictionManager.isReloadFinished();
    }

    public static void removeRestriction(String id, ARestrictionType type) {
        ASSOCIATION_MAP.get(type).removeRestriction(id);
    }
}
