package com.alessandro.astages.core;

import com.alessandro.astages.capability.ClientPlayerStage;
import com.alessandro.astages.util.AManager;
import com.alessandro.astages.util.ARestriction;
import com.alessandro.astages.util.ARestrictionType;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.*;

public class AItemManager implements AManager<AItemRestriction, ItemStack> {
    // public static final ARestrictionType type = ARestrictionType.ITEM;

    public final Map<String, List<AItemRestriction>> restrictions = new HashMap<>();

    public void addRestriction(String stage, AItemRestriction restriction) {
        var newList = restrictions.getOrDefault(stage, new ArrayList<>());

//        if (!newList.isEmpty()) { newList.removeIf(rest -> rest.arePredicatesEqualTo(restriction)); }
//        if (!newList.isEmpty() && !Objects.equals(restriction.id, AItemRestriction.NON_RUNTIME_ID)) { newList.removeIf(rest -> Objects.equals(rest.id, restriction.id)); }
        if (!newList.isEmpty()) { newList.removeIf(rest -> Objects.equals(rest.id, restriction.id)); }
        newList.add(restriction);

        ARestrictionManager.ALL_STAGES.add(stage);

        restrictions.put(stage, newList);
        // registerRestriction(restriction);
    }

//    public void registerRestriction(@NotNull AItemRestriction restriction) {
//        var id = type.getId() + ":" + restriction.id;
//        ARestrictionManager.RESTRICTIONS.put(id, restriction);
//    }

    public AItemRestriction getRestriction(String id) {
        for (String stage : restrictions.keySet()) {
            for (AItemRestriction restriction : restrictions.get(stage)) {
                if (restriction.id.equals(id)) {
                    return restriction;
                }
            }
        }

        return null;
    }

    /**
     * Get the restriction for a particular item.
     * If returns null, there isn't any restriction for the item.
     *
     * @param stack item to check
     * @return restriction or null
     */
    public AItemRestriction getRestriction(ItemStack stack) {
        for (String stage : restrictions.keySet()) {
            for (AItemRestriction restriction : restrictions.get(stage)) {
                if (restriction.isRestricted(stack) && !ClientPlayerStage.getPlayerStages().contains(stage)) {
                    return restriction;
                }
            }
        }

        return null;
    }
}
