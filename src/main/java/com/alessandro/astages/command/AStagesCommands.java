package com.alessandro.astages.command;

import com.alessandro.astages.AStages;
import net.minecraftforge.event.RegisterCommandsEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import org.jetbrains.annotations.NotNull;

@Mod.EventBusSubscriber(modid = AStages.MODID)
public class AStagesCommands {
    @SubscribeEvent
    public static void commandRegisterEvent(@NotNull RegisterCommandsEvent event) {
        var dispatcher = event.getDispatcher();

        AStagesModificationCommands.register(dispatcher);
    }
}
