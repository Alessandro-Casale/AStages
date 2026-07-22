package com.alessandro.astages.infrastructure.integration.rei;

import com.alessandro.astages.api.constant.AOperation;
import com.alessandro.astages.api.nullability.NotNullParams;
import com.alessandro.astages.infrastructure.integration.rei.predicate.StageRecipeVisibilityHandler;
import me.shedaniel.rei.api.client.plugins.REIClientPlugin;
import me.shedaniel.rei.api.client.registry.display.DisplayRegistry;
import me.shedaniel.rei.forge.REIPluginClient;

import java.util.Set;

@NotNullParams
@REIPluginClient
public class ReiRecipeStagesPlugin implements REIClientPlugin {
    @Override
    public void registerDisplays(DisplayRegistry registry) {
        registry.registerVisibilityPredicate(new StageRecipeVisibilityHandler());
    }

    public static void onReloadStarted() { }

    public static void onReloadFinished() { }

    public static void onStageChanged(AOperation operation, Set<String> syncedStages) { }
}
