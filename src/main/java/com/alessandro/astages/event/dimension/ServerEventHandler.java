package com.alessandro.astages.event.dimension;

import com.alessandro.astages.AStages;
import com.alessandro.astages.core.ADimensionRestriction;
import com.alessandro.astages.core.ARestrictionManager;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.event.entity.EntityTravelToDimensionEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import org.jetbrains.annotations.NotNull;

@Mod.EventBusSubscriber(modid = AStages.MODID)
public class ServerEventHandler {
    @SubscribeEvent
    public static void onEntityTravel(@NotNull EntityTravelToDimensionEvent event) {
        if (event.getEntity() instanceof Player player) {
//            ResourceLocation currentDimension = event.getEntity().level().dimension().location();
            ResourceLocation dimension = event.getDimension().location();

            ADimensionRestriction restriction = ARestrictionManager.DIMENSION_INSTANCE.getRestriction(player, dimension);
//            ADimensionRestriction restrictionForCurrentDimension = ARestrictionManager.DIMENSION_INSTANCE.getRestriction(currentDimension);

//            if (restriction != null) {
//                var isBidirectional = restriction.bidirectional;
//
//                if (isBidirectional) {
//
//                }
//            }

            // if (restriction != null && !restriction.bidirectional) {
            if (restriction != null) {
                event.setCanceled(true);

                player.displayClientMessage(Component.literal("You can't visit this dimension!"), true);
            }
        }
    }
}
