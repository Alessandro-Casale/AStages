package com.alessandro.astages.core.server.restriction.item;

import com.alessandro.astages.core.AModelManager;
import com.alessandro.astages.networking.ANetworking;
import com.alessandro.astages.networking.packet.item.ItemPredicateSyncerS2CPacket;
import com.alessandro.astages.store.AModel;
import com.alessandro.astages.api.nullability.NotNullParamsAndMethodsReturn;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;

import java.util.function.Predicate;

@NotNullParamsAndMethodsReturn
public class AItemPredicateRestriction extends ABaseItemRestriction<AItemPredicateRestriction, ResourceLocation> {
    private ResourceLocation modelId;

    public AItemPredicateRestriction(String id, String stage) {
        super(id, stage);
    }

    @SuppressWarnings("unused")
    public static AItemPredicateRestriction newBuilder() {
        return new AItemPredicateRestriction("null", "null");
    }

    @Override
    public AItemPredicateRestriction restrict(ResourceLocation modelId) {
        this.modelId = modelId;
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

    @Override
    public void markAsDirty() {
        ANetworking.sendTo(null, new ItemPredicateSyncerS2CPacket(this));
        super.markAsDirty();
    }
}
