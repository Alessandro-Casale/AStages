package com.alessandro.astages.engine.client.restriction.item;

import com.alessandro.astages.api.nullability.NotNullParamsAndMethodsReturn;
import com.alessandro.astages.api.store.ARestrictionResult;
import com.alessandro.astages.api.store.container.AttributeStore;
import com.alessandro.astages.engine.AClientRestrictionManager;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

import java.util.HashSet;
import java.util.Set;

@NotNullParamsAndMethodsReturn
public class AClientItemTagRestriction extends AClientBaseItemRestriction<AClientItemTagRestriction, TagKey<Item>> {
    private TagKey<Item> tag;
    private final Set<Item> ignoredItems = new HashSet<>();

    public AClientItemTagRestriction(String id, String stage, int priority) {
        super(id, stage, priority);
    }

    @Override
    public AttributeStore allowedAttributes() {
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
    public ARestrictionResult isRestricted(ItemStack stack) {
        if (ignoredItems.contains(stack.getItem())) { return ARestrictionResult.IGNORED; }

        return stack.is(tag) ? ARestrictionResult.RESTRICTED : ARestrictionResult.NOT_RESTRICTED;
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
