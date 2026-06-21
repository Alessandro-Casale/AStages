package com.alessandro.astages.infrastructure.integration.kubejs;

import com.alessandro.astages.AStages;
import com.alessandro.astages.api.nullability.NotNullParams;
import com.alessandro.astages.api.time.ATime;
import com.alessandro.astages.engine.server.restriction.*;
import com.alessandro.astages.engine.server.restriction.item.*;
import com.alessandro.astages.engine.server.restriction.recipe.ABaseRecipeRestriction;
import com.alessandro.astages.engine.server.restriction.recipe.ARecipeModRestriction;
import com.alessandro.astages.engine.server.restriction.recipe.ARecipeRestriction;
import com.alessandro.astages.engine.store.ARestrictionTypes;
import com.alessandro.astages.engine.store.Attributes;
import com.alessandro.astages.engine.store.StageAttributes;
import com.alessandro.astages.infrastructure.integration.Mods;
import com.alessandro.astages.infrastructure.integration.kubejs.bridge.KubeJSEventBridge;
import com.alessandro.astages.infrastructure.integration.kubejs.bridge.KubeJSStageEvents;
import com.alessandro.astages.infrastructure.integration.kubejs.util.KubeJSClientModelUtils;
import com.alessandro.astages.infrastructure.integration.kubejs.util.KubeJSClientUtils;
import com.alessandro.astages.infrastructure.integration.kubejs.util.KubeJSModelUtils;
import com.alessandro.astages.infrastructure.integration.kubejs.util.KubeJSServerUtils;
import dev.latvian.mods.kubejs.event.EventGroupRegistry;
import dev.latvian.mods.kubejs.plugin.KubeJSPlugin;
import dev.latvian.mods.kubejs.script.BindingRegistry;
import dev.latvian.mods.kubejs.script.TypeWrapperRegistry;

@NotNullParams
public class AKubeJSPlugin implements KubeJSPlugin {
    static {
        if (Mods.KUBEJS.isLoaded()) {
            KubeJSEventBridge.init();
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
            bindings.add("AStages", KubeJSServerUtils.class);
            bindings.add("AModels", KubeJSModelUtils.class);
        }

        if (bindings.type().isClient()) {
            bindings.add("AStagesClient", KubeJSClientUtils.class);
            bindings.add("AModels", KubeJSClientModelUtils.class);
        }

        bindings.add("ATime", ATime.class);
        bindings.add("ARestrictionTypes", ARestrictionTypes.class);

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
        bindings.add("StageAttributes", StageAttributes.class);

        bindings.add("ABaseItemRestriction", ABaseItemRestriction.class);
        bindings.add("AItemRestriction", AItemRestriction.class);
        bindings.add("AItemModRestriction", AItemModRestriction.class);
        bindings.add("AItemTagRestriction", AItemTagRestriction.class);
        bindings.add("AItemPredicateRestriction", AItemPredicateRestriction.class);

        bindings.add("ABaseRecipeRestriction", ABaseRecipeRestriction.class);
        bindings.add("ARecipeRestriction", ARecipeRestriction.class);
        bindings.add("ARecipeModRestriction", ARecipeModRestriction.class);

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
        registry.register(KubeJSStageEvents.GROUP);
    }

    @Override
    public void init() {
        AStages.LOGGER.debug("ASTAGES-KUBEJS: INITIALIZED PLUGIN!");
    }
}
