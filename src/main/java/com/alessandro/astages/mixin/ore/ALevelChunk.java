package com.alessandro.astages.mixin.ore;

import com.alessandro.astages.api.holder.AClientHolder;
import com.alessandro.astages.config.AStagesClient;
import com.alessandro.astages.core.AClientRestrictionManager;
import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.chunk.LevelChunk;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(LevelChunk.class)
public class ALevelChunk {
    @Shadow @Final Level level;

    @ModifyReturnValue(method = "getBlockState", at = @At("RETURN"))
    public BlockState astages$getBlockState(BlockState original) {
        if (level.isClientSide && AStagesClient.LEVEL_CHUNK_EXPERIMENTAL_SETTINGS.get()) {
            return AClientRestrictionManager.ORE_INSTANCE.getReplacement(AClientHolder.serverAndPlayer(), original);
        }

        return original;
    }
}