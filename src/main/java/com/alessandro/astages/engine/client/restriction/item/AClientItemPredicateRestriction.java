package com.alessandro.astages.engine.client.restriction.item;

import com.alessandro.astages.api.nullability.NotNull;
import com.alessandro.astages.api.nullability.NotNullParams;
import com.alessandro.astages.api.store.container.AttributeStore;
import com.alessandro.astages.engine.AClientRestrictionManager;
import com.alessandro.astages.engine.AModelManager;
import com.alessandro.astages.engine.store.AModel;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;

import java.util.function.Predicate;

@NotNullParams
public class AClientItemPredicateRestriction extends AClientBaseItemRestriction<AClientItemPredicateRestriction, ResourceLocation> {
    private ResourceLocation modelId;

    public AClientItemPredicateRestriction(String id, String stage) {
        super(id, stage);
    }

    @Override
    public @NotNull AttributeStore allowedAttributes() {
        return AttributeStore.compose()
            .withSuper(super.allowedAttributes())
            .withPlugin(AClientRestrictionManager.ATTACHED_ATTRIBUTES, AClientItemPredicateRestriction.class)
            .build();
    }


    @Override
    public AClientItemPredicateRestriction restrict(ResourceLocation model) {
        this.modelId = model;
        return this;
    }

    @SuppressWarnings("unchecked")
    @Override
    public boolean isRestricted(ItemStack stack) {
        if (stack.isEmpty()) { return false; }

        return ((AModel<Predicate<ItemStack>>) AModelManager.MODELS.getModel(modelId)).modelObject().test(stack);
    }

    public ResourceLocation getModelId() {
        return modelId;
    }
}
