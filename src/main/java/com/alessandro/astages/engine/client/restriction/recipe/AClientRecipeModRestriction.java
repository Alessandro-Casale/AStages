package com.alessandro.astages.engine.client.restriction.recipe;

import com.alessandro.astages.api.nullability.NotNullParamsAndMethodsReturn;
import com.alessandro.astages.api.store.ARestrictionResult;
import com.alessandro.astages.api.store.container.AttributeStore;
import com.alessandro.astages.api.wrapper.RecipeModWrapper;
import com.alessandro.astages.api.wrapper.RecipeWrapper;
import com.alessandro.astages.engine.AClientRestrictionManager;
import net.minecraft.resources.ResourceLocation;

import java.util.ArrayList;
import java.util.List;

@NotNullParamsAndMethodsReturn
public class AClientRecipeModRestriction extends AClientBaseRecipeRestriction<AClientRecipeModRestriction, RecipeModWrapper, RecipeWrapper> {
    private String modId = null;
    private final List<ResourceLocation> ignoredRecipeIds = new ArrayList<>();

    public AClientRecipeModRestriction(String id, String stage, int priority) {
        super(id, stage, priority);
    }

    @Override
    public AttributeStore allowedAttributes() {
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
    public ARestrictionResult isRestricted(RecipeWrapper wrapper) {
        if (ignoredRecipeIds.contains(wrapper.recipe())) { return ARestrictionResult.IGNORED; }

        return modId.equals(wrapper.recipe().getNamespace()) ? ARestrictionResult.RESTRICTED : ARestrictionResult.NOT_RESTRICTED;
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
