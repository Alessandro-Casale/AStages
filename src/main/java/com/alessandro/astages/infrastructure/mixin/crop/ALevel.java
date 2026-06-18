package com.alessandro.astages.infrastructure.mixin.crop;

import com.alessandro.astages.api.nullability.NotNullParams;
import com.alessandro.astages.infrastructure.capability.AProvider;
import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.CropBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.chunk.LevelChunk;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@NotNullParams
@Mixin(Level.class)
public class ALevel {
    @Inject(method = "setBlock(Lnet/minecraft/core/BlockPos;Lnet/minecraft/world/level/block/state/BlockState;II)Z", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/chunk/LevelChunk;setBlockState(Lnet/minecraft/core/BlockPos;Lnet/minecraft/world/level/block/state/BlockState;Z)Lnet/minecraft/world/level/block/state/BlockState;"))
    public void astages$setBlock(BlockPos pos, BlockState newState, int flags, int recursionLeft, CallbackInfoReturnable<Boolean> cir, @Local(ordinal = 1) BlockState oldState) {
        Level level = (Level) (Object) this;

        if (oldState.getBlock() instanceof CropBlock && !oldState.is(newState.getBlock())) {
            if (level.getChunk(pos) instanceof LevelChunk chunk) {
                if (chunk.hasData(AProvider.BLOCK_OWNER.get())) {
                    var data = chunk.getData(AProvider.BLOCK_OWNER.get());
                    data.blockMap().remove(pos.asLong());
                    chunk.setUnsaved(true);
                }
            }
        }
    }
}
