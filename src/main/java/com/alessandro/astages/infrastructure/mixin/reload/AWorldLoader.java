package com.alessandro.astages.infrastructure.mixin.reload;

import com.alessandro.astages.api.nullability.NotNullParams;
import com.alessandro.astages.api.plugin.AStagesPlugin;
import com.alessandro.astages.api.reload.McReloadPhase;
import com.alessandro.astages.api.reload.ReloadContext;
import com.alessandro.astages.engine.PluginManager;
import net.minecraft.server.WorldLoader;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;

@NotNullParams
@Mixin(WorldLoader.class)
public class AWorldLoader {
    @Inject(method = "load", at = @At("HEAD"))
    private static <D, R> void astages$onWorldLoadStart(WorldLoader.InitConfig initConfig, WorldLoader.WorldDataSupplier<D> worldDataSupplier, WorldLoader.ResultFactory<D, R> resultFactory, Executor backgroundExecutor, Executor mainExecutor, CallbackInfoReturnable<CompletableFuture<R>> cir) {
        var context = new ReloadContext(null, null);
        PluginManager.callMethod(McReloadPhase.WORLD_LOAD_STARTED, context, AStagesPlugin::onReload, AStagesPlugin::getDescriptionForReload);
    }

    @Inject(method = "load", at = @At("RETURN"))
    private static <D, R> void astages$onWorldLoadComplete(WorldLoader.InitConfig initConfig, WorldLoader.WorldDataSupplier<D> worldDataSupplier, WorldLoader.ResultFactory<D, R> resultFactory, Executor backgroundExecutor, Executor mainExecutor, CallbackInfoReturnable<CompletableFuture<R>> cir) {
        cir.getReturnValue()
            .thenRunAsync(() -> {
                var context = new ReloadContext(null, null);
                PluginManager.callMethod(McReloadPhase.WORLD_LOAD_FINISHED, context, AStagesPlugin::onReload, AStagesPlugin::getDescriptionForReload);
            }, mainExecutor);
    }
}