package com.alessandro.astages.mixin.recipe.mekanism;

import com.alessandro.astages.capability.BlockStageProvider;
import com.alessandro.astages.core.ARecipeManager;
import com.alessandro.astages.core.ARestrictionManager;
import com.alessandro.astages.util.AStagesUtil;
import mekanism.api.recipes.MekanismRecipe;
import mekanism.api.recipes.cache.CachedRecipe;
import mekanism.common.recipe.lookup.IRecipeLookupHandler;
import mekanism.common.recipe.lookup.monitor.RecipeCacheLookupMonitor;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.block.entity.BlockEntity;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(value = RecipeCacheLookupMonitor.class, remap = false)
public abstract class ARecipeCacheLookupMonitor<RECIPE extends MekanismRecipe> {
    @Shadow protected CachedRecipe<RECIPE> cachedRecipe;

    @Shadow public abstract @Nullable CachedRecipe<RECIPE> getCachedRecipe(int cacheIndex);

    @Shadow @Final protected int cacheIndex;

    @Shadow @Final private IRecipeLookupHandler<RECIPE> handler;

    @Inject(method = "updateAndProcess()Z", at = @At("HEAD"), cancellable = true)
    public void astages$updateAndProcess(CallbackInfoReturnable<Boolean> cir) {
        cachedRecipe = getCachedRecipe(cacheIndex);

        if (cachedRecipe != null && handler instanceof BlockEntity blockEntity) {
            blockEntity.getCapability(BlockStageProvider.BLOCK_STAGE).ifPresent(blockStage -> {
                if (blockEntity.getLevel() != null && blockEntity.getLevel().getServer() != null) {
                    Player player = AStagesUtil.getPlayerFromUUID(blockEntity.getLevel().getServer(), blockStage.getOwner());
                    var restriction = ARestrictionManager.RECIPE_INSTANCE.getRestriction(player, new ARecipeManager.RecipeWrapper(cachedRecipe.getRecipe().getType(), cachedRecipe.getRecipe().getId()));

                    if (restriction != null) {
                        cir.setReturnValue(false);
                    }
                }
            });
        }
    }
}
