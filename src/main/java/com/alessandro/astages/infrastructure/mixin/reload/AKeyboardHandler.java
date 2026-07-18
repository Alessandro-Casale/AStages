package com.alessandro.astages.infrastructure.mixin.reload;

import com.alessandro.astages.api.plugin.AStagesPlugin;
import com.alessandro.astages.api.reload.ClientReloadContext;
import com.alessandro.astages.api.reload.ClientReloadPhase;
import com.alessandro.astages.engine.PluginManager;
import net.minecraft.client.KeyboardHandler;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@SuppressWarnings("all")
@Mixin(value = KeyboardHandler.class, priority = 999)
public class AKeyboardHandler {
    @Inject(method = "handleDebugKeys", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/Minecraft;reloadResourcePacks()Ljava/util/concurrent/CompletableFuture;", shift = At.Shift.BEFORE))
    public void astages$handleDebugKeys(int key, CallbackInfoReturnable<Boolean> cir) {
        var context = new ClientReloadContext();
        PluginManager.callMethod(ClientReloadPhase.ASSETS_RELOAD_STARTED, context, AStagesPlugin::onClientReload, AStagesPlugin::getDescriptionForClientReload);
    }
}
