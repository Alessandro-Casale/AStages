package com.alessandro.astages.core;

import com.alessandro.astages.util.ARestriction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.crafting.RecipeType;

import java.util.ArrayList;
import java.util.List;

public class ARecipeRestriction implements ARestriction {
    public String id;

    public RecipeType<?> type;
    public List<ResourceLocation> recipes = new ArrayList<>();

    public ARecipeRestriction(String id) {
        this.id = id;
    }

    public ARecipeRestriction restrict(ResourceLocation recipe) {
        recipes.add(recipe);

        return this;
    }

    public boolean isRestricted(RecipeType<?> type, ResourceLocation recipe) {
        if (this.type != type) { return false; }

        for (ResourceLocation rec : recipes) {
            if (rec.equals(recipe)) {
                return true;
            }
        }

        return false;
    }

    public RecipeType<?> getType() {
        return type;
    }

    public ARecipeRestriction setType(RecipeType<?> type) {
        this.type = type;

        return this;
    }
}
