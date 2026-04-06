package com.alessandro.astages.engine.server.restriction.item;

import com.alessandro.astages.api.constant.ReloadType;
import com.alessandro.astages.api.feature.AChangeable;
import com.alessandro.astages.api.feature.AMarkable;
import com.alessandro.astages.api.nullability.NotNull;
import com.alessandro.astages.api.restriction.ARestriction;
import com.alessandro.astages.api.store.Attribute;
import com.alessandro.astages.api.store.container.AttributeStore;
import com.alessandro.astages.engine.ARestrictionManager;
import com.alessandro.astages.engine.server.restriction.ALootRestriction;
import com.alessandro.astages.engine.store.Attributes;
import com.alessandro.astages.infrastructure.hook.CommonEventSettings;
import com.alessandro.astages.infrastructure.networking.Networking;
import com.alessandro.astages.infrastructure.networking.packet.reload.RequestReloadS2C;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;

import java.util.function.Function;

public class ABaseItemRestriction<R extends ARestriction<R, U, ItemStack>, U> extends ARestriction<R, U, ItemStack> implements AChangeable, AMarkable {
    public ABaseItemRestriction(String id, String stage) {
        super(id, stage);
    }

    @SuppressWarnings("unused")
    public static @NotNull ABaseItemRestriction<?, ?> newBuilder() {
        return new ABaseItemRestriction<>("null", "null");
    }

    @Override
    public boolean isCorrectClassForConfigs(@NotNull ARestriction<?, ?, ?> config) {
        return config instanceof ABaseItemRestriction<?, ?>;
    }

    @Override
    public @NotNull AttributeStore allowedAttributes() {
        var defaultAttributes = AttributeStore.builder()
            .addAttribute(Attributes.RENDERING_NAME)
            .addAttribute(Attributes.HIDING_TOOLTIP)
            .addAttribute(Attributes.PICKING_UP)
            .addAttribute(Attributes.EQUIPPING)
            .addAttribute(Attributes.STORING_IN_INVENTORY)
            .addAttribute(Attributes.ATTACKING)
            .addAttribute(Attributes.HIDING_JEI)
            .addAttribute(Attributes.BLOCK_PLACING)
            .addAttribute(Attributes.LEFT_CLICK_INTERACTIONS)
            .addAttribute(Attributes.RIGHT_CLICK_INTERACTIONS)
            .addAttribute(Attributes.BLOCK_BREAKING)
            .addAttribute(Attributes.BLOCK_INTERACTIONS)
            // .addAttribute(Attributes.IGNORE_BLOCKS_AROUND)
            .addAttribute(Attributes.STORING_IN_CONTAINERS)

            .addAttribute(Attributes.PICK_UP_DELAY)

            .addAttribute(Attributes.Item.HIDDEN_NAME)
            .addAttribute(Attributes.Item.DROP_MESSAGE)
            .addAttribute(Attributes.Item.ATTACK_MESSAGE)
            .addAttribute(Attributes.Item.PICKING_UP_MESSAGE)
            .addAttribute(Attributes.Item.USING_MESSAGE)
            .addAttribute(Attributes.Item.MINING_MESSAGE)
            .addAttribute(Attributes.Item.PLACING_MESSAGE)
            .addAttribute(Attributes.Item.JADE_ITEM_MESSAGE)
            .addAttribute(Attributes.Item.JADE_BLOCK_MESSAGE);

        return AttributeStore.compose()
            .withSuper(super.allowedAttributes())
            .withSelf(defaultAttributes)
            .withPlugin(ARestrictionManager.ATTACHED_ATTRIBUTES, ABaseItemRestriction.class)
            .build();
    }

    @Override
    public R restrict(U object) {
        return null;
    }

    @Override
    public boolean isRestricted(ItemStack object) {
        return false;
    }

    @Override
    public <T> R set(Attribute<T> attribute, T value) {
        var toReturn = super.set(attribute, value);
        if (toReturn.isConfig()) { return toReturn; }

        if (attribute == Attributes.STORING_IN_INVENTORY || attribute == Attributes.EQUIPPING) {
            setChanged();
        }

        return toReturn;
    }

    @Override
    public void setChanged() {
        ARestrictionManager.ITEM_INSTANCE.recalculateCaches(this);
    }

    @Override
    public void markAsDirty() {
        setChanged();
        Networking.sendTo(null, new RequestReloadS2C(ReloadType.JEI_ITEM)); // For JEI cache reloading
        Networking.sendTo(null,  new RequestReloadS2C(ReloadType.ITEM)); // For Client properties clearing!
        CommonEventSettings.allInventoryChanged();
    }

    public ABaseItemRestriction<?, ?> associateLootRestriction(String id) {
        return this;
    }

    public ABaseItemRestriction<?, ?> associateLootRestriction() {
        return associateLootRestriction(getId() + ALootRestriction.IDENTIFIER);
    }

    @SuppressWarnings("unused")
    public ABaseItemRestriction<R, U> setPickUpDelay(int value) {
        set(Attributes.PICK_UP_DELAY, value);
        return this;
    }

