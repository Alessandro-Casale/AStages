package com.alessandro.astages.core.client.restriction.item;

import com.alessandro.astages.api.nullability.NotNull;
import com.alessandro.astages.api.nullability.NotNullParams;
import com.alessandro.astages.core.AClientRestrictionManager;
import com.alessandro.astages.store.AttributeStore;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

import java.util.ArrayList;
import java.util.List;

@NotNullParams
public class AClientItemModRestriction extends AClientBaseItemRestriction<AClientItemModRestriction, String> {
    private final List<String> modIds = new ArrayList<>();
    private final List<Item> ignoredItems = new ArrayList<>();
    private final List<ResourceLocation> ignoredTags = new ArrayList<>();

    public AClientItemModRestriction(String id, String stage) {
        super(id, stage);
    }

    @Override
    public @NotNull AttributeStore allowedAttributes() {
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
    public boolean isRestricted(ItemStack stack) {
        if (stack.isEmpty()) { return false; }

        var registry = BuiltInRegistries.ITEM.getKey(stack.getItem());

        return !ignoredItems.contains(stack.getItem()) &&
            modIds.contains(registry.getNamespace()) &&
            stack.getTags().noneMatch(t -> ignoredTags.contains(t.location()));
    }

    public AClientItemModRestriction ignoreItems(List<Item> items) {
        ignoredItems.addAll(items);
        return this;
    }

    public AClientItemModRestriction ignoreTags(List<ResourceLocation> tags) {
        ignoredTags.addAll(tags);
        return this;
    }

    public List<String> getModIds() {
        return modIds;
    }

    public List<Item> getIgnoredItems() {
        return ignoredItems;
    }

    public List<ResourceLocation> getIgnoredTags() {
        return ignoredTags;
    }
}
