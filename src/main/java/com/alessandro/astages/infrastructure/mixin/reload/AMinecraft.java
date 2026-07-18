package com.alessandro.astages.infrastructure.mixin.reload;

import com.alessandro.astages.api.nullability.NotNullParams;
import com.alessandro.astages.api.plugin.AStagesPlugin;
import com.alessandro.astages.api.reload.ClientReloadContext;
import com.alessandro.astages.api.reload.ClientReloadPhase;
import com.alessandro.astages.engine.PluginManager;
import net.minecraft.client.Minecraft;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.concurrent.CompletableFuture;

@NotNullParams
@Mixin(Minecraft.class)
public class AMinecraft {
    @Inject(method = "<init>", at = @At("TAIL"))
    private static void astages$afterInit(CallbackInfo ci) {
        var context = new ClientReloadContext();
        PluginManager.callMethod(ClientReloadPhase.INSTANCE_LOAD_FINISHED, context, AStagesPlugin::onClientReload, AStagesPlugin::getDescriptionForClientReload);
    }

    @Inject(method = "reloadResourcePacks(ZLnet/minecraft/client/Minecraft$GameLoadCookie;)Ljava/util/concurrent/CompletableFuture;", at = @At("RETURN"))
    public void astages$reloadResourcePacks(boolean error, Minecraft.GameLoadCookie gameLoadCookie, CallbackInfoReturnable<CompletableFuture<Void>> cir) {
        cir.getReturnValue()
            .thenRun(() -> {
                var context = new ClientReloadContext();
                PluginManager.callMethod(ClientReloadPhase.ASSETS_RELOAD_FINISHED, context, AStagesPlugin::onClientReload, AStagesPlugin::getDescriptionForClientReload);
            });
    }
}
