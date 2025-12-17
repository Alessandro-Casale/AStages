package com.alessandro.astages.core.server.restriction.item;

import com.alessandro.astages.core.ARestrictionManager;
import com.alessandro.astages.core.server.restriction.ALootRestriction;
import com.alessandro.astages.networking.ANetworking;
import com.alessandro.astages.networking.packet.item.ItemModSyncerS2CPacket;
import com.alessandro.astages.api.nullability.NotNullParamsAndMethodsReturn;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

import java.util.ArrayList;
import java.util.List;

@NotNullParamsAndMethodsReturn
public class AItemModRestriction extends ABaseItemRestriction<AItemModRestriction, String> {
    private String modId;
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
    public AItemModRestriction restrict(String modId) {
        this.modId = modId;
        return this;
    }

    @Override
    public boolean isRestricted(ItemStack stack) {
        if (stack.isEmpty()) { return false; }

        var registry = BuiltInRegistries.ITEM.getKey(stack.getItem());

        return !ignoredItems.contains(stack.getItem()) &&
            modId.equals(registry.getNamespace()) &&
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

    public String getModId() {
        return modId;
    }

    public List<Item> getIgnoredItems() {
        return ignoredItems;
    }

    public List<ResourceLocation> getIgnoredTags() {
        return ignoredTags;
    }

    @Override
    public void markAsDirty() {
        ANetworking.sendTo(null, new ItemModSyncerS2CPacket(this));
        super.markAsDirty();
    }

    @Override
    public AItemModRestriction associateLootRestriction(String id) {
        var restriction = new ALootRestriction(id, getStage()).applyForEveryLootTableAndDrop(true);
        restriction.restrictMods(modId);
        for (var tag : ignoredTags) { restriction.ignoredTags(tag); }
        for (var item : ignoredItems) { restriction.ignoredItems(item); }
        ARestrictionManager.LOOT_INSTANCE.addRestriction(restriction);

        return this;
    }
}
