package com.alessandro.astages.infrastructure.mixin.recipe.minecraft;

import com.alessandro.astages.infrastructure.capability.BlockStageProvider;
import com.mojang.authlib.GameProfile;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.OptionalInt;

@Mixin(ServerPlayer.class)
public abstract class AServerPlayer extends Player {
    public AServerPlayer(Level level, BlockPos pos, float yRot, GameProfile gameProfile) {
        super(level, pos, yRot, gameProfile);
    }

    @Inject(method = "openMenu", at = @At(value = "INVOKE", target = "Lnet/minecraft/server/level/ServerPlayer;initMenu(Lnet/minecraft/world/inventory/AbstractContainerMenu;)V"))
    public void astages$openMenu(MenuProvider menu, CallbackInfoReturnable<OptionalInt> cir) {
        if (!(menu instanceof BlockEntity blockEntity)) { return; }

        blockEntity.getCapability(BlockStageProvider.BLOCK_STAGE).ifPresent(data -> {
            if (data.getOwner() == null) {
                data.setOwner(getUUID());
            }
        });
    }
}
