package com.alessandro.astages.core;

import com.alessandro.astages.util.AManager;
import net.minecraft.world.entity.player.Player;

import java.util.*;

public class ATimeManager implements AManager<ATimeRestriction, String> {
    private final Map<String, List<ATimeRestriction>> restrictions = new HashMap<>();

    public Map<String, List<ATimeRestriction>> getRestrictions() {
        return restrictions;
    }

    @Override
    public void addRestriction(String stage, ATimeRestriction restriction) {
        var newList = restrictions.getOrDefault(stage, new ArrayList<>());

        if (!newList.isEmpty()) { newList.removeIf(rest -> Objects.equals(rest.id, restriction.id)); }
        newList.add(restriction);

        ARestrictionManager.ALL_STAGES.add(stage);

        restrictions.put(stage, newList);
    }

    @Override
    public ATimeRestriction getRestriction(String id) {
        for (String stage : restrictions.keySet()) {
            for (ATimeRestriction restriction : restrictions.get(stage)) {
                if (restriction.id.equals(id)) {
                    return restriction;
                }
            }
        }

        return null;
    }

    public ATimeRestriction getRestriction(Player player, String id) {
        for (String stage : restrictions.keySet()) {
            for (ATimeRestriction restriction : restrictions.get(stage)) {
                if (restriction.id.equals(id)) {
                    return restriction;
                }
            }
        }

        return null;
    }
}
