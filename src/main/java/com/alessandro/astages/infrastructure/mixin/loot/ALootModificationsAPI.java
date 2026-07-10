package com.alessandro.astages.infrastructure.mixin.loot;

import com.alessandro.astages.api.nullability.NotNullParams;
import com.alessandro.astages.api.loot.ALootProcessor;
import com.almostreliable.lootjs.LootModificationsAPI;
import com.almostreliable.lootjs.core.LootBucket;
import net.minecraft.world.level.storage.loot.LootContext;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@NotNullParams
@Mixin(LootModificationsAPI.class)
public class ALootModificationsAPI {
    @Inject(method = "runModifiers", at = @At("TAIL"))
    private static void astages$runModifiers(LootContext context, LootBucket lootBucket, CallbackInfo ci) {
        ALootProcessor.apply(lootBucket.iterator(), context);
    }
}
