package com.alessandro.astages.engine.client.evaluator;

import com.alessandro.astages.api.cache.client.RecipeTypeClientCache;
import com.alessandro.astages.api.constant.AStageType;
import com.alessandro.astages.api.holder.AClientHolder;
import com.alessandro.astages.api.nullability.NotNullParamsAndMethodsReturn;
import com.alessandro.astages.api.nullability.Nullable;
import com.alessandro.astages.api.util.AStagesClientUtils;
import com.alessandro.astages.api.wrapper.RecipeWrapper;
import com.alessandro.astages.engine.client.registry.AClientRecipeRegistry;
import com.alessandro.astages.engine.client.restriction.recipe.AClientBaseRecipeRestriction;
import com.alessandro.astages.engine.client.restriction.recipe.AClientRecipeRestriction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.crafting.RecipeType;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

@NotNullParamsAndMethodsReturn
public record AClientRecipeEvaluator(AClientRecipeRegistry registry) {
    public @Nullable AClientBaseRecipeRestriction<?, ?, ?> evaluateCache(RecipeTypeClientCache<AClientRecipeRestriction> cache, AClientHolder holder, RecipeWrapper wrapper) {
        var serverModRestriction = registry.getModRestrictions().stream().filter(r -> r.isRestricted(wrapper) && !AStagesClientUtils.hasStage(holder, AStageType.SERVER, r.getStage())).findFirst().orElse(null);
        if (serverModRestriction == null) { return null; }
        var modRestriction = registry.getModRestrictions().stream().filter(r -> r.isRestricted(wrapper) && !AStagesClientUtils.hasStage(holder, AStageType.PLAYER, r.getStage())).findFirst().orElse(null);
        if (modRestriction != null) { return modRestriction; }

        var restrictions = cache.get(wrapper.type());
        for (var restriction : restrictions) {
            if (restriction.getRecipes().contains(wrapper.recipe()) && AStagesClientUtils.hasStage(holder, AStageType.SERVER, restriction.getStage())) {
                return null;
            }
        }

        for (var restriction : restrictions) {
            if (restriction.getRecipes().contains(wrapper.recipe()) && !AStagesClientUtils.hasStage(holder, AStageType.PLAYER, restriction.getStage())) {
                return restriction;
            }
        }

        return null;
    }

    public Map<String, Set<ResourceLocation>> evaluateRecipes(RecipeTypeClientCache<AClientRecipeRestriction> cache, RecipeType<?> type) {
        var toReturn = new HashMap<String, Set<ResourceLocation>>();

        for (var restriction : cache.get(type)) {
            toReturn.computeIfAbsent(restriction.getStage(), m -> new HashSet<>()).addAll(restriction.getRecipes());
        }

        return toReturn;
    }

    public boolean hasRestrictionsFor(Set<String> stages) {
        for (var restriction : registry.getModRestrictions()) {
            if (stages.contains(restriction.getStage())) { return true; }
        }

        for (var restriction : registry.getRecipeRestrictions()) {
            if (stages.contains(restriction.getStage())) { return true; }
        }

        return false;
    }
}