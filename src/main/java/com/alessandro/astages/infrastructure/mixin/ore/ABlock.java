package com.alessandro.astages.infrastructure.mixin.ore;

import com.alessandro.astages.api.holder.AClientHolder;
import com.alessandro.astages.engine.AClientRestrictionManager;
import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

@Mixin(Block.class)
public class ABlock {
    @ModifyVariable(method = "shouldRenderFace", at = @At("HEAD"), argsOnly = true)
    private static BlockState astages$shouldRenderFace(BlockState original) {
        return AClientRestrictionManager.ORE_INSTANCE.getReplacement(AClientHolder.serverAndPlayer(), original);
    }

    @ModifyExpressionValue(method = "shouldRenderFace", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/BlockGetter;getBlockState(Lnet/minecraft/core/BlockPos;)Lnet/minecraft/world/level/block/state/BlockState;"))
    private static BlockState astages$getBlockState(BlockState original) {
        return AClientRestrictionManager.ORE_INSTANCE.getReplacement(AClientHolder.serverAndPlayer(), original);
    }
}