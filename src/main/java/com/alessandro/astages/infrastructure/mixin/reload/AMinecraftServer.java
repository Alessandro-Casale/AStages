package com.alessandro.astages.infrastructure.mixin.reload;

import com.alessandro.astages.api.nullability.NotNullParams;
import com.alessandro.astages.api.plugin.AStagesPlugin;
import com.alessandro.astages.api.reload.McReloadPhase;
import com.alessandro.astages.api.reload.ReloadContext;
import com.alessandro.astages.engine.PluginManager;
import net.minecraft.server.MinecraftServer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Collection;
import java.util.concurrent.CompletableFuture;

@NotNullParams
@Mixin(MinecraftServer.class)
public class AMinecraftServer {
//    @Inject(method = "spin", at = @At("HEAD"))
//    private static <S> void astages(Function<Thread, S> pThreadFunction, CallbackInfoReturnable<S> cir) {
//
//    }

    @Inject(method = "reloadResources", at = @At("HEAD"))
    public void astages$onReloadResourcesStart(Collection<String> pSelectedIds, CallbackInfoReturnable<CompletableFuture<Void>> cir) {
        var context = new ReloadContext((MinecraftServer) (Object) this);
        PluginManager.callMethod(McReloadPhase.RELOAD_STARTED, context, AStagesPlugin::onReload);
    }

    @Inject(method = "reloadResources", at = @At("RETURN"))
    public void astages$onReloadResourcesComplete(Collection<String> selectedIds, CallbackInfoReturnable<CompletableFuture<Void>> cir) {
        cir.getReturnValue()
            .thenRun(() -> {
                var context = new ReloadContext((MinecraftServer) (Object) this);
                PluginManager.callMethod(McReloadPhase.RELOAD_FINISHED, context, AStagesPlugin::onReload);
            });
    }
}