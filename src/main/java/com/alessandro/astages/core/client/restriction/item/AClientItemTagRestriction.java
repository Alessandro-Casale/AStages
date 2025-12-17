package com.alessandro.astages.core.client.restriction.item;

import com.alessandro.astages.api.nullability.NotNullParams;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

import java.util.ArrayList;
import java.util.List;

@NotNullParams
public class AClientItemTagRestriction extends AClientBaseItemRestriction<AClientItemTagRestriction, ResourceLocation> {
    private ResourceLocation tag;
    private final List<Item> ignoredItems = new ArrayList<>();

    public AClientItemTagRestriction(String id, String stage) {
        super(id, stage);
    }

    @Override
    public AClientItemTagRestriction restrict(ResourceLocation tag) {
        this.tag = tag;
        return this;
    }

    @Override
    public boolean isRestricted(ItemStack stack) {
        if (stack.isEmpty()) { return false; }

        return !ignoredItems.contains(stack.getItem()) && stack.getTags().anyMatch(t -> t.location().equals(tag));
    }

    public AClientItemTagRestriction ignoreItems(List<Item> items) {
        ignoredItems.addAll(items);
        return this;
    }

    public ResourceLocation getTag() {
        return tag;
    }

    public List<Item> getIgnoredItems() {
        return ignoredItems;
    }
}
