package com.alessandro.astages.core.client.restriction.item;

import com.alessandro.astages.api.nullability.NotNullParams;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

import java.util.ArrayList;
import java.util.List;

@NotNullParams
public class AClientItemRestriction extends AClientBaseItemRestriction<AClientItemRestriction, Item> {
    private final List<Item> items = new ArrayList<>();

    public AClientItemRestriction(String id, String stage) {
        super(id, stage);
    }

    @Override
    public AClientItemRestriction restrict(Item item) {
        items.add(item);

        return this;
    }

    @Override
    public boolean isRestricted(ItemStack stack) {
        if (stack.isEmpty()) { return false; }

        return items.contains(stack.getItem());
    }

    public List<Item> getItems() {
        return items;
    }
}
