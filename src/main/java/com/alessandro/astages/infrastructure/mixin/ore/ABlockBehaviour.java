package com.alessandro.astages.infrastructure.mixin.ore;

import com.alessandro.astages.api.holder.AClientHolder;
import com.alessandro.astages.api.holder.AHolder;
import com.alessandro.astages.engine.AClientRestrictionManager;
import com.alessandro.astages.engine.ARestrictionManager;
import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

@Mixin(BlockBehaviour.BlockStateBase.class)
public class ABlockBehaviour {
    @ModifyArg(method = "getDestroyProgress", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/block/Block;getDestroyProgress(Lnet/minecraft/world/level/block/state/BlockState;Lnet/minecraft/world/entity/player/Player;Lnet/minecraft/world/level/BlockGetter;Lnet/minecraft/core/BlockPos;)F"))
    public BlockState astages$getDestroyProgress(BlockState original, @Local(argsOnly = true) Player player) {
        if (player instanceof ServerPlayer serverPlayer) {
            return ARestrictionManager.ORE_INSTANCE.getReplacement(AHolder.serverAndPlayer(serverPlayer), original);
        } else {
            return AClientRestrictionManager.ORE_INSTANCE.getReplacement(AClientHolder.serverAndPlayer(), original);
        }
    }
}