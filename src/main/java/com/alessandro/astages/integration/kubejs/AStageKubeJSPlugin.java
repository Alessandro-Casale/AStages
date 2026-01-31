package com.alessandro.astages.integration.kubejs;

import com.alessandro.astages.AStages;
import com.alessandro.astages.api.ALoader;
import com.alessandro.astages.api.constant.ARestrictionStage;
import com.alessandro.astages.api.nullability.NotNullParams;
import com.alessandro.astages.api.time.ATime;
import com.alessandro.astages.core.ARestrictionManager;
import com.alessandro.astages.core.AStageManager;
import com.alessandro.astages.core.server.restriction.*;
import com.alessandro.astages.core.server.restriction.item.AItemRestriction;
import com.alessandro.astages.core.server.restriction.recipe.ARecipeRestriction;
import com.alessandro.astages.event.custom.ReloadScriptEvent;
import com.alessandro.astages.integration.Mods;
import com.alessandro.astages.integration.kubejs.util.KubeJSStageEventHandler;
import com.alessandro.astages.integration.kubejs.util.StageEvents;
import com.alessandro.astages.store.Attributes;
import dev.latvian.mods.kubejs.KubeJSPlugin;
import dev.latvian.mods.kubejs.script.BindingsEvent;
import dev.latvian.mods.kubejs.script.ScriptType;
import dev.latvian.mods.rhino.util.wrap.TypeWrappers;
import net.minecraftforge.eventbus.api.EventPriority;

@NotNullParams
public class AStageKubeJSPlugin extends KubeJSPlugin {
    static {
        if (Mods.KUBEJS.isLoaded()) {
            KubeJSStageEventHandler.init();

            ALoader.EVENT_BUS.addListener(EventPriority.NORMAL, false, ReloadScriptEvent.BeforeScriptsLoaded.class, e -> {
                if (e.getScriptType() == ReloadScriptEvent.EventScriptType.SERVER) {
                    AStageManager.reloadBeforeScripts();
                    ARestrictionManager.reloadBeforeScripts();
                    ARestrictionManager.addRestrictionsViaJavaCode(ARestrictionStage.BEFORE_JS);
                }
            });

            ALoader.EVENT_BUS.addListener(EventPriority.NORMAL, false, ReloadScriptEvent.AfterScriptsLoaded.class, e -> {
                if (e.getScriptType() == ReloadScriptEvent.EventScriptType.SERVER) {
                    ARestrictionManager.addRestrictionsViaJavaCode(ARestrictionStage.AFTER_JS);
                    AStageManager.reloadAfterScripts();
                    ARestrictionManager.reloadAfterScripts();
                }
            });
        }
    }

    @Override
    public void registerTypeWrappers(ScriptType type, TypeWrappers typeWrappers) {
        typeWrappers.registerSimple(ATime.class, ATime::of);
    }

    @Override
    public void registerBindings(BindingsEvent event) {
        if (!Mods.KUBEJS.isLoaded()) return;

        if (event.getType().isServer() || event.getType().isStartup()) {
            event.add("AStages", AStagesKubeJSUtil.class);
            event.add("AModels", AStagesModelJSUtil.class);
        }

        if (event.getType().isClient()) {
            event.add("AStagesClient", AStagesClientJSUtil.class);
            event.add("AClientModels", AStagesClientModelJSUtil.class);
        }

        event.add("ATime", ATime.class);

        event.add("Attributes", Attributes.class);
        event.add("ItemAttributes", Attributes.Item.class);
        // RecipeAttributes
        // CropAttributes
        event.add("DimensionAttributes", Attributes.Dimension.class);
        // EffectAttributes
        // EnchantAttributes
        // LootAttributes
        event.add("MobAttributes", Attributes.Mob.class);
        // OreAttributes
        event.add("PetAttributes", Attributes.Pet.class);
        event.add("RegionAttributes", Attributes.Region.class);
        event.add("ScreenAttributes", Attributes.Screen.class);
        event.add("StructureAttributes", Attributes.Structure.class);

        event.add("AItemRestriction", AItemRestriction.class);
        event.add("ARecipeRestriction", ARecipeRestriction.class);
        event.add("ACropRestriction", ACropRestriction.class);
        event.add("ADimensionRestriction", ADimensionRestriction.class);
        event.add("AEffectRestriction", AEffectRestriction.class);
        event.add("AEnchantRestriction", AEnchantRestriction.class);
        event.add("ALootRestriction", ALootRestriction.class);
        event.add("AMobRestriction", AMobRestriction.class);
        event.add("AOreRestriction", AOreRestriction.class);
        event.add("APetRestriction", APetRestriction.class);
        event.add("ARegionRestriction", ARegionRestriction.class);
        event.add("AScreenRestriction", AScreenRestriction.class);
        event.add("AStructureRestriction", AStructureRestriction.class);
    }

    @Override
    public void registerEvents() {
        if (!Mods.KUBEJS.isLoaded()) return;

        StageEvents.GROUP.register();
    }

    @Override
    public void init() {
        if (!Mods.KUBEJS.isLoaded()) return;

        AStages.LOGGER.debug("ASTAGES-KUBEJS: INITIALIZED PLUGIN!");
    }
}
