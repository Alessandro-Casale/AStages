package com.alessandro.astages.infrastructure.mixin.loot;

import com.alessandro.astages.infrastructure.loot.ALootParams;
import net.minecraft.world.level.storage.loot.LootParams;
import net.minecraft.world.level.storage.loot.parameters.LootContextParam;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSet;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(LootParams.Builder.class)
public abstract class ALootParams$Builder {
    @Shadow
    public abstract <T> LootParams.Builder withOptionalParameter(LootContextParam<T> pParameter, @Nullable T pValue);

    @Inject(method = "create", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/storage/loot/LootParams;<init>(Lnet/minecraft/server/level/ServerLevel;Ljava/util/Map;Ljava/util/Map;F)V"))
    public void astages$create(LootContextParamSet paramSet, CallbackInfoReturnable<LootParams> cir) {
        withOptionalParameter(ALootParams.PARAM_SET, paramSet);
        withOptionalParameter(ALootParams.PARAM_SET_ID, LootContextParamSets.REGISTRY.inverse().get(paramSet));
    }
}