package com.alessandro.astages.engine.server.restriction.item;

import com.alessandro.astages.api.nullability.NotNullParamsAndMethodsReturn;
import com.alessandro.astages.api.store.container.AttributeStore;
import com.alessandro.astages.engine.ARestrictionManager;
import com.alessandro.astages.engine.server.restriction.ALootRestriction;
import com.alessandro.astages.infrastructure.networking.Networking;
import com.alessandro.astages.infrastructure.networking.packet.item.SyncItemTagS2C;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@NotNullParamsAndMethodsReturn
public class AItemTagRestriction extends ABaseItemRestriction<AItemTagRestriction, TagKey<Item>> {
    private TagKey<Item> tag;
    private final Set<Item> ignoredItems = new HashSet<>();

    public AItemTagRestriction(String id, String stage) {
        super(id, stage);
    }

    @Override
    public AttributeStore allowedAttributes() {
        return AttributeStore.compose()
            .withSuper(super.allowedAttributes())
            .withPlugin(ARestrictionManager.ATTACHED_ATTRIBUTES, AItemTagRestriction.class)
            .build();
    }

    @Override
    public AItemTagRestriction restrict(TagKey<Item> tag) {
        this.tag = tag;
        return this;
    }

    @Override
    public boolean isRestricted(ItemStack stack) {
        if (stack.isEmpty()) { return false; }

        return !ignoredItems.contains(stack.getItem()) && stack.is(tag);
    }

    public AItemTagRestriction ignoreItems(Item... items) {
        ignoredItems.addAll(List.of(items));
        return this;
    }

    public TagKey<Item> getTag() {
        return tag;
    }

    public Set<Item> getIgnoredItems() {
        return ignoredItems;
    }

    @Override
    public void markAsDirty() {
        Networking.sendTo(null, new SyncItemTagS2C(this));
        super.markAsDirty();
    }

    @Override
    public AItemTagRestriction associateLootRestriction(String id) {
        var restriction = new ALootRestriction(id, getStage()).applyEverywhere();
        restriction.restrictTags(tag);
        for (var item : ignoredItems) { restriction.ignoredItems(item); }
        ARestrictionManager.LOOT_INSTANCE.addRestriction(restriction);

        return this;
    }
}
