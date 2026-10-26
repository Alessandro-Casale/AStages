package com.alessandro.astages.engine.client.restriction.item;

import com.alessandro.astages.api.nullability.NotNullParamsAndMethodsReturn;
import com.alessandro.astages.api.store.ARestrictionResult;
import com.alessandro.astages.api.store.container.AttributeStore;
import com.alessandro.astages.engine.AClientRestrictionManager;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

import java.util.ArrayList;
import java.util.List;

@NotNullParamsAndMethodsReturn
public class AClientItemRestriction extends AClientBaseItemRestriction<AClientItemRestriction, Item> {
    private final List<Item> items = new ArrayList<>();

    public AClientItemRestriction(String id, String stage, int priority) {
        super(id, stage, priority);
    }

    @Override
    public AttributeStore allowedAttributes() {
        return AttributeStore.compose()
            .withSuper(super.allowedAttributes())
            .withPlugin(AClientRestrictionManager.ATTACHED_ATTRIBUTES, AClientItemRestriction.class)
            .build();
    }

    @Override
    public AClientItemRestriction restrict(Item item) {
        items.add(item);

        return this;
    }

    @Override
    public ARestrictionResult isRestricted(ItemStack stack) {
        return items.contains(stack.getItem()) ? ARestrictionResult.RESTRICTED : ARestrictionResult.NOT_RESTRICTED;
    }

    public List<Item> getItems() {
        return items;
    }
}
