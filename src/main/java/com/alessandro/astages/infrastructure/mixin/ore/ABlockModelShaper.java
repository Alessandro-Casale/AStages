package com.alessandro.astages.infrastructure.mixin.ore;

import com.alessandro.astages.api.holder.AClientHolder;
import com.alessandro.astages.engine.AClientRestrictionManager;
import net.minecraft.client.renderer.block.BlockModelShaper;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

@Mixin(BlockModelShaper.class)
public class ABlockModelShaper {
    @ModifyVariable(method = "getBlockModel", at = @At("HEAD"), argsOnly = true)
    public BlockState astage$getBlockModel(BlockState original) {
        return AClientRestrictionManager.ORE_INSTANCE.getReplacement(AClientHolder.serverAndPlayer(), original);
    }
}