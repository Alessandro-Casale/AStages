package com.alessandro.astages.core.client.restriction.recipe;

import com.alessandro.astages.core.wrapper.RecipeWrapper;
import com.alessandro.astages.api.nullability.NotNullParams;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.crafting.RecipeType;

import java.util.ArrayList;
import java.util.List;

@NotNullParams
public class AClientRecipeRestriction extends AClientBaseRecipeRestriction<AClientRecipeRestriction, RecipeWrapper, RecipeWrapper> {
    private RecipeType<?> type = null;
    private final List<ResourceLocation> recipes = new ArrayList<>();


    public AClientRecipeRestriction(String id, String stage) {
        super(id, stage);
    }

    @Override
    public AClientRecipeRestriction restrict(RecipeWrapper wrapper) {
        this.type = wrapper.type();
        this.recipes.add(wrapper.recipe());
        return this;
    }

    @Override
    public boolean isRestricted(RecipeWrapper wrapper) {
        return type == wrapper.type() && recipes.contains(wrapper.recipe());
    }

    public RecipeType<?> getType() {
        return type;
    }

    public List<ResourceLocation> getRecipes() {
        return recipes;
    }
}
