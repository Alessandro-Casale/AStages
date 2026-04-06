package com.alessandro.astages.internal.experimental;

import com.alessandro.astages.api.develop.UnderDevelopment;
import net.neoforged.neoforge.event.entity.player.PlayerInteractEvent;
import net.neoforged.neoforge.event.level.BlockEvent;
import net.neoforged.neoforge.event.level.block.CreateFluidSourceEvent;

@UnderDevelopment
//@EventBusSubscriber(modid = AStages.MODID)
public class FluidServerEvents {
    public static void onEvent(BlockEvent.FluidPlaceBlockEvent event) {
    }

//    public static void onEvent(FillBucketEvent event) {
//
//    }

    public static void onEvent(CreateFluidSourceEvent event) {
        // event.getLevel().getBiome(event.getPos()).is
        // event.getState().getFluidState().getType()
    }

    public static void onEvent(PlayerInteractEvent event) {
//        var stack = event.getItemStack();
//
//        if (stack.isEdible() && event.getEntity().eat())
    }
}
