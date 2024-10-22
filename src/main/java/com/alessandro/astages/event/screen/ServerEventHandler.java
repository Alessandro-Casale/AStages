package com.alessandro.astages.event.screen;

import com.alessandro.astages.AStages;
import com.alessandro.astages.core.ARestrictionManager;
import net.minecraftforge.client.event.ContainerScreenEvent;
import net.minecraftforge.client.event.ScreenEvent;
import net.minecraftforge.event.entity.player.PlayerContainerEvent;
import net.minecraftforge.eventbus.api.Event;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.ForgeRegistries;
import org.jetbrains.annotations.NotNull;

@Mod.EventBusSubscriber(modid = AStages.MODID)
public class ServerEventHandler {
//    @SubscribeEvent
//    public static void playerOpenScreen(PlayerContainerEvent.@NotNull Open event) {
//        var type = event.getContainer().getType();
//        // var resourceLocation = ForgeRegistries.MENU_TYPES.getKey(type);
//
//        AStages.LOGGER.debug(event.getContainer().getType().toString());
//
//        var restriction = ARestrictionManager.SCREEN_INSTANCE.getRestriction(event.getEntity(), type);
//
//        if (restriction != null) {
//            // event.setCanceled(true);
//            event.setResult(Event.Result.DENY);
//            event.setResult(Result.);
//            // event.setPhase();
//        }
//    }

//    public static void playerOpenScreen(ScreenEvent.Opening event) {
//        event.getNewScreen();
//    }
//
//    public static void playerOpenScreen(ScreenEvent.Closing event) {
//
//    }
}
