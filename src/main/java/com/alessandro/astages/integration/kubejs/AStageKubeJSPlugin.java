package com.alessandro.astages.integration.kubejs;

import com.alessandro.astages.AStages;
import com.alessandro.astages.api.constant.ARestrictionStage;
import com.alessandro.astages.api.nullability.NotNullParams;
import com.alessandro.astages.api.time.ATime;
import com.alessandro.astages.core.ARestrictionManager;
import com.alessandro.astages.core.AStageManager;
import com.alessandro.astages.core.server.restriction.*;
import com.alessandro.astages.core.server.restriction.item.AItemRestriction;
import com.alessandro.astages.core.server.restriction.recipe.ARecipeRestriction;
import com.alessandro.astages.integration.Mods;
import com.alessandro.astages.integration.kubejs.util.KubeJSStageEventHandler;
import com.alessandro.astages.integration.kubejs.util.StageEvents;
import com.alessandro.astages.store.Attributes;
import dev.latvian.mods.kubejs.event.EventGroupRegistry;
import dev.latvian.mods.kubejs.plugin.KubeJSPlugin;
import dev.latvian.mods.kubejs.script.BindingRegistry;
import dev.latvian.mods.kubejs.script.ScriptManager;
import dev.latvian.mods.kubejs.script.ScriptType;
import dev.latvian.mods.kubejs.script.TypeWrapperRegistry;

@NotNullParams
public class AStageKubeJSPlugin implements KubeJSPlugin {
    static {
        if (Mods.KUBEJS.isLoaded()) {
            KubeJSStageEventHandler.init();
        }
    }

    @Override
    public void beforeScriptsLoaded(ScriptManager manager) {
        if (manager.scriptType == ScriptType.CLIENT) {
            AStageManager.reloadBeforeScripts();
            ARestrictionManager.reloadBeforeScripts();
            ARestrictionManager.addRestrictionsViaJavaCode(ARestrictionStage.BEFORE_JS);
        }
    }

    @Override
    public void afterScriptsLoaded(ScriptManager manager) {
        if (manager.scriptType == ScriptType.SERVER) {
            ARestrictionManager.addRestrictionsViaJavaCode(ARestrictionStage.AFTER_JS);
            AStageManager.reloadAfterScripts();
            ARestrictionManager.reloadAfterScripts();
        }
    }

    @Override
    public void registerTypeWrappers(TypeWrapperRegistry registry) {
        registry.register(ATime.class, (TypeWrapperRegistry.ContextFromFunction<ATime>) (context, object) -> ATime.of(object));
    }

    @Override
    public void registerBindings(BindingRegistry bindings) {
        if (!Mods.KUBEJS.isLoaded()) return;

        if (bindings.type().isServer() || bindings.type().isStartup()) {
            bindings.add("AStages", AStagesKubeJSUtil.class);
        }

        if (bindings.type().isClient()) {
            bindings.add("AStagesClient", AStagesClientJSUtil.class);
        }

        bindings.add("AModels", AStagesModelJSUtil.class);
        bindings.add("ATime", ATime.class);

        bindings.add("Attributes", Attributes.class);
        bindings.add("ItemAttributes", Attributes.Item.class);
        // RecipeAttributes
        // CropAttributes
        bindings.add("DimensionAttributes", Attributes.Dimension.class);
        // EffectAttributes
        // EnchantAttributes
        // LootAttributes
        bindings.add("MobAttributes", Attributes.Mob.class);
        // OreAttributes
        bindings.add("PetAttributes", Attributes.Pet.class);
        bindings.add("RegionAttributes", Attributes.Region.class);
        bindings.add("ScreenAttributes", Attributes.Screen.class);
        bindings.add("StructureAttributes", Attributes.Structure.class);

        bindings.add("AItemRestriction", AItemRestriction.class);
        bindings.add("ARecipeRestriction", ARecipeRestriction.class);
        bindings.add("ACropRestriction", ACropRestriction.class);
        bindings.add("ADimensionRestriction", ADimensionRestriction.class);
        bindings.add("AEffectRestriction", AEffectRestriction.class);
        bindings.add("AEnchantRestriction", AEnchantRestriction.class);
        bindings.add("ALootRestriction", ALootRestriction.class);
        bindings.add("AMobRestriction", AMobRestriction.class);
        bindings.add("AOreRestriction", AOreRestriction.class);
        bindings.add("APetRestriction", APetRestriction.class);
        bindings.add("ARegionRestriction", ARegionRestriction.class);
        bindings.add("AScreenRestriction", AScreenRestriction.class);
        bindings.add("AStructureRestriction", AStructureRestriction.class);
    }

    @Override
    public void registerEvents(EventGroupRegistry registry) {
        if (!Mods.KUBEJS.isLoaded()) return;

        registry.register(StageEvents.GROUP);
    }

    @Override
    public void init() {
        if (!Mods.KUBEJS.isLoaded()) return;

        AStages.LOGGER.debug("ASTAGES-KUBEJS: INITIALIZED PLUGIN!");
    }
}
