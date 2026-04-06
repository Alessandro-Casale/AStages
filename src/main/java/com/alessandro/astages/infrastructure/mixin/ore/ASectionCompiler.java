package com.alessandro.astages.infrastructure.mixin.ore;

import com.alessandro.astages.api.holder.AClientHolder;
import com.alessandro.astages.engine.AClientRestrictionManager;
import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import net.minecraft.client.renderer.chunk.SectionCompiler;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(SectionCompiler.class)
public class ASectionCompiler { // Replaces AChunkRenderDispatcher
    @ModifyExpressionValue(method = "compile(Lnet/minecraft/core/SectionPos;Lnet/minecraft/client/renderer/chunk/RenderChunkRegion;Lcom/mojang/blaze3d/vertex/VertexSorting;Lnet/minecraft/client/renderer/SectionBufferBuilderPack;Ljava/util/List;)Lnet/minecraft/client/renderer/chunk/SectionCompiler$Results;", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/renderer/chunk/RenderChunkRegion;getBlockState(Lnet/minecraft/core/BlockPos;)Lnet/minecraft/world/level/block/state/BlockState;"))
    public BlockState astages$getBlockState(BlockState original) {
        return AClientRestrictionManager.ORE_INSTANCE.getReplacement(AClientHolder.serverAndPlayer(), original);
    }
}