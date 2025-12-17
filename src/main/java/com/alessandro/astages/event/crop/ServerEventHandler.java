package com.alessandro.astages.event.crop;

import com.alessandro.astages.AStages;
import com.alessandro.astages.api.APlayerUtils;
import com.alessandro.astages.api.holder.AHolder;
import com.alessandro.astages.api.nullability.NotNullParams;
import com.alessandro.astages.core.ARestrictionManager;
import com.alessandro.astages.core.server.restriction.ACropRestriction;
import com.alessandro.astages.core.wrapper.CropWrapper;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.CropBlock;
import net.minecraft.world.phys.Vec3;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.level.block.CropGrowEvent;

@NotNullParams
@EventBusSubscriber(modid = AStages.MODID)
public class ServerEventHandler {
    @SubscribeEvent
    public static void onCropGrowth(CropGrowEvent.Post event) {
        if (!event.getLevel().isClientSide()) {
            var pos = event.getPos();
            var nearestPlayer = APlayerUtils.getNearestPlayer((Level) event.getLevel(), new Vec3(pos.getX(), pos.getY(), pos.getZ()));
            var level = event.getLevel();

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
}
