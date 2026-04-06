package com.alessandro.astages.infrastructure.mixin.ore;

import net.minecraft.world.level.lighting.LightEngine;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(LightEngine.class)
public class ALightEngine {
//    @ModifyVariable(method = "hasDifferentLightProperties", at = @At("HEAD"), argsOnly = true, ordinal = 0)
//    private static BlockState state1(BlockState original) {
//        return AClientRestrictionManager.ORE_INSTANCE.getReplacement(original);
//    }
//
//    @ModifyVariable(method = "hasDifferentLightProperties", at = @At("HEAD"), argsOnly = true, ordinal = 1)
//    private static BlockState state2(BlockState original) {
//        return AClientRestrictionManager.ORE_INSTANCE.getReplacement(original);
//    }
//
//    @ModifyVariable(method = "getLightBlockInto", at = @At("HEAD"), argsOnly = true, ordinal = 0)
//    private static BlockState state3(BlockState original) {
//        return AClientRestrictionManager.ORE_INSTANCE.getReplacement(original);
//    }
//
//    @ModifyVariable(method = "getLightBlockInto", at = @At("HEAD"), argsOnly = true, ordinal = 1)
//    private static BlockState state4(BlockState original) {
//        return AClientRestrictionManager.ORE_INSTANCE.getReplacement(original);
//    }
//
//    @ModifyVariable(method = "getOcclusionShape(Lnet/minecraft/world/level/BlockGetter;Lnet/minecraft/core/BlockPos;Lnet/minecraft/world/level/block/state/BlockState;Lnet/minecraft/core/Direction;)Lnet/minecraft/world/phys/shapes/VoxelShape;", at = @At("HEAD"), argsOnly = true)
//    private static BlockState state5(BlockState original) {
//        return AClientRestrictionManager.ORE_INSTANCE.getReplacement(original);
//    }
//
//    @ModifyVariable(method = "isEmptyShape", at = @At("HEAD"), argsOnly = true)
//    private static BlockState state6(BlockState original) {
//        return AClientRestrictionManager.ORE_INSTANCE.getReplacement(original);
//    }
//
//    @ModifyReturnValue(method = "getState", at = @At("RETURN"))
//    private BlockState state7(BlockState original) {
//        return AClientRestrictionManager.ORE_INSTANCE.getReplacement(original);
//    }
//
//    @ModifyVariable(method = "getOpacity", at = @At("HEAD"), argsOnly = true)
//    private BlockState state8(BlockState original) {
//        return AClientRestrictionManager.ORE_INSTANCE.getReplacement(original);
//    }
//
//    @ModifyVariable(method = "shapeOccludes", at = @At("HEAD"), argsOnly = true, ordinal = 0)
//    private BlockState state9(BlockState original) {
//        return AClientRestrictionManager.ORE_INSTANCE.getReplacement(original);
//    }
//
//    @ModifyVariable(method = "shapeOccludes", at = @At("HEAD"), argsOnly = true, ordinal = 1)
//    private BlockState state10(BlockState original) {
//        return AClientRestrictionManager.ORE_INSTANCE.getReplacement(original);
//    }
//
//    @ModifyVariable(method = "getOcclusionShape(Lnet/minecraft/world/level/block/state/BlockState;JLnet/minecraft/core/Direction;)Lnet/minecraft/world/phys/shapes/VoxelShape;", at = @At("HEAD"), argsOnly = true)
//    private BlockState state11(BlockState original) {
//        return AClientRestrictionManager.ORE_INSTANCE.getReplacement(original);
//    }
}