package com.alessandro.astages.infrastructure.mixin.ore;

import com.alessandro.astages.api.holder.AClientHolder;
import com.alessandro.astages.api.nullability.NotNullParams;
import com.alessandro.astages.engine.AClientRestrictionManager;
import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import net.minecraft.client.color.item.ItemColors;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

@NotNullParams
@Mixin(ItemColors.class)
public class AItemColors {
    @ModifyExpressionValue(method = "getColor", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/item/ItemStack;getItem()Lnet/minecraft/world/item/Item;"))
    public Item astages$getItem(Item item) {
        if (item instanceof BlockItem blockItem) {
            return AClientRestrictionManager.ORE_INSTANCE.getReplacement(AClientHolder.serverAndPlayer(), blockItem.getBlock().defaultBlockState()).getBlock().asItem();
        }

        return item;
    }

    @ModifyArg(method = "getColor", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/color/item/ItemColor;getColor(Lnet/minecraft/world/item/ItemStack;I)I"))
    public ItemStack astages$getColor(ItemStack stack) {
        if (stack.getItem() instanceof BlockItem blockItem) {
            return AClientRestrictionManager.ORE_INSTANCE.getReplacement(AClientHolder.serverAndPlayer(), stack, blockItem);
        }

        return stack;
    }
}