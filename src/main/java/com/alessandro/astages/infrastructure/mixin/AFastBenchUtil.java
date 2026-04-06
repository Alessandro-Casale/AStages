package com.alessandro.astages.infrastructure.mixin;

import com.alessandro.astages.api.holder.AHolder;
import com.alessandro.astages.engine.ARestrictionManager;
import com.alessandro.astages.api.wrapper.RecipeWrapper;
import dev.shadowsoffire.fastbench.util.CraftingInventoryExt;
import dev.shadowsoffire.fastbench.util.FastBenchUtil;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.ResultContainer;
import net.minecraft.world.item.crafting.CraftingInput;
import net.minecraft.world.item.crafting.CraftingRecipe;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(FastBenchUtil.class)
public class AFastBenchUtil {
    @Unique private static Player astages$player;

    @Inject(method = "slotChangedCraftingGrid", at = @At("HEAD"), remap = false)
    private static void astages$slotChangedCraftingGrid(Level world, Player player, CraftingInventoryExt inv, ResultContainer result, CallbackInfo ci) {
        AFastBenchUtil.astages$player = player;
    }

    @Inject(method = "findRecipe", at = @At("RETURN"), remap = false, cancellable = true)
    private static void astages$findRecipe(CraftingInput input, Level world, CallbackInfoReturnable<RecipeHolder<CraftingRecipe>> cir) {
        if (astages$player != null) {
            var oldRecipe = cir.getReturnValue();
            if (oldRecipe == null) { return; }

            var restriction = ARestrictionManager.RECIPE_INSTANCE.getRestriction(AHolder.serverAndPlayer(astages$player), new RecipeWrapper(oldRecipe.value().getType(), oldRecipe.id()));

            if (restriction != null) {
                cir.setReturnValue(null);
            }
        }
    }
}
