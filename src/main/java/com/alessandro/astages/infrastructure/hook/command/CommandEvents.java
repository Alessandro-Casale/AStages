package com.alessandro.astages.infrastructure.hook.command;

import com.alessandro.astages.AStages;
import com.alessandro.astages.api.nullability.NotNullParams;
import com.alessandro.astages.infrastructure.command.*;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.RegisterCommandsEvent;

@NotNullParams
@EventBusSubscriber(modid = AStages.MODID)
public class CommandEvents {
    @SubscribeEvent
    public static void commandRegisterEvent(RegisterCommandsEvent event) {
        var dispatcher = event.getDispatcher();
        var context = event.getBuildContext();

        StageCommands.register(dispatcher);
        SimpleCommands.register(dispatcher, context);
        ServerCommands.register(dispatcher);
        TimerCommands.register(dispatcher);
        InfoCommands.register(dispatcher);
    }
}
