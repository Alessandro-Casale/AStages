package com.alessandro.astages.engine;

import com.alessandro.astages.api.manager.AMinimalManager;
import com.alessandro.astages.api.manager.AManagerContainer;
import com.alessandro.astages.api.manager.ManagerInstance;
import com.alessandro.astages.api.nullability.Nullable;
import com.alessandro.astages.api.plugin.ForPlugins;
import com.alessandro.astages.api.store.ARestrictionType;
import com.alessandro.astages.api.store.container.AttributeStore;
import com.alessandro.astages.engine.server.RestrictionLifecycleService;
import com.alessandro.astages.engine.server.RestrictionRegistry;
import com.alessandro.astages.engine.server.manager.*;

import java.util.HashMap;
import java.util.Map;

@AManagerContainer
public class ARestrictionManager {
    // ----------------------------------------
    //            MANAGER INSTANCES
    // ----------------------------------------
    @ManagerInstance public static final AItemManager ITEM_INSTANCE = new AItemManager(); // Done!
    @ManagerInstance public static final ADimensionManager DIMENSION_INSTANCE = new ADimensionManager(); // Done!
    @ManagerInstance public static final AMobManager MOB_INSTANCE = new AMobManager(); // Done!
    @ManagerInstance public static final AStructureManager STRUCTURE_INSTANCE = new AStructureManager(); // Done!
    @ManagerInstance public static final ARecipeManager RECIPE_INSTANCE = new ARecipeManager(); // Done!
    @ManagerInstance public static final AScreenManager SCREEN_INSTANCE = new AScreenManager(); // Done!
    @ManagerInstance public static final AOreManager ORE_INSTANCE = new AOreManager(); // Implement Server Stages
    @ManagerInstance public static final APetManager PET_INSTANCE = new APetManager(); // Done!
    @ManagerInstance public static final AEnchantManager ENCHANT_INSTANCE = new AEnchantManager(); // Done!
    @ManagerInstance public static final ACropManager CROP_INSTANCE = new ACropManager();  // Done!
    @ManagerInstance public static final AEffectManager EFFECT_INSTANCE = new AEffectManager(); // Done!
    @ManagerInstance public static final ARegionManager REGION_INSTANCE = new ARegionManager(); // Done!
    @ManagerInstance public static final ALootManager LOOT_INSTANCE = new ALootManager(); // Done!

    // ----------------------------------------
    //            PLUGIN EXTENSIONS
    // ----------------------------------------
    @ForPlugins public static final Map<Class<?>, AttributeStore> ATTACHED_ATTRIBUTES = new HashMap<>();

    // ----------------------------------------
    //            REGISTRY ACCESS
    // ----------------------------------------
    public static void removeRestriction(String id, ARestrictionType type) {
        RestrictionRegistry.removeRestriction(id, type);
    }

    public static <T> @Nullable T getRestrictionById(String id, ARestrictionType type) {
        return RestrictionRegistry.getRestriction(id, type);
    }

    public static AMinimalManager<?, ?> getManagerFromType(ARestrictionType type) {
        return RestrictionRegistry.getManager(type);
    }

    // ----------------------------------------
    //                LIFECYCLE
    // ----------------------------------------
    public static void reloadBeforeScripts() {
        RestrictionLifecycleService.reloadBeforeScripts();
    }

    public static void reloadAfterScripts() {
        RestrictionLifecycleService.reloadAfterScripts();
    }
}