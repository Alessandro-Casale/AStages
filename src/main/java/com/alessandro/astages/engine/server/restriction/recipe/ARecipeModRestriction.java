package com.alessandro.astages.engine.server.restriction.recipe;

import com.alessandro.astages.api.nullability.NotNullParamsAndMethodsReturn;
import com.alessandro.astages.api.store.container.AttributeStore;
import com.alessandro.astages.api.wrapper.RecipeModWrapper;
import com.alessandro.astages.api.wrapper.RecipeWrapper;
import com.alessandro.astages.engine.ARestrictionManager;
import com.alessandro.astages.infrastructure.networking.Networking;
import com.alessandro.astages.infrastructure.networking.packet.recipe.SyncRecipeModS2C;
import net.minecraft.resources.ResourceLocation;

import java.util.ArrayList;
import java.util.List;

@NotNullParamsAndMethodsReturn
public class ARecipeModRestriction extends ABaseRecipeRestriction<ARecipeModRestriction, RecipeModWrapper, RecipeWrapper> {
    private String modId = null;
    private final List<ResourceLocation> ignoredRecipeIds = new ArrayList<>();

    public ARecipeModRestriction(String id, String stage) {
        super(id, stage);
    }

    @SuppressWarnings("unused")
    public static ARecipeModRestriction newBuilder() {
        return new ARecipeModRestriction("null", "null");
    }

    @Override
    public AttributeStore allowedAttributes() {
        return AttributeStore.compose()
            .withSuper(super.allowedAttributes())
            .withPlugin(ARestrictionManager.ATTACHED_ATTRIBUTES, ARecipeModRestriction.class)
            .build();
    }

    @Override
    public ARecipeModRestriction restrict(RecipeModWrapper wrapper) {
        modId = wrapper.modId();
        return this;
    }

    @Override
    public boolean isRestricted(RecipeWrapper wrapper) {
        return modId.equals(wrapper.recipe().getNamespace()) && !ignoredRecipeIds.contains(wrapper.recipe());
    }

    @SuppressWarnings("unused")
    public ARecipeModRestriction ignoreItems(String... itemIds) {
        for (var id : itemIds) {
            ignoredRecipeIds.add(ResourceLocation.parse(id));
        }
        return this;
    }

    public String getModId() {
        return modId;
    }

    public List<ResourceLocation> getIgnoredRecipeIds() {
        return ignoredRecipeIds;
    }

    @Override
    public void markAsDirty() {
        if (modId != null) {
            Networking.sendToAllPlayers(new SyncRecipeModS2C(this));
        }

        super.markAsDirty();
    }
}
