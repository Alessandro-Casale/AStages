package com.alessandro.astages.engine.client.restriction.item;

import com.alessandro.astages.api.nullability.NotNullParamsAndMethodsReturn;
import com.alessandro.astages.api.nullability.Nullable;
import com.alessandro.astages.api.restriction.AClientRestriction;
import com.alessandro.astages.api.store.ARestrictionResult;
import com.alessandro.astages.api.store.container.AttributeStore;
import com.alessandro.astages.engine.AClientRestrictionManager;
import com.alessandro.astages.engine.store.Attributes;
import net.minecraft.world.item.ItemStack;

@NotNullParamsAndMethodsReturn
public class AClientBaseItemRestriction<R extends AClientRestriction<R, U, ItemStack>, U> extends AClientRestriction<R, U, ItemStack> {
    public AClientBaseItemRestriction(String id, String stage, int priority) {
        super(id, stage, priority);
    }

    @Override
    public AttributeStore allowedAttributes() {
        var defaultAttributes = AttributeStore.builder()
            .addAttribute(Attributes.HIDING_RECIPE_VIEWER)
            .addAttribute(Attributes.SHOW_ACTION_BAR_NAME)
            .addAttribute(Attributes.SHOW_TOOLTIP_NAME)
            .addAttribute(Attributes.SHOW_RECIPE_VIEWER_NAME)
            .addAttribute(Attributes.SHOW_JADE_ITEM_NAME)
            .addAttribute(Attributes.SHOW_JADE_BLOCK_NAME);

        return AttributeStore.compose()
            .withSuper(super.allowedAttributes())
            .withSelf(defaultAttributes)
            .withPlugin(AClientRestrictionManager.ATTACHED_ATTRIBUTES, AClientBaseItemRestriction.class)
            .build();
    }

    @Override
    public @Nullable R restrict(U object) {
        return null;
    }

    @Override
    public ARestrictionResult isRestricted(ItemStack stack) {
        return ARestrictionResult.IGNORED;
    }
}
