package com.alessandro.astages.core.client.restriction.item;

import com.alessandro.astages.core.AModelManager;
import com.alessandro.astages.store.AModel;
import com.alessandro.astages.api.nullability.NotNullParams;
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
