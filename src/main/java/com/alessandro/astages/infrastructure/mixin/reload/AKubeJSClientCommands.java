package com.alessandro.astages.infrastructure.mixin.reload;

import com.alessandro.astages.api.plugin.AStagesPlugin;
import com.alessandro.astages.api.reload.ClientReloadContext;
import com.alessandro.astages.api.reload.ClientReloadPhase;
import com.alessandro.astages.engine.PluginManager;
import dev.latvian.mods.kubejs.command.KubeJSClientCommands;
import net.minecraft.commands.CommandSourceStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(KubeJSClientCommands.class)
public class AKubeJSClientCommands {
    @Inject(method = "reloadClient", at = @At("HEAD"))
    private static void astages$beforeReloadClientScripts(CommandSourceStack source, CallbackInfoReturnable<Integer> cir) {
        var context = new ClientReloadContext();
        PluginManager.callMethod(ClientReloadPhase.ASSETS_RELOAD_STARTED, context, AStagesPlugin::onClientReload, AStagesPlugin::getDescriptionForClientReload);
    }

    @Inject(method = "reloadClient", at = @At("TAIL"))
    private static void astages$afterReloadClientScripts(CommandSourceStack source, CallbackInfoReturnable<Integer> cir) {
        var context = new ClientReloadContext();
        PluginManager.callMethod(ClientReloadPhase.ASSETS_RELOAD_FINISHED, context, AStagesPlugin::onClientReload, AStagesPlugin::getDescriptionForClientReload);
    }
}
