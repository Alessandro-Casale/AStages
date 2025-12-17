package com.alessandro.astages.datageneration;

import com.alessandro.astages.AStages;
import com.alessandro.astages.api.nullability.NotNullParams;
import net.minecraft.data.PackOutput;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.data.event.GatherDataEvent;

@NotNullParams
@EventBusSubscriber(modid = AStages.MODID)
public class GatherDataEventHandler {
    @SubscribeEvent
    public static void gatherData(GatherDataEvent event) {
        PackOutput packOutput = event.getGenerator().getPackOutput();

        event.getGenerator().addProvider(
            event.includeClient(),
            new ALanguageProvider(packOutput, "en_us")
        );

//        event.getGenerator().addProvider(
//            event.includeServer(),
//            new ALootProvider(packOutput)
//        );
    }
}
