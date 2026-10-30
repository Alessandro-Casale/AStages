package com.alessandro.astages.engine.client.evaluator;

import com.alessandro.astages.api.cache.RecipeTypeCache;
import com.alessandro.astages.api.cache.ResourceLocationCache;
import com.alessandro.astages.api.nullability.NotNullParamsAndMethodsReturn;
import com.alessandro.astages.api.nullability.Nullable;
import com.alessandro.astages.api.stage.AStageContext;
import com.alessandro.astages.api.util.AEvaluatorUtils;
import com.alessandro.astages.api.wrapper.RecipeWrapper;
import com.alessandro.astages.engine.client.registry.AClientRecipeRegistry;
import com.alessandro.astages.engine.client.restriction.recipe.AClientBaseRecipeRestriction;
import com.alessandro.astages.engine.client.restriction.recipe.AClientRecipeModRestriction;
import com.alessandro.astages.engine.client.restriction.recipe.AClientRecipeRestriction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.crafting.RecipeType;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

@NotNullParamsAndMethodsReturn
public record AClientRecipeEvaluator(AClientRecipeRegistry registry) {
    public @Nullable AClientBaseRecipeRestriction<?, ?, ?> evaluateCache(ResourceLocationCache<AClientRecipeRestriction> cache, AStageContext context, RecipeWrapper wrapper) {
        //noinspection RedundantTypeArguments
        return AEvaluatorUtils.firstNonNull(
            () -> AEvaluatorUtils.<AClientRecipeModRestriction, RecipeWrapper>evaluateRestriction(registry.getModRestrictions(), AEvaluatorUtils.calculateEvaluationMode(registry), context, wrapper),
            () -> cache.find(context, wrapper.recipe())
        );
        // TODO: check divergent implementation server-client!
    }

    public Map<String, Set<ResourceLocation>> evaluateRecipes(RecipeTypeCache<AClientRecipeRestriction> cache, RecipeType<?> type) {
        var toReturn = new HashMap<String, Set<ResourceLocation>>();

        for (var restriction : cache.get(type)) {
            toReturn.computeIfAbsent(restriction.getStage(), m -> new HashSet<>()).addAll(restriction.getRecipes());
        }

        return toReturn;
    }

    public boolean hasRestrictionsFor(Set<String> stages) {
        return AEvaluatorUtils.hasAtLeastOneRestrictionForStages(registry.getModRestrictions(), stages) ||
            AEvaluatorUtils.hasAtLeastOneRestrictionForStages(registry.getRecipeRestrictions(), stages);
    }
}