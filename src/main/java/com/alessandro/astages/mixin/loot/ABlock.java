package com.alessandro.astages.mixin.loot;
import com.alessandro.astages.api.holder.AHolder;
import com.alessandro.astages.core.ARestrictionManager;
import com.alessandro.astages.store.Attributes;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.List;

@Mixin(Block.class)
public class ABlock {
    @Inject(method = "getDrops(Lnet/minecraft/world/level/block/state/BlockState;Lnet/minecraft/server/level/ServerLevel;Lnet/minecraft/core/BlockPos;Lnet/minecraft/world/level/block/entity/BlockEntity;Lnet/minecraft/world/entity/Entity;Lnet/minecraft/world/item/ItemStack;)Ljava/util/List;", at = @At("RETURN"))
    private static void astages$getDrops(BlockState state, ServerLevel level, BlockPos pos, BlockEntity blockEntity, Entity entity, ItemStack tool, CallbackInfoReturnable<List<ItemStack>> cir) {
        if (entity instanceof ServerPlayer player) {
            var iterator = cir.getReturnValue().listIterator();
            var lootTable = state.getBlock().getLootTable().location();

            while (iterator.hasNext()) {
                var stackToCheck = iterator.next();
                var restriction = ARestrictionManager.LOOT_INSTANCE.getRestriction(AHolder.serverAndPlayer(player), stackToCheck, null, lootTable);

                if (restriction != null) {
                    iterator.remove();

                    if (restriction.isEnabled(Attributes.HAS_REPLACER)) {
                        var replacer = restriction.getReplacer().apply(stackToCheck);

                        if (!replacer.isEmpty()) {
                            iterator.add(replacer);
                        }
                    }
                }
            }
        }
    }
}