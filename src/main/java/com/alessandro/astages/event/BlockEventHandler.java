package com.alessandro.astages.event;

import com.alessandro.astages.AStages;
import com.alessandro.astages.capability.BlockStageProvider;
import mekanism.common.registries.MekanismTileEntityTypes;
import mekanism.common.tile.base.CapabilityTileEntity;
import mekanism.common.tile.base.TileEntityUpdateable;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.level.BlockEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

@Mod.EventBusSubscriber(modid = AStages.MODID)
public class BlockEventHandler {
    @SubscribeEvent(priority = EventPriority.LOWEST)
    public static void onAttachedCapabilities(@NotNull AttachCapabilitiesEvent<BlockEntity> event) {
        // if (!event.getObject().getCapability(BlockStageProvider.BLOCK_STAGE).isPresent()) {
        event.addCapability(new ResourceLocation(AStages.MODID, "owners"), new BlockStageProvider());
        // }
    }

    @SubscribeEvent
    public static void blockPlaced(BlockEvent.@NotNull EntityPlaceEvent event) {
        if (event.getEntity() instanceof Player player) {
            BlockPos pos = event.getPos();

            if (event.getLevel().getBlockEntity(pos) != null) {
                Objects.requireNonNull(event.getLevel().getBlockEntity(pos)).getCapability(BlockStageProvider.BLOCK_STAGE).ifPresent(blockStage -> {
                    blockStage.setOwner(player.getUUID());
                });
            }
        }
    }
}
