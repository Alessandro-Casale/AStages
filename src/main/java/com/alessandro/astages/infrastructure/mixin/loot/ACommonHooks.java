package com.alessandro.astages.infrastructure.mixin.loot;

import com.alessandro.astages.engine.loot.ALootProcessor;
import com.alessandro.astages.infrastructure.config.AStagesCommon;
import com.alessandro.astages.infrastructure.integration.Mods;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.storage.loot.LootContext;
import net.neoforged.neoforge.common.CommonHooks;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(value = CommonHooks.class, remap = false)
public class ACommonHooks {
    @Inject(method = "modifyLoot(Lnet/minecraft/resources/ResourceLocation;Lit/unimi/dsi/fastutil/objects/ObjectArrayList;Lnet/minecraft/world/level/storage/loot/LootContext;)Lit/unimi/dsi/fastutil/objects/ObjectArrayList;", at = @At("TAIL"))
    private static void astages$modifyLoot(ResourceLocation lootTableId, ObjectArrayList<ItemStack> generatedLoot, LootContext context, CallbackInfoReturnable<ObjectArrayList<ItemStack>> cir) {
        if (!Mods.LOOTJS.isLoaded() && AStagesCommon.FORCE_LAST_LOOT_MODIFIER.get()) {
            ALootProcessor.apply(generatedLoot.listIterator(), context);
        }
    }
}