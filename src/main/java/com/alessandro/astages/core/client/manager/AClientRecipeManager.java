package com.alessandro.astages.core.client.manager;

import com.alessandro.astages.api.AStagesClientUtils;
import com.alessandro.astages.api.base.OrderedMultiMap;
import com.alessandro.astages.api.constant.AStageType;
import com.alessandro.astages.api.develop.Info;
import com.alessandro.astages.api.holder.AClientHolder;
import com.alessandro.astages.api.nullability.NotNullParams;
import com.alessandro.astages.core.client.restriction.recipe.AClientBaseRecipeRestriction;
import com.alessandro.astages.core.client.restriction.recipe.AClientRecipeModRestriction;
import com.alessandro.astages.core.client.restriction.recipe.AClientRecipeRestriction;
import com.alessandro.astages.core.wrapper.RecipeWrapper;
import com.alessandro.astages.store.ARestrictionType;
import com.alessandro.astages.store.ARestrictionTypes;
import com.alessandro.astages.store.client.AClientMinimalManager;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.crafting.RecipeType;

import java.util.*;

@NotNullParams
public class AClientRecipeManager implements AClientMinimalManager<AClientBaseRecipeRestriction<?, ?, ?>> {
    public final List<AClientBaseRecipeRestriction<?, ?, ?>> restrictions = new ArrayList<>();
    private final Map<String, AClientBaseRecipeRestriction<?, ?, ?>> IDS = new HashMap<>();

    // Used for JEI/REI recipe hiding: contains all recipes TO HIDE!
    // private final List<AClientRecipeRestriction> recipes = new ArrayList<>();
    @Info("Different from server!") public final OrderedMultiMap<RecipeType<?>, AClientRecipeRestriction> RECIPE_TYPE_CACHE = OrderedMultiMap.create();
    public final List<AClientRecipeModRestriction> mods = new ArrayList<>();

    public void reloadBeforeScripts() {
        restrictions.clear();
        IDS.clear();

        // recipes.clear();
        RECIPE_TYPE_CACHE.clear();
        mods.clear();
    }

    public void addRestriction(AClientRecipeRestriction restriction) {
        commonAddOperations(restriction);
        RECIPE_TYPE_CACHE.put(restriction.getType(), restriction);
    }

    public void addRestriction(AClientRecipeModRestriction restriction) {
        commonAddOperations(restriction);
        mods.add(restriction);
    }

    private void commonAddOperations(AClientBaseRecipeRestriction<?, ?, ?> restriction) {
        IDS.put(restriction.getId(), restriction);
        restrictions.add(restriction);
    }

    @Override
    public AClientBaseRecipeRestriction<?, ?, ?> getRestriction(String id) {
        return IDS.getOrDefault(id, null);
    }

    public AClientBaseRecipeRestriction<?, ?, ?> getRestriction(AClientHolder holder, RecipeWrapper wrapper) {
        var serverModRestriction = mods.stream().filter(r -> r.isRestricted(wrapper) && !AStagesClientUtils.hasStage(holder, AStageType.SERVER, r.getStage())).findFirst().orElse(null);
        if (serverModRestriction == null) { return null; }
        var modRestriction = mods.stream().filter(r -> r.isRestricted(wrapper) && !AStagesClientUtils.hasStage(holder, AStageType.PLAYER, r.getStage())).findFirst().orElse(null);
        if (modRestriction != null) { return modRestriction; }

        var restrictions = RECIPE_TYPE_CACHE.get(wrapper.type());
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

    public Map<String, Set<ResourceLocation>> getAllRecipesForType(RecipeType<?> type) {
        var toReturn = new HashMap<String, Set<ResourceLocation>>();

        for (var restriction : RECIPE_TYPE_CACHE.get(type)) {
            toReturn.computeIfAbsent(restriction.getStage(), m -> new HashSet<>()).addAll(restriction.getRecipes());
        }

        return toReturn;
    }

    @Override
    public void removeRestriction(String id) {
        restrictions.removeIf(restriction -> restriction.getId().equals(id));
        // recipes.removeIf(restriction -> restriction.getId().equals(id));
        mods.removeIf(restriction -> restriction.getId().equals(id));
        RECIPE_TYPE_CACHE.removeValues(restriction -> restriction.getId().equals(id));
        IDS.remove(id);
    }

    @Override
    public ARestrictionType associatedType() {
        return ARestrictionTypes.RECIPE;
    }
}
