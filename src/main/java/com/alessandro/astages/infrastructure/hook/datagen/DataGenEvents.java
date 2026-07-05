package com.alessandro.astages.infrastructure.hook.datagen;

import com.alessandro.astages.AStages;
import com.alessandro.astages.api.nullability.NotNullParams;
import com.alessandro.astages.infrastructure.datagen.ALanguageProvider;
import com.alessandro.astages.infrastructure.datagen.ALootProvider;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.data.event.GatherDataEvent;

@NotNullParams
@EventBusSubscriber(modid = AStages.MODID)
public class DataGenEvents {
    @SubscribeEvent
    public static void gatherData(GatherDataEvent event) {
        event.createProvider(packOutput -> new ALanguageProvider(packOutput, "en_us"));
        event.createProvider(ALootProvider::new);
    }
}
