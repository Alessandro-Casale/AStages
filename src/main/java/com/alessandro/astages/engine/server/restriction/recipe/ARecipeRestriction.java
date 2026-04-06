package com.alessandro.astages.engine.server.restriction.recipe;

import com.alessandro.astages.api.nullability.NotNullParamsAndMethodsReturn;
import com.alessandro.astages.api.store.container.AttributeStore;
import com.alessandro.astages.api.wrapper.RecipeWrapper;
import com.alessandro.astages.engine.ARestrictionManager;
import com.alessandro.astages.infrastructure.networking.Networking;
import com.alessandro.astages.infrastructure.networking.packet.recipe.SyncRecipeS2C;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.crafting.RecipeType;

import java.util.ArrayList;
import java.util.List;

@NotNullParamsAndMethodsReturn
public class ARecipeRestriction extends ABaseRecipeRestriction<ARecipeRestriction, RecipeWrapper, RecipeWrapper> {
    private RecipeType<?> type = null;
    private final List<ResourceLocation> recipes = new ArrayList<>();

    private final RuntimeException TYPE_EXCEPTION = new RuntimeException("Trying to add recipes of different type for restriction with id: " + getId() + "!");

    public ARecipeRestriction(String id, String stage) {
        super(id, stage);
    }

    @SuppressWarnings("unused")
    public static ARecipeRestriction newBuilder() {
        return new ARecipeRestriction("null", "null");
    }

    @Override
    public AttributeStore allowedAttributes() {
        return AttributeStore.compose()
            .withSuper(super.allowedAttributes())
            .withPlugin(ARestrictionManager.ATTACHED_ATTRIBUTES, ARecipeRestriction.class)
            .build();
    }

    @Override
    public ARecipeRestriction restrict(RecipeWrapper wrapper) {
        if (type == null) { this.type = wrapper.type(); }

        if (type == wrapper.type()) {
            this.recipes.add(wrapper.recipe());
        } else {
            throw TYPE_EXCEPTION;
        }

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

    @Override
    public void markAsDirty() {
        if (type != null && !recipes.isEmpty()) {
            Networking.sendToAllPlayers(new SyncRecipeS2C(this));
        }

        super.markAsDirty();
    }
}
