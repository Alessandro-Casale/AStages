package com.alessandro.astages.mixin.recipe.minecraft;

import com.alessandro.astages.capability.BlockStageProvider;
import com.alessandro.astages.core.ARecipeManager;
import com.alessandro.astages.core.ARecipeRestriction;
import com.alessandro.astages.core.ARestrictionManager;
import com.alessandro.astages.util.AStagesUtil;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.AbstractFurnaceBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

import java.util.UUID;
import java.util.concurrent.atomic.AtomicReference;

@Mixin(value = AbstractFurnaceBlockEntity.class)
public class AAbstractFurnaceBlockEntity {
    @Inject(method = "serverTick", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/block/entity/AbstractFurnaceBlockEntity;getMaxStackSize()I"), locals = LocalCapture.CAPTURE_FAILHARD, cancellable = true)
    private static void astages$serverTick(@NotNull Level level, BlockPos pos, BlockState pState, AbstractFurnaceBlockEntity blockEntity, CallbackInfo ci, boolean flag, boolean flag1, ItemStack itemstack, boolean flag2, boolean flag3, Recipe recipe) {
        if (level.getServer() == null) { return; }

        AtomicReference<UUID> atomicOwner = new AtomicReference<>();
        blockEntity.getCapability(BlockStageProvider.BLOCK_STAGE).ifPresent(blockStage -> atomicOwner.set(blockStage.getOwner()));

        UUID blockOwner = atomicOwner.get();
        Player player = AStagesUtil.getPlayerFromUUID(level.getServer(), blockOwner);
        if (player == null || recipe == null) { return; }

        ARecipeRestriction restriction = ARestrictionManager.RECIPE_INSTANCE.getRestriction(player, new ARecipeManager.RecipeWrapper(recipe.getType(), recipe.getId()));

        if (restriction != null) {
            ci.cancel();
        }
    }
}
