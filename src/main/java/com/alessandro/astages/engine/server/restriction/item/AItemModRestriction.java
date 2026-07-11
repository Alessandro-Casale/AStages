package com.alessandro.astages.engine.server.restriction.item;

import com.alessandro.astages.api.nullability.NotNullParamsAndMethodsReturn;
import com.alessandro.astages.api.store.container.AttributeStore;
import com.alessandro.astages.engine.ARestrictionManager;
import com.alessandro.astages.engine.server.restriction.ALootRestriction;
import com.alessandro.astages.infrastructure.networking.Networking;
import com.alessandro.astages.infrastructure.networking.packet.item.SyncItemModS2C;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@NotNullParamsAndMethodsReturn
public class AItemModRestriction extends ABaseItemRestriction<AItemModRestriction, String> {
    private final Set<String> modIds = new HashSet<>();
    private final Set<Item> ignoredItems = new HashSet<>();
    private final Set<TagKey<Item>> ignoredTags = new HashSet<>();

    public AItemModRestriction(String id, String stage) {
        super(id, stage);
    }

    @Override
    public AttributeStore allowedAttributes() {
        return AttributeStore.compose()
            .withSuper(super.allowedAttributes())
            .withPlugin(ARestrictionManager.ATTACHED_ATTRIBUTES, AItemModRestriction.class)
            .build();
    }

    @Override
    public AItemModRestriction restrict(String modId) {
        modIds.add(modId);
        return this;
    }

    @Override
    public boolean isRestricted(ItemStack stack) {
        if (stack.isEmpty()) { return false; }

        var registry = BuiltInRegistries.ITEM.getKey(stack.getItem());

        return !ignoredItems.contains(stack.getItem()) &&
            ignoredTags.stream().anyMatch(stack::is) &&
            modIds.contains(registry.getNamespace());
    }

    @SuppressWarnings("unused")
    public AItemModRestriction ignoreItems(Item... items) {
        ignoredItems.addAll(List.of(items));
        return this;
    }

    @SuppressWarnings("unused")
    public AItemModRestriction ignoreTags(TagKey<Item>... tags) {
        ignoredTags.addAll(Set.of(tags));
        return this;
    }

    public Set<String> getModIds() {
        return modIds;
    }

    public Set<Item> getIgnoredItems() {
        return ignoredItems;
    }

    public Set<TagKey<Item>> getIgnoredTags() {
        return ignoredTags;
    }

    @Override
    public void markAsDirty() {
        Networking.sendTo(null, new SyncItemModS2C(this));
        super.markAsDirty();
    }

    @Override
    public AItemModRestriction associateLootRestriction(String id) {
        var restriction = new ALootRestriction(id, getStage()).applyEverywhere();
        for (var modId : modIds) { restriction.restrictMods(modId); }
        for (var tag : ignoredTags) { restriction.ignoredTags(tag); }
        for (var item : ignoredItems) { restriction.ignoredItems(item); }
        ARestrictionManager.LOOT_INSTANCE.addRestriction(restriction);

        return this;
    }
}
