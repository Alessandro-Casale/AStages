package com.alessandro.astages.mixin.recipe.minecraft;

import com.alessandro.astages.api.holder.AHolder;
import com.alessandro.astages.api.nullability.NotNullParams;
import com.alessandro.astages.core.ARestrictionManager;
import com.alessandro.astages.core.wrapper.RecipeWrapper;
import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.CraftingContainer;
import net.minecraft.world.inventory.CraftingMenu;
import net.minecraft.world.inventory.ResultContainer;
import net.minecraft.world.item.crafting.CraftingRecipe;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Optional;

@NotNullParams
@Mixin(CraftingMenu.class)
public class ACraftingMenu {
    @SuppressWarnings("OptionalUsedAsFieldOrParameterType")
    @Inject(method = "slotChangedCraftingGrid", at = @At(value = "INVOKE", target = "Ljava/util/Optional;get()Ljava/lang/Object;"), cancellable = true)
    private static void astages$slotChanged(AbstractContainerMenu menu, Level level, Player player, CraftingContainer container, ResultContainer resultSlots, RecipeHolder<CraftingRecipe> recipe, CallbackInfo ci, @Local ServerPlayer serverPlayer, @Local Optional<RecipeHolder<CraftingRecipe>> optional) {
        if (optional.isPresent()) {
            var rec = optional.get();
            var restriction = ARestrictionManager.RECIPE_INSTANCE.getRestriction(AHolder.serverAndPlayer(serverPlayer), new RecipeWrapper(rec.value().getType(), rec.id()));

            if (restriction != null) {
                resultSlots.clearContent();
                ci.cancel();
            }
        }
    }
}
