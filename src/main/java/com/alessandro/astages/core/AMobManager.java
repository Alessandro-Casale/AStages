package com.alessandro.astages.core;

import com.alessandro.astages.capability.ClientPlayerStage;
import com.alessandro.astages.util.AManager;
import net.minecraft.world.entity.EntityType;

import java.util.*;

public class AMobManager implements AManager<AMobRestriction, EntityType<?>> {
    private final Map<String, List<AMobRestriction>> restrictions = new HashMap<>();

    public void addRestriction(String stage, AMobRestriction restriction) {
        var newList = restrictions.getOrDefault(stage, new ArrayList<>());

        if (!newList.isEmpty()) { newList.removeIf(rest -> Objects.equals(rest.id, restriction.id)); }
        newList.add(restriction);

        ARestrictionManager.ALL_STAGES.add(stage);

        restrictions.put(stage, newList);
    }

    public AMobRestriction getRestriction(String id) {
        for (String stage : restrictions.keySet()) {
            for (AMobRestriction restriction : restrictions.get(stage)) {
                if (restriction.id.equals(id)) {
                    return restriction;
                }
            }
        }

        return null;
    }

    public AMobRestriction getRestriction(EntityType<?> mob) {
        for (String stage : restrictions.keySet()) {
            for (AMobRestriction restriction : restrictions.get(stage)) {
                if (restriction.isRestricted(mob) && !ClientPlayerStage.getPlayerStages().contains(stage)) {
                    return restriction;
                }
            }
        }

        return null;
    }
}
