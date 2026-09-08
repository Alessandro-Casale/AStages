package com.alessandro.astages.infrastructure.mixin.recipe.minecraft;

import com.alessandro.astages.infrastructure.capability.AProvider;
import com.mojang.authlib.GameProfile;
import net.minecraft.core.BlockPos;
import net.minecraft.network.RegistryFriendlyByteBuf;
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
import java.util.function.Consumer;

@Mixin(ServerPlayer.class)
public abstract class AServerPlayer extends Player {
    public AServerPlayer(Level level, BlockPos pos, float yRot, GameProfile gameProfile) {
        super(level, pos, yRot, gameProfile);
    }

    @Inject(method = "openMenu(Lnet/minecraft/world/MenuProvider;Ljava/util/function/Consumer;)Ljava/util/OptionalInt;", at = @At(value = "INVOKE", target = "Lnet/minecraft/server/level/ServerPlayer;initMenu(Lnet/minecraft/world/inventory/AbstractContainerMenu;)V"))
    public void astages$openMenu(MenuProvider menu, Consumer<RegistryFriendlyByteBuf> extraDataWriter, CallbackInfoReturnable<OptionalInt> cir) {
        if (!(menu instanceof BlockEntity blockEntity)) { return; }

        var data = blockEntity.getData(AProvider.BLOCK_STAGE);
        if (data.getOwner() == null) {
            data.setOwner(getUUID());
        }
    }
}
