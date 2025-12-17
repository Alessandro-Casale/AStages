package com.alessandro.astages.core.client.restriction.item;

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
        return AttributeStore.builder()
                .addAttribute(Attributes.RENDERING_NAME)
                .addAttribute(Attributes.HIDING_TOOLTIP)
                .addAttribute(Attributes.HIDING_JEI);
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