    @SuppressWarnings("unused")
    public ABaseItemRestriction<R, U> setCanAttack(boolean value) {
        set(Attributes.ATTACKING, value);
        return this;
    }

    @SuppressWarnings("unused")
    public ABaseItemRestriction<R, U> setCanBeStoredInInventory(boolean value) {
        set(Attributes.STORING_IN_INVENTORY, value);
        return this;
    }

    @SuppressWarnings("unused")
    public ABaseItemRestriction<R, U> setCanBeEquipped(boolean value) {
        set(Attributes.EQUIPPING, value);
        return this;
    }

    @SuppressWarnings("unused")
    public ABaseItemRestriction<R, U> setCanPickedUp(boolean value) {
        set(Attributes.PICKING_UP, value);
        return this;
    }

    @SuppressWarnings("unused")
    public ABaseItemRestriction<R, U> setHideTooltip(boolean value) {
        set(Attributes.HIDING_TOOLTIP, value);
        return this;
    }

    @SuppressWarnings("unused")
    public ABaseItemRestriction<R, U> setRenderItemName(boolean value) {
        set(Attributes.RENDERING_NAME, value);
        return this;
    }

    @SuppressWarnings("unused")
    public ABaseItemRestriction<R, U> setHideInJEI(boolean value) {
        set(Attributes.HIDING_JEI, value);
        return this;
    }

    @SuppressWarnings("unused")
    public ABaseItemRestriction<R, U> setCanBePlaced(boolean value) {
        set(Attributes.BLOCK_PLACING, value);
        return this;
    }

    @SuppressWarnings("unused")
    public ABaseItemRestriction<R, U> setCanBeDig(boolean value) {
        set(Attributes.BLOCK_BREAKING, value);
        return this;
    }

    @SuppressWarnings("unused")
    public ABaseItemRestriction<R, U> setCanItemBeLeftClicked(boolean value) {
        set(Attributes.LEFT_CLICK_INTERACTIONS, value);
        return this;
    }

    @SuppressWarnings("unused")
    public ABaseItemRestriction<R, U> setCanItemBeRightClicked(boolean value) {
        set(Attributes.RIGHT_CLICK_INTERACTIONS, value);
        return this;
    }

    @SuppressWarnings("unused")
    public ABaseItemRestriction<R, U> setCanInteractWithBlock(boolean value) {
        set(Attributes.BLOCK_INTERACTIONS, value);
        return this;
    }

//    @SuppressWarnings("unused")
//    public ABaseItemRestriction<R, U> setIgnoreBlocksAroundWhenPlacing(boolean value) {
//        set(Attributes.IGNORE_BLOCKS_AROUND, value);
//        return this;
//    }

    @SuppressWarnings("unused")
    public ABaseItemRestriction<R, U> setCanBeStoredInContainers(boolean value) {
        set(Attributes.STORING_IN_CONTAINERS, value);
        return this;
    }

    @SuppressWarnings("unused")
    public ABaseItemRestriction<R, U> setHiddenName(Function<ItemStack, Component> message) {
        set(Attributes.Item.HIDDEN_NAME, message);
        return this;
    }

    @SuppressWarnings("unused")
    public ABaseItemRestriction<R, U> setDropMessage(Function<ItemStack, Component> message) {
        set(Attributes.Item.DROP_MESSAGE, message);
        return this;
    }

    @SuppressWarnings("unused")
    public ABaseItemRestriction<R, U> setAttackMessage(Function<ItemStack, Component> message) {
        set(Attributes.Item.ATTACK_MESSAGE, message);
        return this;
    }

    @SuppressWarnings("unused")
    public ABaseItemRestriction<R, U> setPickupMessage(Function<ItemStack, Component> message) {
        set(Attributes.Item.PICKING_UP_MESSAGE, message);
        return this;
    }

    @SuppressWarnings("unused")
    public ABaseItemRestriction<R, U> setUsageMessage(Function<ItemStack, Component> message) {
        set(Attributes.Item.USING_MESSAGE, message);
        return this;
    }

    @SuppressWarnings("unused")
    public ABaseItemRestriction<R, U> setMineMessage(Function<ItemStack, Component> message) {
        set(Attributes.Item.MINING_MESSAGE, message);
        return this;
    }

    @SuppressWarnings("unused")
    public ABaseItemRestriction<R, U> setPlaceMessage(Function<ItemStack, Component> message) {
        set(Attributes.Item.PLACING_MESSAGE, message);
        return this;
    }

    @SuppressWarnings("unused")
    public ABaseItemRestriction<R, U> setJadeItemMessage(Function<ItemStack, Component> message) {
        set(Attributes.Item.JADE_ITEM_MESSAGE, message);
        return this;
    }

    @SuppressWarnings("unused")
    public ABaseItemRestriction<R, U> setJadeBlockMessage(Function<ItemStack, Component> message) {
        set(Attributes.Item.JADE_BLOCK_MESSAGE, message);
        return this;
    }

    @Override
    public String toString() {
        return this.getId() + " " + this.get(Attributes.STORING_IN_INVENTORY);
    }
}
