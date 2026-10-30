package com.alessandro.astages.engine.server.evaluator;

import com.alessandro.astages.api.cache.ResourceLocationCache;
import com.alessandro.astages.api.cache.StringCache;
import com.alessandro.astages.api.nullability.NotNullParamsAndMethodsReturn;
import com.alessandro.astages.api.nullability.Nullable;
import com.alessandro.astages.api.stage.AStageContext;
import com.alessandro.astages.api.util.AEvaluatorUtils;
import com.alessandro.astages.api.wrapper.RecipeWrapper;
import com.alessandro.astages.engine.server.registry.ARecipeRegistry;
import com.alessandro.astages.engine.server.restriction.recipe.ABaseRecipeRestriction;
import com.alessandro.astages.engine.server.restriction.recipe.ARecipeModRestriction;
import com.alessandro.astages.engine.server.restriction.recipe.ARecipeRestriction;

@NotNullParamsAndMethodsReturn
public record ARecipeEvaluator(ARecipeRegistry registry) {
    public @Nullable ABaseRecipeRestriction<?, ?, ?> evaluateCaches(ResourceLocationCache<ARecipeRestriction> recipeCache, StringCache<ARecipeModRestriction> recipeModCache, AStageContext context, RecipeWrapper wrapper) {
        return AEvaluatorUtils.evaluateRestrictionFromMultipleSources(
            AEvaluatorUtils.calculateEvaluationMode(registry), context,
            r -> r.isRestricted(wrapper),
            registry::getModRestrictions,
            () -> recipeModCache.get(wrapper.recipe().getNamespace()),
            () -> recipeCache.get(wrapper.recipe())
        );
    }
}