package com.alessandro.astages.engine.client.restriction.item;

import com.alessandro.astages.api.nullability.NotNullParamsAndMethodsReturn;
import com.alessandro.astages.api.store.ARestrictionResult;
import com.alessandro.astages.api.store.container.AttributeStore;
import com.alessandro.astages.engine.AClientRestrictionManager;
import com.alessandro.astages.engine.AModelManager;
import com.alessandro.astages.engine.store.AModel;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;

import java.util.function.Predicate;

@SuppressWarnings("unchecked")
@NotNullParamsAndMethodsReturn
public class AClientItemPredicateRestriction extends AClientBaseItemRestriction<AClientItemPredicateRestriction, ResourceLocation> {
    private ResourceLocation modelId;

    public AClientItemPredicateRestriction(String id, String stage, int priority) {
        super(id, stage, priority);
    }

    @Override
    public AttributeStore allowedAttributes() {
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

    @Override
    public ARestrictionResult isRestricted(ItemStack stack) {
        return ((AModel<Predicate<ItemStack>>) AModelManager.MODELS.getModel(modelId)).modelObject().test(stack) ?
            ARestrictionResult.RESTRICTED : ARestrictionResult.NOT_RESTRICTED;
    }

    public ResourceLocation getModelId() {
        return modelId;
    }
}
