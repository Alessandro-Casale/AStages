package com.alessandro.astages.engine.client.restriction.recipe;

import com.alessandro.astages.api.nullability.NotNull;
import com.alessandro.astages.api.nullability.NotNullParams;
import com.alessandro.astages.engine.AClientRestrictionManager;
import com.alessandro.astages.api.wrapper.RecipeModWrapper;
import com.alessandro.astages.api.wrapper.RecipeWrapper;
import com.alessandro.astages.api.store.container.AttributeStore;
import net.minecraft.resources.ResourceLocation;

import java.util.ArrayList;
import java.util.List;

@NotNullParams
public class AClientRecipeModRestriction extends AClientBaseRecipeRestriction<AClientRecipeModRestriction, RecipeModWrapper, RecipeWrapper> {
    private String modId = null;
    private final List<ResourceLocation> ignoredRecipeIds = new ArrayList<>();

    public AClientRecipeModRestriction(String id, String stage) {
        super(id, stage);
    }

    @Override
    public @NotNull AttributeStore allowedAttributes() {
        return AttributeStore.compose()
            .withSuper(super.allowedAttributes())
            .withPlugin(AClientRestrictionManager.ATTACHED_ATTRIBUTES, AClientRecipeModRestriction.class)
            .build();
    }

    @Override
    public AClientRecipeModRestriction restrict(RecipeModWrapper wrapper) {
        modId = wrapper.modId();
        return this;
    }

    @Override
    public boolean isRestricted(RecipeWrapper wrapper) {
        return modId.equals(wrapper.recipe().getNamespace()) && !ignoredRecipeIds.contains(wrapper.recipe());
    }

    public AClientRecipeModRestriction ignoreItems(List<ResourceLocation> recipeIds) {
        ignoredRecipeIds.addAll(recipeIds);
        return this;
    }

    public String getModId() {
        return modId;
    }

    public List<ResourceLocation> getIgnoredRecipeIds() {
        return ignoredRecipeIds;
    }
}
