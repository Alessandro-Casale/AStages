package com.alessandro.astages.core;

import com.alessandro.astages.capability.ClientPlayerStage;
import com.alessandro.astages.util.AManager;
import com.alessandro.astages.util.AStagesUtil;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.block.state.BlockState;

import java.util.*;

public class AOreManager implements AManager<AOreRestriction, BlockState> {
    private final Map<String, List<AOreRestriction>> restrictions = new HashMap<>();

    public Map<String, List<AOreRestriction>> getRestrictions() {
        return restrictions;
    }

    @Override
    public void addRestriction(String stage, AOreRestriction restriction) {
        var newList = restrictions.getOrDefault(stage, new ArrayList<>());

        if (!newList.isEmpty()) { newList.removeIf(rest -> Objects.equals(rest.id, restriction.id)); }
        newList.add(restriction);

        ARestrictionManager.ALL_STAGES.add(stage);

        restrictions.put(stage, newList);
    }

    @Override
    public AOreRestriction getRestriction(String id) {
        for (String stage : restrictions.keySet()) {
            for (AOreRestriction restriction : restrictions.get(stage)) {
                if (restriction.id.equals(id)) {
                    return restriction;
                }
            }
        }

        return null;
    }

    @Override
    public AOreRestriction getRestriction(BlockState original) {
        for (String stage : restrictions.keySet()) {
            for (AOreRestriction restriction : restrictions.get(stage)) {
                if (restriction.isRestricted(original) && !ClientPlayerStage.getPlayerStages().contains(stage)) {
                    return restriction;
                }
            }
        }

        return null;
    }

    @Override
    public AOreRestriction getRestriction(Player player, BlockState original) {
        for (String stage : restrictions.keySet()) {
            for (AOreRestriction restriction : restrictions.get(stage)) {
                if (restriction.isRestricted(original) && !AStagesUtil.hasStage(player, stage)) {
                    return restriction;
                }
            }
        }

        return null;
    }
}
