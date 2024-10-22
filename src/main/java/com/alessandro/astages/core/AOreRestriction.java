package com.alessandro.astages.core;

import com.alessandro.astages.util.ARestriction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.state.BlockState;

public class AOreRestriction implements ARestriction {
    public final String id;

    public BlockState original;
    public BlockState replacement;


    public AOreRestriction(String id) {
        this.id = id;
    }

    public AOreRestriction restrict(BlockState original, BlockState replacement) {
        this.replacement = replacement;
        this.original = original;

        return this;
    }

    public boolean isRestricted(BlockState original) {
        return this.original.equals(original);
    }
}
