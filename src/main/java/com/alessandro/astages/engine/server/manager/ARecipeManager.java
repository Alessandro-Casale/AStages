package com.alessandro.astages.engine.server.manager;

import com.alessandro.astages.api.feature.ClientSynchronizable;
import com.alessandro.astages.api.holder.AHolder;
import com.alessandro.astages.api.nullability.NotNullParams;
import com.alessandro.astages.api.nullability.Nullable;
import com.alessandro.astages.api.cache.server.ResourceLocationCache;
import com.alessandro.astages.api.wrapper.RecipeWrapper;
import com.alessandro.astages.engine.server.evaluator.ARecipeEvaluator;
import com.alessandro.astages.engine.server.registry.ARecipeRegistry;
import com.alessandro.astages.engine.server.restriction.recipe.ABaseRecipeRestriction;
import com.alessandro.astages.engine.server.restriction.recipe.ARecipeModRestriction;
import com.alessandro.astages.engine.server.restriction.recipe.ARecipeRestriction;
import com.alessandro.astages.infrastructure.networking.Networking;
import com.alessandro.astages.infrastructure.networking.packet.recipe.SyncRecipeModS2C;
import com.alessandro.astages.infrastructure.networking.packet.recipe.SyncRecipeS2C;
import com.alessandro.astages.infrastructure.networking.packet.reload.RequestRestrictionDeleteS2C;
import com.alessandro.astages.api.store.ARestrictionType;
import com.alessandro.astages.engine.store.ARestrictionTypes;
import com.alessandro.astages.api.manager.AMinimalManager;
import net.minecraft.server.level.ServerPlayer;

@NotNullParams
public class ARecipeManager implements AMinimalManager<ABaseRecipeRestriction<?, ?, ?>, RecipeWrapper>, ClientSynchronizable {
    private final ARecipeRegistry registry = new ARecipeRegistry();
    private final ARecipeEvaluator evaluator = new ARecipeEvaluator(registry);

    private final ResourceLocationCache<ARecipeRestriction> recipeCache = new ResourceLocationCache<>() {
        @Override
        public void index(ARecipeRestriction restriction) {
            for (var recipe : restriction.getRecipes()) {
                add(recipe, restriction);
            }
        }
    };

    @Override
    public ABaseRecipeRestriction<?, ?, ?> getRestriction(String id) {
        return registry.getById(id);
    }

    @Override
    public ABaseRecipeRestriction<?, ?, ?> getRestriction(AHolder holder, RecipeWrapper wrapper) {
        return evaluator.evaluateCache(recipeCache, holder, wrapper);
    }

    public void addRestriction(ARecipeRestriction restriction) {
        if (registry.register(restriction)) {
            recipeCache.index(restriction);
        }
    }

    public void addRestriction(ARecipeModRestriction restriction) {
        registry.register(restriction);
    }

    @Override
    public void removeRestriction(String id) {
        var restriction = registry.remove(id);

        if (restriction != null) {
            if (restriction instanceof ARecipeRestriction r) { recipeCache.remove(r); }
        }

        Networking.sendTo(null, new RequestRestrictionDeleteS2C(id, associatedType()));
    }

    @Override
    public void onReloadStarted() {
        registry.clear();
        recipeCache.clear();
    }

    @Override
    public void onReloadFinished() { }

    @Override
    public ARecipeRegistry getRegistry() {
        return registry;
    }

    @Override
    public void synchronizeWithClient(@Nullable ServerPlayer player) {
        getRegistry().getRecipeRestrictions()
            .forEach(restriction -> Networking.sendTo(player, new SyncRecipeS2C(restriction)));

        getRegistry().getModRestrictions()
            .forEach(restriction -> Networking.sendTo(player, new SyncRecipeModS2C(restriction)));
    }

    @Override
    public ARestrictionType associatedType() {
        return ARestrictionTypes.RECIPE;
    }
}