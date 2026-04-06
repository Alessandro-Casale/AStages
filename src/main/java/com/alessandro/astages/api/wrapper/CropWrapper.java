package com.alessandro.astages.api.wrapper;

import net.minecraft.world.level.block.state.BlockState;

public record CropWrapper(BlockState crop, Integer age) { }
