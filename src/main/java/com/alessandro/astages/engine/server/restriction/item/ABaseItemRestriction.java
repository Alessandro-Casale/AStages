package com.alessandro.astages.engine.server.restriction.item;

import com.alessandro.astages.api.feature.AChangeable;
import com.alessandro.astages.api.feature.AMarkable;
import com.alessandro.astages.api.nullability.NotNull;
import com.alessandro.astages.api.reload.ClientReloadPhase;
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

@SuppressWarnings("UnusedReturnValue")
public class ABaseItemRestriction<R extends ARestriction<R, U, ItemStack>, U> extends ARestriction<R, U, ItemStack> implements AChangeable, AMarkable {
    public ABaseItemRestriction(String id, String stage) {
        super(id, stage);
    }

    @Override
    public @NotNull AttributeStore allowedAttributes() {
        var defaultAttributes = AttributeStore.builder()
            .addAttribute(Attributes.PICKUP)
            .addAttribute(Attributes.EQUIPPING)
            .addAttribute(Attributes.STORING_IN_INVENTORY)
            .addAttribute(Attributes.ATTACKING)
            .addAttribute(Attributes.HIDING_RECIPE_VIEWER)
            .addAttribute(Attributes.BLOCK_PLACING)
            .addAttribute(Attributes.LEFT_CLICK_INTERACTIONS)
            .addAttribute(Attributes.RIGHT_CLICK_INTERACTIONS)
            .addAttribute(Attributes.BLOCK_BREAKING)
            .addAttribute(Attributes.BLOCK_INTERACTIONS)
            .addAttribute(Attributes.STORING_IN_CONTAINERS)
            .addAttribute(Attributes.SHOW_ACTION_BAR_NAME)
            .addAttribute(Attributes.SHOW_TOOLTIP_NAME)
            .addAttribute(Attributes.SHOW_RECIPE_VIEWER_NAME)
            .addAttribute(Attributes.SHOW_JADE_ITEM_NAME)
            .addAttribute(Attributes.SHOW_JADE_BLOCK_NAME)

            .addAttribute(Attributes.PICKUP_DELAY)

            .addAttribute(Attributes.Item.DROP_MESSAGE)
            .addAttribute(Attributes.Item.ATTACK_MESSAGE)
            .addAttribute(Attributes.Item.PICKUP_MESSAGE)
            .addAttribute(Attributes.Item.USE_MESSAGE)
            .addAttribute(Attributes.Item.BREAKING_MESSAGE)
            .addAttribute(Attributes.Item.PLACE_MESSAGE)
            .addAttribute(Attributes.Item.ACTION_BAR_MESSAGE)
            .addAttribute(Attributes.Item.TOOLTIP_MESSAGE)
            .addAttribute(Attributes.Item.RECIPE_VIEWER_MESSAGE)
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
        Networking.sendTo(null, new RequestReloadS2C(ClientReloadPhase.ITEM_RESTRICTION_MARKED_AS_DIRTY));
        CommonEventSettings.allInventoryChanged();
    }

    public ABaseItemRestriction<?, ?> associateLootRestriction(String id) {
        return this;
    }

    public ABaseItemRestriction<?, ?> associateLootRestriction() {
        return associateLootRestriction(getId() + ALootRestriction.IDENTIFIER);
    }

    public R pickupDelay(int value) {
        return set(Attributes.PICKUP_DELAY, value);
    }

    public R allowAttack() {
        return set(Attributes.ATTACKING, true);
    }

    public R showInRecipeViewer() {
        return set(Attributes.HIDING_RECIPE_VIEWER, false);
    }

    public R allowInventoryStorage() {
        return set(Attributes.STORING_IN_INVENTORY, true);
    }

    public R allowEquip() {
        return set(Attributes.EQUIPPING, true);
    }

    public R allowPickup() {
        return set(Attributes.PICKUP, true);
    }

    public R allowPlacement() {
        return set(Attributes.BLOCK_PLACING, true);
    }

    public R allowMining() {
        return set(Attributes.BLOCK_BREAKING, true);
    }

    public R allowLeftClick() {
        return set(Attributes.LEFT_CLICK_INTERACTIONS, true);
    }

    public R allowRightClick() {
        return set(Attributes.RIGHT_CLICK_INTERACTIONS, true);
    }

    public R disableBlockInteraction() {
        return set(Attributes.BLOCK_INTERACTIONS, false);
    }

    public R allowContainerStorage() {
        return set(Attributes.STORING_IN_CONTAINERS, true);
    }

    @SuppressWarnings("unchecked")
    public R globalShowName() {
        showActionBarName();
        showTooltipName();
        showRecipeViewerName();
        showJadeItemName();
        showJadeBlockName();

        return (R) this;
    }

    public R showActionBarName() {
        return set(Attributes.SHOW_ACTION_BAR_NAME, true);
    }

