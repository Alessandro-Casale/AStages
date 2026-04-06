package com.alessandro.astages.api.wrapper;

import net.minecraft.world.level.block.state.BlockState;

public record OreWrapper(BlockState original, BlockState replacement) { }
