package com.alessandro.astages.infrastructure.mixin.ore;

import com.alessandro.astages.api.holder.AHolder;
import com.alessandro.astages.engine.ARestrictionManager;
import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.level.ServerPlayerGameMode;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(value = ServerPlayerGameMode.class)
public class AServerPlayerGameMode {
    @Shadow @Final protected ServerPlayer player;

    @ModifyExpressionValue(method = "handleBlockBreakAction", at = @At(value = "INVOKE", target = "Lnet/minecraft/server/level/ServerLevel;getBlockState(Lnet/minecraft/core/BlockPos;)Lnet/minecraft/world/level/block/state/BlockState;"))
    public BlockState astages$handleBlockBreakAction(BlockState original) {
        return ARestrictionManager.ORE_INSTANCE.getReplacement(AHolder.serverAndPlayer(player), original);
    }

    @ModifyExpressionValue(method = "destroyBlock", at = @At(value = "INVOKE", target = "Lnet/minecraft/server/level/ServerLevel;getBlockState(Lnet/minecraft/core/BlockPos;)Lnet/minecraft/world/level/block/state/BlockState;"))
    public BlockState astages$destroyBlock(BlockState original) {
        return ARestrictionManager.ORE_INSTANCE.getReplacement(AHolder.serverAndPlayer(player), original);
    }

    @ModifyExpressionValue(method = "useItemOn", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/Level;getBlockState(Lnet/minecraft/core/BlockPos;)Lnet/minecraft/world/level/block/state/BlockState;"))
    public BlockState astages$useItemOn(BlockState original) {
        return ARestrictionManager.ORE_INSTANCE.getReplacement(AHolder.serverAndPlayer(player), original);
    }
}
