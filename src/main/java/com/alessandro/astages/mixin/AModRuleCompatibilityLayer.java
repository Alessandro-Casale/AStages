package com.alessandro.astages.mixin;

import com.alessandro.astages.api.AStagesUtils;
import com.alessandro.astages.api.holder.AHolder;
import mcjty.incontrol.compat.ModRuleCompatibilityLayer;
import net.minecraft.world.entity.player.Player;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;

@Mixin(value = ModRuleCompatibilityLayer.class, remap = false)
public class AModRuleCompatibilityLayer {
    /**
     * @author Alessandro
     * @reason AStages integration
     */
    @Overwrite
    public boolean hasGameStages() {
        return true;
    }

    /**
     * @author Alessandro
     * @reason AStages integration
     */
    @Overwrite
    public boolean hasGameStage(Player player, String stage) {
        if (player == null) { return false; }

        return AStagesUtils.hasStage(AHolder.player(player), stage);
    }

    /**
     * @author Alessandro
     * @reason AStages integration
     */
    @Overwrite
    public void addGameStage(Player player, String stage) {
        if (player == null) { return; }

        AStagesUtils.addStage(AHolder.player(player), stage, false);
    }

    /**
     * @author Alessandro
     * @reason AStages integration
     */
    @Overwrite
    public void removeGameStage(Player player, String stage) {
        if (player == null) { return; }

        AStagesUtils.removeStage(AHolder.player(player), stage, false);
    }
}
