package com.alessandro.astages.api.util;

import com.alessandro.astages.api.nullability.NotNullParamsAndMethodsReturn;
import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;

@NotNullParamsAndMethodsReturn
public class ABlockStateUtils {
    public static BakedModel getBakedModelFromState(BlockState state) {
        return Minecraft.getInstance().getBlockRenderer().getBlockModel(state);
    }

    public static ItemStack stateToStack(BlockState state) {
        return new ItemStack(state.getBlock());
    }

    public static ItemStack blockToStack(Block block) {
        return new ItemStack(block);
    }

    public static void setBakedModelForState(BlockState state, BakedModel bakedModel) {
        Minecraft.getInstance().getBlockRenderer().getBlockModelShaper().modelByStateCache.put(state, bakedModel);
    }
}
