package com.alessandro.astages.event.ore;

import com.alessandro.astages.AStages;
import com.alessandro.astages.api.develop.Info;
import com.alessandro.astages.api.holder.AHolder;
import com.alessandro.astages.api.nullability.NotNullParams;
import com.alessandro.astages.api.nullability.Nullable;
import com.alessandro.astages.core.ARestrictionManager;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.common.util.FakePlayer;
import net.neoforged.neoforge.event.level.BlockDropsEvent;

@NotNullParams
@EventBusSubscriber(modid = AStages.MODID)
public class ServerEventHandler {
    @Info("For exp settings! - For other ore changing see mixin/ore package!")
    @SubscribeEvent
    public static void onBlockBroken(BlockDropsEvent event) {
        if (event.getBreaker() instanceof Player player) {
            if (canBeRunForPlayer(player)) {
                var restriction = ARestrictionManager.ORE_INSTANCE.getRestriction(AHolder.serverAndPlayer(player), event.getState());

                if (restriction != null) {
                    var newValue = EnchantmentHelper.processBlockExperience(event.getLevel(), event.getTool(), restriction.getReplacement().getExpDrop(event.getLevel(), event.getPos(), event.getBlockEntity(), player, event.getTool()));
                    event.setDroppedExperience(newValue);
                }
            }
        }
    }
    public static boolean canBeRunForPlayer(@Nullable Player player) {
        return player != null && !player.level().isClientSide && !(player instanceof FakePlayer);
    }
}
