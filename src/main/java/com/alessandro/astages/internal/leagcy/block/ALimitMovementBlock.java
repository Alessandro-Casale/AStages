package com.alessandro.astages.internal.leagcy.block;

import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockBehaviour;

@Deprecated(forRemoval = true)
public class ALimitMovementBlock extends Block {
    public ALimitMovementBlock() {
        super(BlockBehaviour.Properties.of().strength(-1.0F, 3600000.0F));
    }
}
