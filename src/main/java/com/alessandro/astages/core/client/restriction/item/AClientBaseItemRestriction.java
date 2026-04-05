package com.alessandro.astages.core.client.restriction.item;

import com.alessandro.astages.core.AClientRestrictionManager;
import com.alessandro.astages.store.AttributeStore;
import com.alessandro.astages.store.Attributes;
import com.alessandro.astages.store.client.AClientRestriction;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;

public class AClientBaseItemRestriction<R extends AClientRestriction<R, U, ItemStack>, U> extends AClientRestriction<R, U, ItemStack> {
    public AClientBaseItemRestriction(String id, String stage) {
        super(id, stage);
    }

    @Override
    public @NotNull AttributeStore allowedAttributes() {
        var defaultAttributes = AttributeStore.builder()
            .addAttribute(Attributes.RENDERING_NAME)
            .addAttribute(Attributes.HIDING_TOOLTIP)
            .addAttribute(Attributes.HIDING_JEI);

        return AttributeStore.compose()
            .withSuper(super.allowedAttributes())
            .withSelf(defaultAttributes)
            .withPlugin(AClientRestrictionManager.ATTACHED_ATTRIBUTES, AClientBaseItemRestriction.class)
            .build();
    }

    @Override
    public R restrict(U object) {
        return null;
    }

    @Override
    public boolean isRestricted(ItemStack object) {
        return false;
    }
}
