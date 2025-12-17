package com.alessandro.astages.core.server.restriction.item;

import com.alessandro.astages.core.ARestrictionManager;
import com.alessandro.astages.core.server.restriction.ALootRestriction;
import com.alessandro.astages.networking.ANetworking;
import com.alessandro.astages.networking.packet.item.ItemTagSyncerS2CPacket;
import com.alessandro.astages.api.nullability.NotNullParamsAndMethodsReturn;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

import java.util.ArrayList;
import java.util.List;

@NotNullParamsAndMethodsReturn
public class AItemTagRestriction extends ABaseItemRestriction<AItemTagRestriction, ResourceLocation> {
    private ResourceLocation tag;
    private final List<Item> ignoredItems = new ArrayList<>();

    public AItemTagRestriction(String id, String stage) {
        super(id, stage);
    }

    @SuppressWarnings("unused")
    public static AItemTagRestriction newBuilder() {
        return new AItemTagRestriction("null", "null");
    }

    @Override
    public AItemTagRestriction restrict(ResourceLocation tag) {
        this.tag = tag;
        return this;
    }

    @Override
    public boolean isRestricted(ItemStack stack) {
        if (stack.isEmpty()) { return false; }

        return !ignoredItems.contains(stack.getItem()) && stack.getTags().anyMatch(t -> t.location().equals(tag));
    }

    @SuppressWarnings("unused")
    public AItemTagRestriction ignoreItems(Item... items) {
        ignoredItems.addAll(List.of(items));
        return this;
    }

    public ResourceLocation getTag() {
        return tag;
    }

    public List<Item> getIgnoredItems() {
        return ignoredItems;
    }

    @Override
    public void markAsDirty() {
        ANetworking.sendTo(null, new ItemTagSyncerS2CPacket(this));
        super.markAsDirty();
    }

    @Override
    public AItemTagRestriction associateLootRestriction(String id) {
        var restriction = new ALootRestriction(id, getStage()).applyForEveryLootTableAndDrop(true);
        restriction.restrictTags(tag);
        for (var item : ignoredItems) { restriction.ignoredItems(item); }
        ARestrictionManager.LOOT_INSTANCE.addRestriction(restriction);

        return this;
    }
}
