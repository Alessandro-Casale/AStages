package com.alessandro.astages.engine.client.restriction.item;

import com.alessandro.astages.api.nullability.NotNullParamsAndMethodsReturn;
import com.alessandro.astages.api.store.ARestrictionResult;
import com.alessandro.astages.api.store.container.AttributeStore;
import com.alessandro.astages.engine.AClientRestrictionManager;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

import java.util.HashSet;
import java.util.Set;

@NotNullParamsAndMethodsReturn
public class AClientItemModRestriction extends AClientBaseItemRestriction<AClientItemModRestriction, String> {
    private final Set<String> modIds = new HashSet<>();
    private final Set<Item> ignoredItems = new HashSet<>();
    private final Set<TagKey<Item>> ignoredTags = new HashSet<>();

    public AClientItemModRestriction(String id, String stage, int priority) {
        super(id, stage, priority);
    }

    @Override
    public AttributeStore allowedAttributes() {
        return AttributeStore.compose()
            .withSuper(super.allowedAttributes())
            .withPlugin(AClientRestrictionManager.ATTACHED_ATTRIBUTES, AClientItemModRestriction.class)
            .build();
    }

    @Override
    public AClientItemModRestriction restrict(String modId) {
        modIds.add(modId);
        return this;
    }

    @Override
    public ARestrictionResult isRestricted(ItemStack stack) {
        var registry = BuiltInRegistries.ITEM.getKey(stack.getItem());

        if (ignoredItems.contains(stack.getItem())) { return ARestrictionResult.IGNORED; }
        if (ignoredTags.stream().anyMatch(stack::is)) { return ARestrictionResult.IGNORED; }

        return modIds.contains(registry.getNamespace()) ? ARestrictionResult.RESTRICTED : ARestrictionResult.NOT_RESTRICTED;
    }

    public AClientItemModRestriction ignoreItems(Set<Item> items) {
        ignoredItems.addAll(items);
        return this;
    }

    public AClientItemModRestriction ignoreTags(Set<TagKey<Item>> tags) {
        ignoredTags.addAll(tags);
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
}
