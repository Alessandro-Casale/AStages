package com.alessandro.astages.core;

import com.alessandro.astages.capability.ClientPlayerStage;
import com.alessandro.astages.util.AManager;
import com.alessandro.astages.util.AStagesUtil;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;

import java.util.*;

public class ADimensionManager implements AManager<ADimensionRestriction, ResourceLocation> {
    private final Map<String, List<ADimensionRestriction>> restrictions = new HashMap<>();

    public void addRestriction(String stage, ADimensionRestriction restriction) {
        var newList = restrictions.getOrDefault(stage, new ArrayList<>());

        if (!newList.isEmpty()) { newList.removeIf(rest -> Objects.equals(rest.id, restriction.id)); }
        newList.add(restriction);

        ARestrictionManager.ALL_STAGES.add(stage);

        restrictions.put(stage, newList);
    }

    public ADimensionRestriction getRestriction(String id) {
        for (String stage : restrictions.keySet()) {
            for (ADimensionRestriction restriction : restrictions.get(stage)) {
                if (restriction.id.equals(id)) {
                    return restriction;
                }
            }
        }

        return null;
    }

    public ADimensionRestriction getRestriction(ResourceLocation dimension) {
        for (String stage : restrictions.keySet()) {
            for (ADimensionRestriction restriction : restrictions.get(stage)) {
                if (restriction.isRestricted(dimension) && !ClientPlayerStage.getPlayerStages().contains(stage)) {
                    return restriction;
                }
            }
        }

        return null;
    }

    public ADimensionRestriction getRestriction(Player player, ResourceLocation dimension) {
        for (String stage : restrictions.keySet()) {
            for (ADimensionRestriction restriction : restrictions.get(stage)) {
                if (restriction.isRestricted(dimension) && !AStagesUtil.hasStage(player, stage)) {
                    return restriction;
                }
            }
        }

        return null;
    }
}
