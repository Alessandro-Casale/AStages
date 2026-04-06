package com.alessandro.astages.infrastructure.mixin.recipe.minecraft;

import com.alessandro.astages.api.holder.AHolder;
import com.alessandro.astages.api.nullability.NotNullParams;
import com.alessandro.astages.engine.ARestrictionManager;
import com.alessandro.astages.api.wrapper.RecipeWrapper;
import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.ItemInteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.CampfireCookingRecipe;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.CampfireBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Optional;

@NotNullParams
@Mixin(CampfireBlock.class)
public class ACampfireBlock {
    @SuppressWarnings("OptionalUsedAsFieldOrParameterType")
    @Inject(method = "useItemOn", at = @At(value = "INVOKE", target = "Ljava/util/Optional;isPresent()Z"), cancellable = true)
    public void astages$use(ItemStack stack, BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hitResult, CallbackInfoReturnable<ItemInteractionResult> cir, @Local Optional<RecipeHolder<CampfireCookingRecipe>> optional) {
        if (optional.isPresent()) {
            var recipe = optional.get();
            var restriction = ARestrictionManager.RECIPE_INSTANCE.getRestriction(AHolder.serverAndPlayer(player), new RecipeWrapper(recipe.value().getType(), recipe.id()));

            if (restriction != null) {
                cir.setReturnValue(ItemInteractionResult.CONSUME);
            }
        }
    }
}
