package com.alessandro.astages.mixin.reload;

import com.alessandro.astages.networking.ANetworking;
import com.alessandro.astages.networking.packet.reload.RequestReloadS2CPacket;
import com.alessandro.astages.util.ReloadType;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.server.commands.ReloadCommand;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Collection;

@Mixin(ReloadCommand.class)
public class AReloadCommand {
    @Inject(method = "reloadPacks", at = @At("HEAD"))
    private static void astages$reloadPacks(Collection<String> ids, CommandSourceStack source, CallbackInfo ci) {
        ANetworking.sendTo(null, new RequestReloadS2CPacket(ReloadType.RELOAD_BEFORE));
    }
}