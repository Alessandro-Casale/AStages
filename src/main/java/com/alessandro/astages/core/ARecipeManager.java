package com.alessandro.astages.core;

import com.alessandro.astages.capability.ClientPlayerStage;
import com.alessandro.astages.util.AManager;
import com.alessandro.astages.util.AStagesUtil;
import com.alessandro.astages.util.Info;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.Container;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraftforge.server.ServerLifecycleHooks;

import java.util.*;

public class ARecipeManager implements AManager<ARecipeRestriction, ARecipeManager.RecipeWrapper> {
    private final Map<String, List<ARecipeRestriction>> restrictions = new HashMap<>();

    public Map<String, List<ARecipeRestriction>> getRestrictions() {
        return restrictions;
    }

    @Override
    public void addRestriction(String stage, ARecipeRestriction restriction) {
        var newList = restrictions.getOrDefault(stage, new ArrayList<>());

        if (!newList.isEmpty()) { newList.removeIf(rest -> Objects.equals(rest.id, restriction.id)); }
        newList.add(restriction);

        ARestrictionManager.ALL_STAGES.add(stage);

        restrictions.put(stage, newList);
    }

    @Override
    public ARecipeRestriction getRestriction(String id) {
        for (String stage : restrictions.keySet()) {
            for (ARecipeRestriction restriction : restrictions.get(stage)) {
                if (restriction.id.equals(id)) {
                    return restriction;
                }
            }
        }

        return null;
    }

    @Override
    public ARecipeRestriction getRestriction(RecipeWrapper recipe) {
        for (String stage : restrictions.keySet()) {
            for (ARecipeRestriction restriction : restrictions.get(stage)) {
                if (restriction.isRestricted(recipe.type, recipe.recipe) && !ClientPlayerStage.getPlayerStages().contains(stage)) {
                    return restriction;
                }
            }
        }

        return null;
    }

    @Override
    public ARecipeRestriction getRestriction(Player player, RecipeWrapper recipe) {
        for (String stage : restrictions.keySet()) {
            for (ARecipeRestriction restriction : restrictions.get(stage)) {
                if (restriction.isRestricted(recipe.type, recipe.recipe) && !AStagesUtil.hasStage(player, stage)) {
                    return restriction;
                }
            }
        }

        return null;
    }

    public record RecipeWrapper(RecipeType<?> type, ResourceLocation recipe) { }
}
