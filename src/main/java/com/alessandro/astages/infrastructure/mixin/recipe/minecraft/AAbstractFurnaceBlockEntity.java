package com.alessandro.astages.infrastructure.mixin.recipe.minecraft;

import com.alessandro.astages.api.holder.AHolder;
import com.alessandro.astages.api.util.APlayerUtils;
import com.alessandro.astages.api.wrapper.RecipeWrapper;
import com.alessandro.astages.engine.ARestrictionManager;
import com.alessandro.astages.infrastructure.capability.AProvider;
import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.AbstractFurnaceBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.AbstractFurnaceBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.UUID;

@Mixin(value = AbstractFurnaceBlockEntity.class)
public class AAbstractFurnaceBlockEntity {
    @Inject(method = "serverTick", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/block/entity/AbstractFurnaceBlockEntity;getMaxStackSize()I"), cancellable = true)
    private static void astages$serverTick(@NotNull Level level, BlockPos pos, BlockState state, AbstractFurnaceBlockEntity blockEntity, CallbackInfo ci, @Local RecipeHolder<?> recipe) {
        if (level.getServer() == null) { return; }

        var blockStage = blockEntity.getData(AProvider.BLOCK_ENTITY_OWNER);

        UUID blockOwner = blockStage.getOwner();
        Player player = APlayerUtils.getPlayerFromUUID(level.getServer(), blockOwner);
        if (player == null || recipe == null) { return; }

        var restriction = ARestrictionManager.RECIPE_INSTANCE.getRestriction(AHolder.serverAndPlayer(player), new RecipeWrapper(recipe.value().getType(), recipe.id()));

        if (restriction != null) {
            ci.cancel();

            level.setBlock(pos, state.setValue(AbstractFurnaceBlock.LIT, false), Block.UPDATE_ALL);
        } else {
            level.setBlock(pos, state.setValue(AbstractFurnaceBlock.LIT, true), Block.UPDATE_ALL);
        }
    }
}
