package com.alessandro.astages.infrastructure.hook.capability;

import com.alessandro.astages.AStages;
import com.alessandro.astages.api.nullability.NotNullParams;
import com.alessandro.astages.infrastructure.capability.AProvider;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Player;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.level.BlockEvent;

@NotNullParams
@EventBusSubscriber(modid = AStages.MODID)
public class CapabilityEvents {
    @SubscribeEvent
    public static void blockPlaced(BlockEvent.EntityPlaceEvent event) {
        if (event.getEntity() instanceof Player player) {
            BlockPos pos = event.getPos();

            var blockEntity = event.getLevel().getBlockEntity(pos);
            if (blockEntity != null) {
                var data = blockEntity.getData(AProvider.BLOCK_STAGE);
                data.setOwner(player.getUUID());
            }
        }
    }
}
