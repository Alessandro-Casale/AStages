package com.alessandro.astages.core.client.restriction.recipe;

import com.alessandro.astages.core.wrapper.RecipeModWrapper;
import com.alessandro.astages.core.wrapper.RecipeWrapper;
import com.alessandro.astages.api.nullability.NotNullParams;

@NotNullParams
public class AClientRecipeModRestriction extends AClientBaseRecipeRestriction<AClientRecipeModRestriction, RecipeModWrapper, RecipeWrapper> {
    private String modId = null;

    public AClientRecipeModRestriction(String id, String stage) {
        super(id, stage);
    }

    @Override
    public AClientRecipeModRestriction restrict(RecipeModWrapper wrapper) {
        modId = wrapper.modId();
        return this;
    }

    @Override
    public boolean isRestricted(RecipeWrapper wrapper) {
        return modId.equals(wrapper.recipe().getNamespace());
    }

    public String getModId() {
        return modId;
    }
}
