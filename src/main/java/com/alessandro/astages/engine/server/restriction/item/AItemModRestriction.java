package com.alessandro.astages.engine.server.restriction.item;

import com.alessandro.astages.api.nullability.NotNullParamsAndMethodsReturn;
import com.alessandro.astages.api.store.container.AttributeStore;
import com.alessandro.astages.engine.ARestrictionManager;
import com.alessandro.astages.engine.server.restriction.ALootRestriction;
import com.alessandro.astages.infrastructure.networking.Networking;
import com.alessandro.astages.infrastructure.networking.packet.item.SyncItemModS2C;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

import java.util.ArrayList;
import java.util.List;

@NotNullParamsAndMethodsReturn
public class AItemModRestriction extends ABaseItemRestriction<AItemModRestriction, String> {
    private final List<String> modIds = new ArrayList<>();
    private final List<Item> ignoredItems = new ArrayList<>();
    private final List<ResourceLocation> ignoredTags = new ArrayList<>();

    public AItemModRestriction(String id, String stage) {
        super(id, stage);
    }

    @SuppressWarnings("unused")
    public static AItemModRestriction newBuilder() {
        return new AItemModRestriction("null", "null");
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
            modIds.contains(registry.getNamespace()) &&
            stack.getTags().noneMatch(t -> ignoredTags.contains(t.location()));
    }

    @SuppressWarnings("unused")
    public AItemModRestriction ignoreItems(Item... items) {
        ignoredItems.addAll(List.of(items));
        return this;
    }

    @SuppressWarnings("unused")
    public AItemModRestriction ignoreTags(ResourceLocation... tags) {
        ignoredTags.addAll(List.of(tags));
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

    @Override
    public void markAsDirty() {
        Networking.sendTo(null, new SyncItemModS2C(this));
        super.markAsDirty();
    }

    @Override
    public AItemModRestriction associateLootRestriction(String id) {
        var restriction = new ALootRestriction(id, getStage()).applyForEveryLootTableAndDrop(true);
        for (var modId : modIds) { restriction.restrictMods(modId); }
        for (var tag : ignoredTags) { restriction.ignoredTags(tag); }
        for (var item : ignoredItems) { restriction.ignoredItems(item); }
        ARestrictionManager.LOOT_INSTANCE.addRestriction(restriction);

        return this;
    }
}
