package com.alessandro.astages.test;

import com.alessandro.astages.AStages;
import com.alessandro.astages.api.AResourceLocation;
import com.alessandro.astages.api.ARestrictionUtils;
import com.alessandro.astages.api.constant.ARestrictionStage;
import com.alessandro.astages.api.event.AddRestrictionEvent;
import com.alessandro.astages.config.AStagesCommon;
import net.minecraft.world.item.Items;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;

@EventBusSubscriber(modid = AStages.MODID)
public class TestEventHandler {
    @SubscribeEvent
    public static void addRestriction(AddRestrictionEvent event) {
        if (!AStagesCommon.ENABLE_TEST_MODE.get()) { return; }
        if (event.getStage() != ARestrictionStage.BEFORE_JS) { return; }

        ARestrictionUtils.addRestrictionForItem("astages:item1", "stage_item_1", Items.ACACIA_BOAT);
        ARestrictionUtils.addRestrictionForItem("astages:item2", "stage_item_2", Items.ACACIA_PLANKS)
            .setCanBeStoredInInventory(true)
            .setCanPickedUp(true)
            .setCanBeStoredInContainers(false);
        ARestrictionUtils.addRestrictionForTag("astages:item3", "stage_item_3", AResourceLocation.fromTag("#forge:ingots/iron"));
    }
}
