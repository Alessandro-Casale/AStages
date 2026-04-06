package com.alessandro.astages.engine.server.evaluator;

import com.alessandro.astages.api.constant.AStageType;
import com.alessandro.astages.api.holder.AHolder;
import com.alessandro.astages.api.nullability.NotNullParams;
import com.alessandro.astages.api.nullability.Nullable;
import com.alessandro.astages.api.cache.server.ResourceLocationCache;
import com.alessandro.astages.api.util.AStagesUtils;
import com.alessandro.astages.api.wrapper.RecipeWrapper;
import com.alessandro.astages.engine.server.registry.ARecipeRegistry;
import com.alessandro.astages.engine.server.restriction.recipe.ABaseRecipeRestriction;
import com.alessandro.astages.engine.server.restriction.recipe.ARecipeRestriction;

@NotNullParams
public record ARecipeEvaluator(ARecipeRegistry registry) {
    public @Nullable ABaseRecipeRestriction<?, ?, ?> evaluateCache(ResourceLocationCache<ARecipeRestriction> cache, AHolder holder, RecipeWrapper wrapper) {
        if (holder.isServerActive()) {
            var serverRestriction = evaluateCache(cache, holder, AStageType.SERVER, wrapper);

            if (serverRestriction == null) { return null; } // If the stage is unlocked in the server, pass!
        }

        if (holder.isPlayerActive()) {
            return evaluateCache(cache, holder, AStageType.PLAYER, wrapper);
        }

        return null;
    }

    private @Nullable ABaseRecipeRestriction<?, ?, ?> evaluateCache(ResourceLocationCache<ARecipeRestriction> cache,
                                                                    AHolder holder, AStageType type, RecipeWrapper wrapper) {
        var modRestriction = registry.getModRestrictions().stream()
            .filter(r -> r.isRestricted(wrapper) && !AStagesUtils.hasStage(holder, type, r.getStage()))
            .findFirst()
            .orElse(null);

        if (modRestriction != null) { return modRestriction; }

        return cache.find(holder, wrapper.recipe());
    }
}