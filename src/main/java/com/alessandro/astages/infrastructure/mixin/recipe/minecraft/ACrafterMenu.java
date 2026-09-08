package com.alessandro.astages.infrastructure.mixin.recipe.minecraft;

import com.alessandro.astages.api.holder.AHolder;
import com.alessandro.astages.api.wrapper.RecipeWrapper;
import com.alessandro.astages.engine.ARestrictionManager;
import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.inventory.CrafterMenu;
import net.minecraft.world.inventory.ResultContainer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.CraftingInput;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.CrafterBlock;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(CrafterMenu.class)
public class ACrafterMenu {
    @Shadow @Final private ResultContainer resultContainer;

    @Inject(method = "refreshRecipeResult", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/block/CrafterBlock;getPotentialResults(Lnet/minecraft/world/level/Level;Lnet/minecraft/world/item/crafting/CraftingInput;)Ljava/util/Optional;"), cancellable = true)
    private void astages$refreshRecipeResult(@NotNull CallbackInfo ci, @Local ServerPlayer serverPlayer, @Local Level level, @Local CraftingInput craftingInput) {
        ItemStack itemstack = CrafterBlock.getPotentialResults(level, craftingInput).map((recipeHolder) -> {
            var recipe = recipeHolder.value();
            var restriction = ARestrictionManager.RECIPE_INSTANCE.getRestriction(AHolder.serverAndPlayer(serverPlayer), new RecipeWrapper(recipe.getType(), recipeHolder.id()));

            if (restriction != null) { return ItemStack.EMPTY; }
            return recipe.assemble(craftingInput, level.registryAccess());
        }).orElse(ItemStack.EMPTY);

        this.resultContainer.setItem(0, itemstack);
        ci.cancel();
    }
}
