package com.alessandro.astages.infrastructure.mixin.recipe.minecraft;

import com.alessandro.astages.api.holder.AHolder;
import com.alessandro.astages.api.util.APlayerUtils;
import com.alessandro.astages.api.wrapper.RecipeWrapper;
import com.alessandro.astages.engine.ARestrictionManager;
import com.alessandro.astages.infrastructure.capability.AProvider;
import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.item.crafting.CraftingRecipe;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.level.block.CrafterBlock;
import net.minecraft.world.level.block.entity.CrafterBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(CrafterBlock.class)
public class ACrafterBlock {
    @Inject(method = "dispenseFrom", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/item/crafting/RecipeHolder;value()Lnet/minecraft/world/item/crafting/Recipe;", ordinal = 0), cancellable = true)
    public void astages$dispenseFrom(BlockState state, @NotNull ServerLevel level, BlockPos pos, CallbackInfo ci, @Local @NotNull CrafterBlockEntity crafter, @Local RecipeHolder<CraftingRecipe> recipe) {
        var uuid = crafter.getData(AProvider.BLOCK_ENTITY_OWNER).getOwner();
        var player = APlayerUtils.getPlayerFromUUID(level.getServer(), uuid);

        if (player != null) {
            var restriction = ARestrictionManager.RECIPE_INSTANCE.getRestriction(AHolder.serverAndPlayer(player), new RecipeWrapper(recipe.value().getType(), recipe.id()));

            if (restriction != null) {
                level.levelEvent(1050, pos, 0);
                ci.cancel();
            }
        }
    }
}
