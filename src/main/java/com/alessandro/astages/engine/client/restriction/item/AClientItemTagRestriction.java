package com.alessandro.astages.engine.client.restriction.item;

import com.alessandro.astages.api.nullability.NotNull;
import com.alessandro.astages.api.nullability.NotNullParams;
import com.alessandro.astages.api.store.container.AttributeStore;
import com.alessandro.astages.engine.AClientRestrictionManager;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

import java.util.HashSet;
import java.util.Set;

@NotNullParams
public class AClientItemTagRestriction extends AClientBaseItemRestriction<AClientItemTagRestriction, TagKey<Item>> {
    private TagKey<Item> tag;
    private final Set<Item> ignoredItems = new HashSet<>();

    public AClientItemTagRestriction(String id, String stage) {
        super(id, stage);
    }

    @Override
    public @NotNull AttributeStore allowedAttributes() {
        return AttributeStore.compose()
            .withSuper(super.allowedAttributes())
            .withPlugin(AClientRestrictionManager.ATTACHED_ATTRIBUTES, AClientItemTagRestriction.class)
            .build();
    }

    @Override
    public AClientItemTagRestriction restrict(TagKey<Item> tag) {
        this.tag = tag;
        return this;
    }

    @Override
    public boolean isRestricted(ItemStack stack) {
        if (stack.isEmpty()) { return false; }

        return !ignoredItems.contains(stack.getItem()) && stack.is(tag);
    }

    public AClientItemTagRestriction ignoreItems(Set<Item> items) {
        ignoredItems.addAll(items);
        return this;
    }

    public TagKey<Item> getTag() {
        return tag;
    }

    public Set<Item> getIgnoredItems() {
        return ignoredItems;
    }
}