    public R showTooltipName() {
        return set(Attributes.SHOW_TOOLTIP_NAME, true);
    }

    public R showRecipeViewerName() {
        return set(Attributes.SHOW_RECIPE_VIEWER_NAME, true);
    }

    public R showJadeItemName() {
        return set(Attributes.SHOW_JADE_ITEM_NAME, true);
    }

    public R showJadeBlockName() {
        return set(Attributes.SHOW_JADE_BLOCK_NAME, true);
    }

    public R dropMessage(Function<ItemStack, Component> message) {
        return set(Attributes.Item.DROP_MESSAGE, message);
    }

    public R attackMessage(Function<ItemStack, Component> message) {
        return set(Attributes.Item.ATTACK_MESSAGE, message);
    }

    public R pickupMessage(Function<ItemStack, Component> message) {
        return set(Attributes.Item.PICKUP_MESSAGE, message);
    }

    public R useMessage(Function<ItemStack, Component> message) {
        return set(Attributes.Item.USE_MESSAGE, message);
    }

    public R breakMessage(Function<ItemStack, Component> message) {
        return set(Attributes.Item.BREAKING_MESSAGE, message);
    }

    public R placeMessage(Function<ItemStack, Component> message) {
        return set(Attributes.Item.PLACE_MESSAGE, message);
    }

    @SuppressWarnings("unchecked")
    public R globalHiddenMessage(Function<ItemStack, Component> message) {
        actionBarMessage(message);
        tooltipMessage(message);
        recipeViewerMessage(message);
        jadeItemMessage(message);
        jadeBlockMessage(message);

        return (R) this;
    }

    public R actionBarMessage(Function<ItemStack, Component> message) {
        return set(Attributes.Item.ACTION_BAR_MESSAGE, message);
    }

    public R tooltipMessage(Function<ItemStack, Component> message) {
        return set(Attributes.Item.TOOLTIP_MESSAGE, message);
    }

    public R recipeViewerMessage(Function<ItemStack, Component> message) {
        return set(Attributes.Item.RECIPE_VIEWER_MESSAGE, message);
    }

    public R jadeItemMessage(Function<ItemStack, Component> message) {
        return set(Attributes.Item.JADE_ITEM_MESSAGE, message);
    }

    public R jadeBlockMessage(Function<ItemStack, Component> message) {
        return set(Attributes.Item.JADE_BLOCK_MESSAGE, message);
    }

    @SuppressWarnings("unchecked")
    @Deprecated(forRemoval = true, since = "3.0.0")
    public R showTooltip() {
        // return set(Attributes.HIDING_TOOLTIP, false);
        return (R) this;
    }

    @SuppressWarnings("unchecked")
    @Deprecated(forRemoval = true, since = "3.0.0")
    public R showName() {
        // return set(Attributes.RENDERING_NAME, true);
        return (R) this;
    }

    @SuppressWarnings("unchecked")
    @Deprecated(forRemoval = true, since = "3.0.0")
    public R hiddenName(Function<ItemStack, Component> message) {
        // return set(Attributes.Item.HIDDEN_NAME, message);
        return (R) this;
    }

    @Deprecated(forRemoval = true, since = "3.0.0")
    public R mineMessage(Function<ItemStack, Component> message) {
        return set(Attributes.Item.BREAKING_MESSAGE, message);
    }

    @SuppressWarnings("unchecked")
    @Deprecated(forRemoval = true, since = "3.0.0")
    public R showInJEI() {
        // return set(Attributes.HIDING_RECIPE_VIEWER, false);
        return (R) this;
    }

    @Deprecated(forRemoval = true, since = "3.0.0")
    public ABaseItemRestriction<R, U> setPickUpDelay(int value) {
        set(Attributes.PICKUP_DELAY, value);
        return this;
    }

    @Deprecated(forRemoval = true, since = "3.0.0")
    public ABaseItemRestriction<R, U> setCanAttack(boolean value) {
        set(Attributes.ATTACKING, value);
        return this;
    }

    @Deprecated(forRemoval = true, since = "3.0.0")
    public ABaseItemRestriction<R, U> setCanBeStoredInInventory(boolean value) {
        set(Attributes.STORING_IN_INVENTORY, value);
        return this;
    }

    @Deprecated(forRemoval = true, since = "3.0.0")
    public ABaseItemRestriction<R, U> setCanBeEquipped(boolean value) {
        set(Attributes.EQUIPPING, value);
        return this;
    }

    @Deprecated(forRemoval = true, since = "3.0.0")
    public ABaseItemRestriction<R, U> setCanPickedUp(boolean value) {
        set(Attributes.PICKUP, value);
        return this;
    }

