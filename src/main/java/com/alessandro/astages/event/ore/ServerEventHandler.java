package com.alessandro.astages.event.ore;

import com.alessandro.astages.AStages;
import com.alessandro.astages.core.ARestrictionManager;
import net.minecraft.core.BlockPos;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.common.ForgeHooks;
import net.minecraftforge.event.entity.living.LivingDropsEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.event.level.BlockEvent;
import net.minecraftforge.eventbus.api.Event;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import org.jetbrains.annotations.NotNull;

@Mod.EventBusSubscriber(modid = AStages.MODID)
public class ServerEventHandler {
    @SubscribeEvent
    public static void onBlockBroken(BlockEvent.@NotNull BreakEvent event) {
        var restriction = ARestrictionManager.ORE_INSTANCE.getRestriction(event.getState());

        if (restriction != null) {
            event.setExpToDrop(0);
            // event.
            Block.dropResources(restriction.replacement, event.getPlayer().level(), event.getPos());
            // restriction.replacement.getBlock().getDrops(restriction.replacement, event.getLevel(), event.getPos(), )
            // restriction.replacement.canHarvestBlock(event.getLevel(), event.getPos(), event.getPlayer());
        }
    }

    @SubscribeEvent
    public static void breakSpeed(PlayerEvent.@NotNull BreakSpeed event) {
        var restriction = ARestrictionManager.ORE_INSTANCE.getRestriction(event.getState());

        // restriction.replacement.getBlock().getDrops()

        if (restriction != null && event.getPosition().isPresent()) {
            event.setNewSpeed(restriction.replacement.getDestroySpeed(event.getEntity().level(), event.getPosition().get()));
        }
    }

    @SubscribeEvent
    public static void harvestDrop(PlayerEvent.@NotNull HarvestCheck event) {
        // event.setCanHarvest(event.getTargetBlock().canHarvestBlock(event.getEntity().level(), new BlockPos(0, 0, 0), event.getEntity()));
    }

    @SubscribeEvent
    public static void harvestDropE(PlayerEvent.HarvestCheck event) {
        //event.
        // event.setCanHarvest(event.getTargetBlock().canHarvestBlock(event.getEntity().level(), new BlockPos(0, 0, 0), event.getEntity()));
    }

//    public static void onDrop(Block) {
//        // Block.getDrops();
//    }

    @SubscribeEvent
    public static void onBlockUsed(@NotNull PlayerInteractEvent event) {
        var blockState = event.getLevel().getBlockState(event.getPos());
        var restriction = ARestrictionManager.ORE_INSTANCE.getRestriction(blockState);

        if (restriction != null) {
            event.setResult(Event.Result.DENY);
        }
    }
}
