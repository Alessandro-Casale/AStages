package com.alessandro.astages.infrastructure.hook.restriction;

import com.alessandro.astages.AStages;
import com.alessandro.astages.api.develop.Info;
import com.alessandro.astages.api.holder.AHolder;
import com.alessandro.astages.api.nullability.NotNullParams;
import com.alessandro.astages.engine.ARestrictionManager;
import com.alessandro.astages.engine.util.EventGuards;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.player.PlayerEvent;
import net.neoforged.neoforge.event.level.BlockDropsEvent;

@NotNullParams
@EventBusSubscriber(modid = AStages.MODID)
public class OreServerEvents {
    @Info("For exp settings! - For other ore changing see mixin/ore package!")
    @SubscribeEvent
    public static void onBlockBroken(BlockDropsEvent event) {
        if (event.getBreaker() instanceof Player player) {
            if (!EventGuards.isValidPlayer(player)) { return; }

            var restriction = ARestrictionManager.ORE_INSTANCE.getRestriction(AHolder.serverAndPlayer(player), event.getState());

            if (restriction != null) {
                var newValue = EnchantmentHelper.processBlockExperience(event.getLevel(), event.getTool(), restriction.getReplacement().getExpDrop(event.getLevel(), event.getPos(), event.getBlockEntity(), player, event.getTool()));
                event.setDroppedExperience(newValue);
            }
        }
    }

    @SubscribeEvent
    public static void onPlayerHarvest(PlayerEvent.HarvestCheck event) {
        var player = event.getEntity();
        if (!EventGuards.isValidPlayer(player)) { return; }

        var restriction = ARestrictionManager.ORE_INSTANCE.getRestriction(AHolder.serverAndPlayer(player), event.getTargetBlock());

        if (restriction != null) {
            event.setCanHarvest(player.hasCorrectToolForDrops(restriction.getReplacement(), player.level(), event.getPos()));
        }
    }
}
