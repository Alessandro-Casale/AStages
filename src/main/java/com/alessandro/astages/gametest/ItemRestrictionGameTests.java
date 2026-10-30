package com.alessandro.astages.gametest;

import com.alessandro.astages.AStages;
import com.alessandro.astages.api.holder.AHolder;
import com.alessandro.astages.api.nullability.NotNullParams;
import com.alessandro.astages.api.util.AStagesUtils;
import com.alessandro.astages.engine.ARestrictionManager;
import net.minecraft.core.BlockPos;
import net.minecraft.gametest.framework.GameTest;
import net.minecraft.gametest.framework.GameTestHelper;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.entity.ChestBlockEntity;
import net.minecraftforge.gametest.GameTestHolder;
import net.minecraftforge.gametest.PrefixGameTestTemplate;

@NotNullParams
@GameTestHolder(value = AStages.MODID)
public class ItemRestrictionGameTests {
    @PrefixGameTestTemplate(false)
    @GameTest(template = "empty")
    public static void restriction(GameTestHelper helper) {
        var player = helper.makeMockSurvivalPlayer();

        var restriction = ARestrictionManager.ITEM_INSTANCE.getRestriction(AHolder.player(player), Items.ACACIA_BOAT.getDefaultInstance());
        helper.assertTrue(restriction != null, "Item restriction is null");

        AStagesUtils.addStage(AHolder.player(player), "stage_item_1", true);
        restriction = ARestrictionManager.ITEM_INSTANCE.getRestriction(AHolder.player(player), Items.ACACIA_BOAT.getDefaultInstance());
        helper.assertTrue(restriction == null, "Item restriction is NOT null");

        helper.succeed();
    }

    @PrefixGameTestTemplate(false)
    @GameTest(template = "empty")
    public static void containerCheck(GameTestHelper helper) {
//        var player = helper.makeMockSurvivalPlayer();
//
//        if (helper.getBlockEntity(new BlockPos(1, 1, 1)) instanceof ChestBlockEntity chest) {
//            chest.setItem(0, Items.ACACIA_PLANKS.getDefaultInstance());
//
//            var restriction = ARestrictionManager.ITEM_INSTANCE.getContainerRestriction(AHolder.player(player), Items.ACACIA_PLANKS.getDefaultInstance(), chest.);
//
//        } else {
//            helper.fail("Expected Chest at [1, 1, 1]");
//        }
    }
}
