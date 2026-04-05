package com.alessandro.astages.core.server.restriction.recipe;

import com.alessandro.astages.api.nullability.NotNullParamsAndMethodsReturn;
import com.alessandro.astages.core.ARestrictionManager;
import com.alessandro.astages.core.wrapper.RecipeModWrapper;
import com.alessandro.astages.core.wrapper.RecipeWrapper;
import com.alessandro.astages.networking.ANetworking;
import com.alessandro.astages.networking.packet.recipe.RecipeModSyncerS2CPacket;
import com.alessandro.astages.store.AttributeStore;
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
            ANetworking.sendToAllPlayers(new RecipeModSyncerS2CPacket(this));
        }

        super.markAsDirty();
    }
}
