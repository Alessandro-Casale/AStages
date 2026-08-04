package com.alessandro.astages.engine.server.restriction.item;

import com.alessandro.astages.api.develop.Info;
import com.alessandro.astages.api.nullability.NotNullParamsAndMethodsReturn;
import com.alessandro.astages.api.store.container.AttributeStore;
import com.alessandro.astages.engine.ARestrictionManager;
import com.alessandro.astages.engine.server.restriction.ALootRestriction;
import com.alessandro.astages.engine.store.Attributes;
import com.alessandro.astages.infrastructure.networking.Networking;
import com.alessandro.astages.infrastructure.networking.packet.item.SyncItemS2C;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

import java.util.ArrayList;
import java.util.List;

@NotNullParamsAndMethodsReturn
public class AItemRestriction extends ABaseItemRestriction<AItemRestriction, Item> {
    private final List<Item> items = new ArrayList<>();

    public AItemRestriction(String id, String stage) {
        super(id, stage);
    }

    @Override
    public AttributeStore allowedAttributes() {
        return AttributeStore.compose()
            .withSuper(super.allowedAttributes())
            .withPlugin(ARestrictionManager.ATTACHED_ATTRIBUTES, AItemRestriction.class)
            .build();
    }

    @Override
    public AItemRestriction restrict(Item item) {
        items.add(item);

        return this;
    }

    @SuppressWarnings("UnusedReturnValue")
    @Info("Utility method used to maintain consistency across multiple implementations.")
    public AItemRestriction setArmorAttributes() {
        set(Attributes.STORING_IN_INVENTORY, true);
        set(Attributes.STORING_IN_CONTAINERS, true);
        set(Attributes.EQUIPPING, false);
        set(Attributes.ATTACKING, true);
        set(Attributes.BLOCK_PLACING, true);
        set(Attributes.LEFT_CLICK_INTERACTIONS, true);
        set(Attributes.RIGHT_CLICK_INTERACTIONS, true);
        set(Attributes.BLOCK_BREAKING, true);
        set(Attributes.PICKUP, true);
        set(Attributes.SHOW_ACTION_BAR_NAME, true);
        set(Attributes.SHOW_TOOLTIP_NAME, true);
        set(Attributes.SHOW_RECIPE_VIEWER_NAME, true);
        set(Attributes.SHOW_JADE_ITEM_NAME, true);
        set(Attributes.SHOW_JADE_BLOCK_NAME, true);

        return this;
    }

    @Override
    public boolean isRestricted(ItemStack stack) {
        if (stack.isEmpty()) { return false; }

        return items.contains(stack.getItem());
    }

    public List<Item> getItems() {
        return items;
    }

    @Override
    public void markAsDirty() {
        Networking.sendTo(null, new SyncItemS2C(this));
        super.markAsDirty();
    }

    @Override
    public AItemRestriction associateLootRestriction(String id) {
        var restriction = new ALootRestriction(id, getStage()).applyEverywhere();
        for (var item : items) { restriction.restrictItems(item); }
        ARestrictionManager.LOOT_INSTANCE.addRestriction(restriction);

        return this;
    }
}
