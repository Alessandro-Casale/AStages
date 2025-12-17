package com.alessandro.astages.util.underdevelopment.block;

import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockBehaviour;

public class ALimitMovementBlock extends Block {
    public ALimitMovementBlock() {
        super(BlockBehaviour.Properties.of().strength(-1.0F, 3600000.0F));
    }
}
