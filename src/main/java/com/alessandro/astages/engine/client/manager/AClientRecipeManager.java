package com.alessandro.astages.engine.client.manager;

import com.alessandro.astages.api.cache.client.RecipeTypeClientCache;
import com.alessandro.astages.api.cache.client.ResourceLocationClientCache;
import com.alessandro.astages.api.develop.Info;
import com.alessandro.astages.api.holder.AClientHolder;
import com.alessandro.astages.api.manager.AClientMinimalManager;
import com.alessandro.astages.api.nullability.NotNullParams;
import com.alessandro.astages.api.store.ARestrictionType;
import com.alessandro.astages.api.wrapper.RecipeWrapper;
import com.alessandro.astages.engine.client.evaluator.AClientRecipeEvaluator;
import com.alessandro.astages.engine.client.registry.AClientRecipeRegistry;
import com.alessandro.astages.engine.client.restriction.recipe.AClientBaseRecipeRestriction;
import com.alessandro.astages.engine.client.restriction.recipe.AClientRecipeModRestriction;
import com.alessandro.astages.engine.client.restriction.recipe.AClientRecipeRestriction;
import com.alessandro.astages.engine.store.ARestrictionTypes;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.crafting.RecipeType;

import java.util.Map;
import java.util.Set;

@NotNullParams
public class AClientRecipeManager implements AClientMinimalManager<AClientBaseRecipeRestriction<?, ?, ?>, RecipeWrapper> {
    private final AClientRecipeRegistry registry = new AClientRecipeRegistry();
    private final AClientRecipeEvaluator evaluator = new AClientRecipeEvaluator(registry);

    @Info("Different from server!")
    private final RecipeTypeClientCache<AClientRecipeRestriction> recipeTypeCache = new RecipeTypeClientCache<>(this) {
        @Override
        public void index(AClientRecipeRestriction restriction) {
            add(restriction.getType(), restriction);
        }
    };

    private final ResourceLocationClientCache<AClientRecipeRestriction> recipeCache = new ResourceLocationClientCache<>(this) {
        @Override
        public void index(AClientRecipeRestriction restriction) {
            for (var recipe : restriction.getRecipes()) {
                add(recipe, restriction);
            }
        }
    };

    @Override
    public AClientBaseRecipeRestriction<?, ?, ?> getRestriction(String id) {
        return registry.getById(id);
    }

    @Override
    public AClientBaseRecipeRestriction<?, ?, ?> getRestriction(AClientHolder holder, RecipeWrapper wrapper) {
        return evaluator.evaluateCache(recipeCache, holder, wrapper);
    }

    public Map<String, Set<ResourceLocation>> getAllRecipesForType(RecipeType<?> type) {
        return evaluator.evaluateRecipes(recipeTypeCache, type);
    }

    public boolean hasRestrictionsFor(Set<String> stages) {
        return evaluator.hasRestrictionsFor(stages);
    }

    public void addRestriction(AClientRecipeRestriction restriction) {
        registry.register(restriction);
        recipeCache.index(restriction);
        recipeTypeCache.index(restriction);
    }

    public void addRestriction(AClientRecipeModRestriction restriction) {
        registry.register(restriction);
    }

    @Override
    public void removeRestriction(String id) {
        var restriction = registry.remove(id);

        if (restriction != null) {
            if (restriction instanceof AClientRecipeRestriction r) {
                recipeCache.remove(r);
                recipeTypeCache.remove(r);
            }
        }
    }

    @Override
    public void onReloadStarted() {
        registry.clear();
        recipeCache.clear();
        recipeTypeCache.clear();
    }

    @Override
    public void onReloadFinished() { }

    @Override
    public AClientRecipeRegistry getRegistry() {
        return registry;
    }

    @Override
    public ARestrictionType associatedType() {
        return ARestrictionTypes.RECIPE;
    }
}