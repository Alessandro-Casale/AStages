package com.alessandro.astages.core;

import com.alessandro.astages.capability.ClientPlayerStage;
import com.alessandro.astages.util.AManager;
import com.alessandro.astages.util.AStagesUtil;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;

import java.util.*;

public class AStructureManager implements AManager<AStructureRestriction, ResourceLocation> {
    private final Map<String, List<AStructureRestriction>> restrictions = new HashMap<>();

    public Map<String, List<AStructureRestriction>> getRestrictions() {
        return restrictions;
    }

    @Override
    public void addRestriction(String stage, AStructureRestriction restriction) {
        var newList = restrictions.getOrDefault(stage, new ArrayList<>());

        if (!newList.isEmpty()) { newList.removeIf(rest -> Objects.equals(rest.id, restriction.id)); }
        newList.add(restriction);

        ARestrictionManager.ALL_STAGES.add(stage);

        restrictions.put(stage, newList);
    }

    @Override
    public AStructureRestriction getRestriction(String id) {
        for (String stage : restrictions.keySet()) {
            for (AStructureRestriction restriction : restrictions.get(stage)) {
                if (restriction.id.equals(id)) {
                    return restriction;
                }
            }
        }

        return null;
    }

    @Override
    public AStructureRestriction getRestriction(ResourceLocation structure) {
        for (String stage : restrictions.keySet()) {
            for (AStructureRestriction restriction : restrictions.get(stage)) {
                if (restriction.isRestricted(structure) && !ClientPlayerStage.getPlayerStages().contains(stage)) {
                    return restriction;
                }
            }
        }

        return null;
    }

    @Override
    public AStructureRestriction getRestriction(Player player, ResourceLocation structure) {
        for (String stage : restrictions.keySet()) {
            for (AStructureRestriction restriction : restrictions.get(stage)) {
                if (restriction.isRestricted(structure) && !AStagesUtil.hasStage(player, stage)) {
                    return restriction;
                }
            }
        }

        return null;
    }
}
