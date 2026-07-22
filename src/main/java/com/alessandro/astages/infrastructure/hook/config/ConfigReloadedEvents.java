package com.alessandro.astages.infrastructure.hook.config;

import com.alessandro.astages.AStages;
import com.alessandro.astages.api.nullability.NotNullParams;
import com.alessandro.astages.infrastructure.config.AStagesClient;
import com.alessandro.astages.infrastructure.integration.RecipeViewerMods;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.event.config.ModConfigEvent;

@NotNullParams
@EventBusSubscriber(modid = AStages.MODID, value = Dist.CLIENT)
public class ConfigReloadedEvents {
    @SubscribeEvent
    public static void onConfigReloaded(ModConfigEvent.Reloading event) {
        if (event.getConfig().getSpec() == AStagesClient.SPEC) {
            RecipeViewerMods.clearCache();
        }
    }
}
