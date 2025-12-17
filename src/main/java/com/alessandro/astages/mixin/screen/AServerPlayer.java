package com.alessandro.astages.mixin.screen;

import com.alessandro.astages.api.holder.AHolder;
import com.alessandro.astages.api.nullability.NotNullParams;
import com.alessandro.astages.core.ARestrictionManager;
import com.alessandro.astages.core.server.restriction.AScreenRestriction;
import com.alessandro.astages.store.Attributes;
import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.level.block.entity.BlockEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.OptionalInt;
import java.util.function.Consumer;

@NotNullParams
@Mixin(value = ServerPlayer.class)
public abstract class AServerPlayer {
    @Shadow private int containerCounter;

    @Shadow public abstract ServerLevel serverLevel();

    @Unique
    private ServerPlayer serverPlayer$self() {
        return (ServerPlayer) (Object) this;
    }

    @Inject(method = "openMenu(Lnet/minecraft/world/MenuProvider;Ljava/util/function/Consumer;)Ljava/util/OptionalInt;", at = @At(value = "INVOKE", target = "Lnet/minecraft/server/network/ServerGamePacketListenerImpl;send(Lnet/minecraft/network/protocol/Packet;)V"), cancellable = true)
    public void astages$openMenu(MenuProvider menu, Consumer<RegistryFriendlyByteBuf> extraDataWriter, CallbackInfoReturnable<OptionalInt> cir, @Local AbstractContainerMenu abstractcontainermenu) {
        AScreenRestriction restriction;
        if (menu instanceof BlockEntity entity) {
            restriction = ARestrictionManager.SCREEN_INSTANCE.getRestriction(AHolder.serverAndPlayer(serverPlayer$self()), abstractcontainermenu, serverLevel().getBlockState(entity.getBlockPos()), entity);
        } else {
            restriction = ARestrictionManager.SCREEN_INSTANCE.getRestriction(AHolder.serverAndPlayer(serverPlayer$self()), abstractcontainermenu, null, null);
        }

        if (restriction != null) {
            restriction.displayMessage(Attributes.Screen.OPEN_MESSAGE, abstractcontainermenu.getType(), serverPlayer$self());
            cir.setReturnValue(OptionalInt.of(containerCounter));
        }
    }
}
