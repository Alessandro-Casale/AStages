package com.alessandro.astages.event.ore;

import com.alessandro.astages.AStages;
import com.alessandro.astages.capability.ClientPlayerStage;
import com.alessandro.astages.core.AOreRestriction;
import com.alessandro.astages.core.ARestrictionManager;
import com.alessandro.astages.event.custom.StageAddedPlayerEvent;
import com.alessandro.astages.event.custom.StageRemovedPlayerEvent;
import com.alessandro.astages.event.custom.StageSyncedPlayerEvent;
import com.alessandro.astages.render.AOreBakedModel;
import com.alessandro.astages.util.AStagesUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.world.level.block.Blocks;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RenderLevelStageEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLLoadCompleteEvent;

import java.util.List;
import java.util.Map;

// import static com.alessandro.astages.util.AStagesUtil.setBakedModelForState;

@Mod.EventBusSubscriber(modid = AStages.MODID, value = Dist.CLIENT)
public class ClientEventHandler {
    @SubscribeEvent
    public static void stageSync(StageSyncedPlayerEvent event) {
        for (Map.Entry<String, List<AOreRestriction>> entry : ARestrictionManager.ORE_INSTANCE.getRestrictions().entrySet()) {
            for (AOreRestriction restriction : entry.getValue()) {
                AStagesUtil.setBakedModelForState(restriction.original, new AOreBakedModel(entry.getKey(), restriction.original, restriction.replacement));
            }
        }

        // Blocks.EMERALD_ORE.defaultBlockState()

        Minecraft.getInstance().levelRenderer.allChanged();
    }
}
