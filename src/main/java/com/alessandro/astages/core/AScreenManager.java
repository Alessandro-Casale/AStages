package com.alessandro.astages.core;

import com.alessandro.astages.capability.ClientPlayerStage;
import com.alessandro.astages.util.AManager;
import com.alessandro.astages.util.AStagesUtil;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.MenuType;

import java.util.*;

public class AScreenManager implements AManager<AScreenRestriction, MenuType<?>> {
    private final Map<String, List<AScreenRestriction>> restrictions = new HashMap<>();

    @Override
    public void addRestriction(String stage, AScreenRestriction restriction) {
        var newList = restrictions.getOrDefault(stage, new ArrayList<>());

        if (!newList.isEmpty()) { newList.removeIf(rest -> Objects.equals(rest.id, restriction.id)); }
        newList.add(restriction);

        ARestrictionManager.ALL_STAGES.add(stage);

        restrictions.put(stage, newList);
    }

    @Override
    public AScreenRestriction getRestriction(String id) {
        for (String stage : restrictions.keySet()) {
            for (AScreenRestriction restriction : restrictions.get(stage)) {
                if (restriction.id.equals(id)) {
                    return restriction;
                }
            }
        }

        return null;
    }

    @Override
    public AScreenRestriction getRestriction(MenuType<?> menu) {
        for (String stage : restrictions.keySet()) {
            for (AScreenRestriction restriction : restrictions.get(stage)) {
                if (restriction.isRestricted(menu) && !ClientPlayerStage.getPlayerStages().contains(stage)) {
                    return restriction;
                }
            }
        }

        return null;
    }

    @Override
    public AScreenRestriction getRestriction(Player player, MenuType<?> menu) {
        for (String stage : restrictions.keySet()) {
            for (AScreenRestriction restriction : restrictions.get(stage)) {
                if (restriction.isRestricted(menu) && !AStagesUtil.hasStage(player, stage)) {
                    return restriction;
                }
            }
        }

        return null;
    }
}
