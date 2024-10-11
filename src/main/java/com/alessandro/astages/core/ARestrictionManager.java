package com.alessandro.astages.core;

import com.alessandro.astages.util.ARestriction;
import com.alessandro.astages.util.ARestrictionType;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.*;
import java.util.concurrent.atomic.AtomicReference;

public class ARestrictionManager {
    public static final AItemManager ITEM_INSTANCE = new AItemManager();
    public static final ADimensionManager DIMENSION_INSTANCE = new ADimensionManager();
    public static final AMobManager MOB_INSTANCE = new AMobManager();

    public static final Set<String> ALL_STAGES = new HashSet<>();

    public static ARestriction getRestrictionById(@NotNull ARestrictionType type, String id) {
        return switch (type) {
            case ITEM -> ITEM_INSTANCE.getRestriction(id);
            case MOB -> MOB_INSTANCE.getRestriction(id);
            case DIMENSION -> DIMENSION_INSTANCE.getRestriction(id);
        };
    }

    // public static final Map<String, ARestriction> RESTRICTIONS = new HashMap<>();

    // public static ARestriction getRestrictionById(String id) {
        // return RESTRICTIONS.getOrDefault(id, null);
    // }

//    public static ARestriction getRestrictionById(String id) {
//        AtomicReference<ARestriction> restriction = new AtomicReference<>();
//
//        ITEM_INSTANCE.restrictions.forEach((s, restrictions) -> restrictions.forEach(r -> {
//            if (Objects.equals(r.id, id)) {
//                restriction.set(r);
//            }
//        }));
//    }

    // FOR DEBUG ONLY!
    static {
//        var r = new Restriction();
//        r.items.add(Items.HOPPER);
//        INSTANCE.addRestriction("cobblestone", r);

        /*
         - Single restriction WORK!
           INSTANCE.addRestriction("hoppers", new Restriction().restrict(itemStack -> itemStack.is(Items.HOPPER)));

         - Concatenation of multiple restrictions WORK!
           INSTANCE.addRestriction("hoppers", new Restriction().restrict(itemStack -> itemStack.is(Items.HOPPER)).restrict(itemStack -> ForgeHooks.getBurnTime(itemStack, RecipeType.SMELTING) == 300));

         - Concatenation with custom attributes WORK! (solved "display item name" problem!)
           INSTANCE.addRestriction("hoppers", new Restriction().restrict(itemStack -> itemStack.is(Items.HOPPER)).restrict(itemStack -> ForgeHooks.getBurnTime(itemStack, RecipeType.SMELTING) == 300).setCanPickedUp(true).setCanBeStoredInInventory(true));

         */
        // INSTANCE.addRestriction("hoppers", new Restriction().restrict(itemStack -> itemStack.is(Items.HOPPER)).restrict(itemStack -> ForgeHooks.getBurnTime(itemStack, RecipeType.SMELTING) == 300).setCanPickedUp(true).setCanBeStoredInInventory(true).setRenderItemName(true));
        // INSTANCE.addRestriction("stack", new ARestriction().restrict(itemStack -> itemStack.getMaxStackSize() == 16));
    }
}
