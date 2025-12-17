package com.alessandro.astages.command;

import com.alessandro.astages.AStages;
import com.alessandro.astages.api.nullability.NotNullParams;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.RegisterCommandsEvent;

@NotNullParams
@EventBusSubscriber(modid = AStages.MODID)
public class AStagesCommands {
    @SubscribeEvent
    public static void commandRegisterEvent(RegisterCommandsEvent event) {
        var dispatcher = event.getDispatcher();
        var context = event.getBuildContext();

        AStagesModificationCommands.register(dispatcher);
        AStagesSimpleRestrictionsCommands.register(dispatcher, context);
        AStagesServerCommands.register(dispatcher);
        AStagesTimerCommands.register(dispatcher);
        AStagesInfoCommands.register(dispatcher);
    }
}
