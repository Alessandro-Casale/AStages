package com.alessandro.astages.infrastructure.hook.restriction;

import com.alessandro.astages.AStages;
import com.alessandro.astages.api.holder.AHolder;
import com.alessandro.astages.api.nullability.NotNullParams;
import com.alessandro.astages.api.util.APlayerUtils;
import com.alessandro.astages.api.wrapper.CropWrapper;
import com.alessandro.astages.engine.ARestrictionManager;
import com.alessandro.astages.engine.server.restriction.ACropRestriction;
import com.alessandro.astages.infrastructure.capability.AProvider;
import com.alessandro.astages.infrastructure.capability.BlockOwner;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.CropBlock;
import net.minecraft.world.level.chunk.LevelChunk;
import net.minecraft.world.phys.Vec3;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.level.BlockEvent;
import net.neoforged.neoforge.event.level.block.CropGrowEvent;

import java.util.UUID;

@NotNullParams
@EventBusSubscriber(modid = AStages.MODID)
public class CropServerEvents {
    @SubscribeEvent
    public static void onCropGrowth(CropGrowEvent.Post event) {
        if (!event.getLevel().isClientSide()) {
            var pos = event.getPos().above();
            var level = event.getLevel();

            UUID nearestPlayer;
            if (level.getChunk(pos) instanceof LevelChunk chunk && chunk.hasData(AProvider.BLOCK_OWNER.get())) {
                var data = chunk.getData(AProvider.BLOCK_OWNER.get());
                nearestPlayer = data.blockMap().get(pos.asLong());
                AStages.LOGGER.debug("Owner found!");
            } else {
                AStages.LOGGER.debug("Nearest player found!");
                nearestPlayer = APlayerUtils.getNearestPlayer((Level) level, new Vec3(pos.getX(), pos.getY(), pos.getZ())).getUUID();
            }

            ACropRestriction restriction;
            if (event.getOriginalState().getBlock() instanceof CropBlock crop) {
                restriction = ARestrictionManager.CROP_INSTANCE.getRestriction(AHolder.serverAndPlayer(nearestPlayer), new CropWrapper(event.getOriginalState(), crop.getAge(event.getOriginalState())));
            } else {
                restriction = ARestrictionManager.CROP_INSTANCE.getRestriction(AHolder.serverAndPlayer(nearestPlayer), new CropWrapper(event.getOriginalState(), null));
            }

            if (restriction != null) {
                level.setBlock(pos, event.getOriginalState(), Block.UPDATE_ALL);
            }
        }
    }

    @SubscribeEvent
    public static void onBlockPlaced(BlockEvent.EntityPlaceEvent event) {
        if (event.getEntity() instanceof Player player && !event.getLevel().isClientSide()) {
            if (event.getPlacedBlock().getBlock() instanceof CropBlock) {
                var blockPos = event.getPos();
                var pos = blockPos.asLong();

                if (event.getLevel().getChunk(blockPos) instanceof LevelChunk chunk) {
                    BlockOwner data = chunk.getData(AProvider.BLOCK_OWNER.get());
                    data.blockMap().put(pos, player.getUUID());
                    chunk.setUnsaved(true);
                }
            }
        }
    }
}
