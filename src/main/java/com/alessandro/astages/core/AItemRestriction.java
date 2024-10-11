package com.alessandro.astages.core;

import com.alessandro.astages.util.ARestriction;
//import com.alessandro.astages.util.RestrictionType;
import net.minecraft.world.item.ItemStack;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;

public class AItemRestriction implements ARestriction {
    // public static String NON_RUNTIME_ID = "NON_RUNTIME_RESTRICTION_ID";

    public final String id;
//    public final RestrictionType type;

    public boolean renderItemName = false;
    public boolean hideTooltip = true;
    public boolean canPickedUp = false;
    public boolean canBeEquipped = false;
    public boolean canBeStoredInInventory = false;
    public boolean canAttack = false;
    public boolean hideInJEI = true;
    public boolean canBePlaced = false;
    public boolean canBlockBeUsed = false;
    public boolean canItemBeUsed = false;

    public int pickUpDelay = 60;

//    public List<Item> items = new ArrayList<>();
    public List<Predicate<ItemStack>> predicates = new ArrayList<>();

    public AItemRestriction(String id) {
        this.id = id;
//        this.type = RestrictionType.DEFAULT;
    }

//    public AItemRestriction(String id, RestrictionType type) {
//        this.type = type;
//
////        if (type == RestrictionType.DEFAULT) {
////            this.id = NON_RUNTIME_ID;
////        } else {
//        this.id = id;
////        }
//    }

    public AItemRestriction restrict(Predicate<ItemStack> predicate) {
        predicates.add(predicate);

//        ArrowFunction predicate1 = (ArrowFunction) predicate;
//        // predicate1.ge
//
//        AStages.LOGGER.debug("AAAAAAA" + predicate.test(predicates.get(1).));

        // final IForgeRegistry<Predicate<?>> PREDICATES = RegistryManager.ACTIVE.

//        var ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, AStages.MODID);
//        var a = ITEMS.register("aa", () -> new Item(new Item.Properties()));
//        a.get();
//
//        var PREDICATES = DeferredRegister.create(new ResourceLocation(AStages.MODID, "predicates"), AStages.MODID);
//        var p = PREDICATES.register("predicate1", () -> predicate);
//        p.get();

        // ForgeRegistries.ITEMS

        // 327159982
        // 1803202795
        // 1823190995

        return this;
    }

    public boolean isRestricted(ItemStack stack) {
        for (Predicate<ItemStack> predicate : predicates) {
            if (predicate.test(stack)) {
                return true;
            }
        }

        return false;
    }

    // GETTER AND SETTERS
    public int getPickUpDelay() {
        return pickUpDelay;
    }

    public AItemRestriction setPickUpDelay(int pickUpDelay) {
        this.pickUpDelay = pickUpDelay;

        return this;
    }

    public boolean isCanAttack() {
        return canAttack;
    }

    public AItemRestriction setCanAttack(boolean canAttack) {
        this.canAttack = canAttack;

        return this;
    }

    public boolean isCanBeStoredInInventory() {
        return canBeStoredInInventory;
    }

    public AItemRestriction setCanBeStoredInInventory(boolean canBeStoredInInventory) {
        this.canBeStoredInInventory = canBeStoredInInventory;

        return this;
    }

    public boolean isCanBeEquipped() {
        return canBeEquipped;
    }

    public AItemRestriction setCanBeEquipped(boolean canBeEquipped) {
        this.canBeEquipped = canBeEquipped;

        return this;
    }

    public boolean isCanPickedUp() {
        return canPickedUp;
    }

    public AItemRestriction setCanPickedUp(boolean canPickedUp) {
        this.canPickedUp = canPickedUp;

        return this;
    }

    public boolean isHideTooltip() {
        return hideTooltip;
    }

    public AItemRestriction setHideTooltip(boolean hideTooltip) {
        this.hideTooltip = hideTooltip;

        return this;
    }

    public boolean isRenderItemName() {
        return renderItemName;
    }

    public AItemRestriction setRenderItemName(boolean renderItemName) {
        this.renderItemName = renderItemName;

        return this;
    }

    public boolean isHideInJEI() {
        return hideInJEI;
    }

    public AItemRestriction setHideInJEI(boolean hideInJEI) {
        this.hideInJEI = hideInJEI;

        return this;
    }

    public boolean isCanBePlaced() {
        return canBePlaced;
    }

    public AItemRestriction setCanBePlaced(boolean canBePlaced) {
        this.canBePlaced = canBePlaced;

        return this;
    }

    public boolean isCanBlockBeUsed() {
        return canBlockBeUsed;
    }

    public AItemRestriction setCanBlockBeUsed(boolean canBlockBeUsed) {
        this.canBlockBeUsed = canBlockBeUsed;

        return this;
    }

    public boolean isCanItemBeUsed() {
        return canItemBeUsed;
    }

    public AItemRestriction setCanItemBeUsed(boolean canItemBeUsed) {
        this.canItemBeUsed = canItemBeUsed;

        return this;
    }
}
