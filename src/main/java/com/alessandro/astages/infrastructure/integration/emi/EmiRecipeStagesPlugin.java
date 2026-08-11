package com.alessandro.astages.infrastructure.integration.emi;

import com.alessandro.astages.api.constant.AOperation;
import com.alessandro.astages.api.holder.AClientHolder;
import com.alessandro.astages.api.nullability.NotNullParams;
import com.alessandro.astages.api.wrapper.RecipeWrapper;
import com.alessandro.astages.engine.AClientRestrictionManager;
import dev.emi.emi.api.EmiEntrypoint;
import dev.emi.emi.api.EmiPlugin;
import dev.emi.emi.api.EmiRegistry;
import dev.emi.emi.api.recipe.EmiRecipe;

import java.util.Set;

@NotNullParams
@EmiEntrypoint
public class EmiRecipeStagesPlugin implements EmiPlugin {
    @Override
    public void register(EmiRegistry registry) {
        registry.removeRecipes(this::hideRecipes);
    }

    private boolean hideRecipes(EmiRecipe recipe) {
        var holder = recipe.getBackingRecipe();
        if (holder == null) { return false; }

        var wrapper = new RecipeWrapper(holder.value().getType(), holder.id());
        var restriction = AClientRestrictionManager.RECIPE_INSTANCE.getRestriction(AClientHolder.serverAndPlayer(), wrapper);

        return restriction != null;
    }

    public static void onReloadStarted() { }

    public static void onReloadFinished() { }

    public static void onStageChanged(AOperation operation, Set<String> syncedStages) {
        if (!AClientRestrictionManager.RECIPE_INSTANCE.hasRestrictionsFor(syncedStages)) { return; }

    }
}
