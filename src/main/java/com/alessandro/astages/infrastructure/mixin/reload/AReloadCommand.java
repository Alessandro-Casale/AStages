package com.alessandro.astages.infrastructure.mixin.reload;

import com.alessandro.astages.api.constant.ReloadType;
import com.alessandro.astages.infrastructure.networking.Networking;
import com.alessandro.astages.infrastructure.networking.packet.reload.RequestReloadS2C;
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
        Networking.sendTo(null, new RequestReloadS2C(ReloadType.RELOAD_BEFORE));
    }
}