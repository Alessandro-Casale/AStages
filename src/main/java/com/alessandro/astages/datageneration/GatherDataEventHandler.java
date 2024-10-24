package com.alessandro.astages.datageneration;

import com.alessandro.astages.AStages;
import net.minecraft.data.DataProvider;
import net.minecraftforge.data.event.GatherDataEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import org.jetbrains.annotations.NotNull;

@Mod.EventBusSubscriber(modid = AStages.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class GatherDataEventHandler {
    @SubscribeEvent
    public static void gatherData(@NotNull GatherDataEvent event) {
        event.getGenerator().addProvider(
            event.includeClient(),
            (DataProvider.Factory<ALanguageProvider>) output -> new ALanguageProvider(output, "en_us")
        );
    }
}
