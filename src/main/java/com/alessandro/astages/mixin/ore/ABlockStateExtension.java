package com.alessandro.astages.mixin.ore;

import com.alessandro.astages.api.develop.Info;
import com.alessandro.astages.api.develop.UnderDevelopment;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;

// @Mixin(IBlockStateExtension.class)
@Mixin(BlockState.class)
@Info("Missing EXP correct!")
@UnderDevelopment
public class ABlockStateExtension {
//    @Unique
//    private IBlockStateExtension blockStateExtension$self() {
//        return (IBlockStateExtension) (Object) this;
//    }
//
//    @Inject(method = "get", at = @At("HEAD"), cancellable = true)
//    public void astages$getExpDrop(LevelAccessor level, BlockPos pos, BlockEntity blockEntity, Entity breaker, ItemStack tool, CallbackInfoReturnable<Integer> cir) {
//        if (breaker instanceof ServerPlayer player) {
//            var restriction = ARestrictionManager.ORE_INSTANCE.getRestriction(player, level.getBlockState(pos));
//
//            if (restriction != null) {
//                cir.setReturnValue(restriction.getReplacement().getExpDrop(level, pos, blockEntity, breaker, tool));
//            }
//        }
//    }
}
