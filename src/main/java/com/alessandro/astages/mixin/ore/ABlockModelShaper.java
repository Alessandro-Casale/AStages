package com.alessandro.astages.mixin.ore;

import com.alessandro.astages.api.holder.AClientHolder;
import com.alessandro.astages.core.AClientRestrictionManager;
import net.minecraft.client.renderer.block.BlockModelShaper;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

@Mixin(BlockModelShaper.class)
public class ABlockModelShaper {
    @ModifyArg(method = "getBlockModel", at = @At(value = "INVOKE", target = "Ljava/util/Map;get(Ljava/lang/Object;)Ljava/lang/Object;"), index = 0)
    public Object astages$getBlockModel(Object key) {
        if (key instanceof BlockState original) {
            return AClientRestrictionManager.ORE_INSTANCE.getReplacement(AClientHolder.serverAndPlayer(), original);
        }

        return key;
    }
}