    @Deprecated(forRemoval = true, since = "3.0.0")
    public ABaseItemRestriction<R, U> setHideTooltip(boolean ignoredValue) {
        // set(Attributes.HIDING_TOOLTIP, value);
        return this;
    }

    @Deprecated(forRemoval = true, since = "3.0.0")
    public ABaseItemRestriction<R, U> setRenderItemName(boolean ignoredValue) {
        // set(Attributes.RENDERING_NAME, value);
        return this;
    }

    @Deprecated(forRemoval = true, since = "3.0.0")
    public ABaseItemRestriction<R, U> setHideInJEI(boolean ignoredValue) {
        // set(Attributes.HIDING_RECIPE_VIEWER, value);
        return this;
    }

    @Deprecated(forRemoval = true, since = "3.0.0")
    public ABaseItemRestriction<R, U> setCanBePlaced(boolean value) {
        set(Attributes.BLOCK_PLACING, value);
        return this;
    }

    @Deprecated(forRemoval = true, since = "3.0.0")
    public ABaseItemRestriction<R, U> setCanBeDig(boolean value) {
        set(Attributes.BLOCK_BREAKING, value);
        return this;
    }

    @Deprecated(forRemoval = true, since = "3.0.0")
    public ABaseItemRestriction<R, U> setCanItemBeLeftClicked(boolean value) {
        set(Attributes.LEFT_CLICK_INTERACTIONS, value);
        return this;
    }

    @Deprecated(forRemoval = true, since = "3.0.0")
    public ABaseItemRestriction<R, U> setCanItemBeRightClicked(boolean value) {
        set(Attributes.RIGHT_CLICK_INTERACTIONS, value);
        return this;
    }

    @Deprecated(forRemoval = true, since = "3.0.0")
    public ABaseItemRestriction<R, U> setCanInteractWithBlock(boolean value) {
        set(Attributes.BLOCK_INTERACTIONS, value);
        return this;
    }

//    @SuppressWarnings("unused")
//    public ABaseItemRestriction<R, U> setIgnoreBlocksAroundWhenPlacing(boolean value) {
//        set(Attributes.IGNORE_BLOCKS_AROUND, value);
//        return this;
//    }

    @Deprecated(forRemoval = true, since = "3.0.0")
    public ABaseItemRestriction<R, U> setCanBeStoredInContainers(boolean value) {
        set(Attributes.STORING_IN_CONTAINERS, value);
        return this;
    }

    @Deprecated(forRemoval = true, since = "3.0.0")
    public ABaseItemRestriction<R, U> setHiddenName(Function<ItemStack, Component> message) {
        // set(Attributes.Item.HIDDEN_NAME, message);
        return this;
    }

    @Deprecated(forRemoval = true, since = "3.0.0")
    public ABaseItemRestriction<R, U> setDropMessage(Function<ItemStack, Component> message) {
        set(Attributes.Item.DROP_MESSAGE, message);
        return this;
    }

    @Deprecated(forRemoval = true, since = "3.0.0")
    public ABaseItemRestriction<R, U> setAttackMessage(Function<ItemStack, Component> message) {
        set(Attributes.Item.ATTACK_MESSAGE, message);
        return this;
    }

    @Deprecated(forRemoval = true, since = "3.0.0")
    public ABaseItemRestriction<R, U> setPickupMessage(Function<ItemStack, Component> message) {
        set(Attributes.Item.PICKUP_MESSAGE, message);
        return this;
    }

    @Deprecated(forRemoval = true, since = "3.0.0")
    public ABaseItemRestriction<R, U> setUsageMessage(Function<ItemStack, Component> message) {
        set(Attributes.Item.USE_MESSAGE, message);
        return this;
    }

    @Deprecated(forRemoval = true, since = "3.0.0")
    public ABaseItemRestriction<R, U> setMineMessage(Function<ItemStack, Component> message) {
        set(Attributes.Item.BREAKING_MESSAGE, message);
        return this;
    }

    @Deprecated(forRemoval = true, since = "3.0.0")
    public ABaseItemRestriction<R, U> setPlaceMessage(Function<ItemStack, Component> message) {
        set(Attributes.Item.PLACE_MESSAGE, message);
        return this;
    }

    @Deprecated(forRemoval = true, since = "3.0.0")
    public ABaseItemRestriction<R, U> setJadeItemMessage(Function<ItemStack, Component> message) {
        set(Attributes.Item.JADE_ITEM_MESSAGE, message);
        return this;
    }

    @Deprecated(forRemoval = true, since = "3.0.0")
    public ABaseItemRestriction<R, U> setJadeBlockMessage(Function<ItemStack, Component> message) {
        set(Attributes.Item.JADE_BLOCK_MESSAGE, message);
        return this;
    }

    @Override
    public String toString() {
        return this.getId() + " " + this.get(Attributes.STORING_IN_INVENTORY);
    }
}