package com.alessandro.astages.infrastructure.hook.restriction;

import com.alessandro.astages.AStages;
import com.alessandro.astages.api.ALoader;
import com.alessandro.astages.api.event.sync.ClientSynchronizeStagesEvent;
import com.alessandro.astages.api.event.update.ClientOreUpdateEvent;
import com.alessandro.astages.api.nullability.NotNullParams;
import com.alessandro.astages.engine.client.ClientMiscStorage;
import net.minecraft.client.Minecraft;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.EventPriority;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.util.thread.EffectiveSide;

@NotNullParams
@EventBusSubscriber(modid = AStages.MODID, value = Dist.CLIENT)
public class OreClientEvents {
    static {
        if (EffectiveSide.get().isClient()) {
            ALoader.EVENT_BUS.addListener(EventPriority.NORMAL, false, ClientOreUpdateEvent.class, e -> renderAllAgain());
        }
    }

    @SubscribeEvent
    public static void stageSync(ClientSynchronizeStagesEvent event) {
        if (!ClientMiscStorage.areOreStages(event.getStagesSynced())) { return; }

        renderAllAgain();
    }

    public static void renderAllAgain() {
        // Minecraft.getInstance().levelRenderer. // Try reloading only blocks
        Minecraft.getInstance().levelRenderer.allChanged();
    }
}
