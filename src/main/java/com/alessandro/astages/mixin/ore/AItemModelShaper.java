package com.alessandro.astages.mixin.ore;

import com.alessandro.astages.api.holder.AClientHolder;
import com.alessandro.astages.core.AClientRestrictionManager;
import net.minecraft.client.renderer.ItemModelShaper;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

@Mixin(ItemModelShaper.class)
public class AItemModelShaper {
    @ModifyArg(method = "getItemModel(Lnet/minecraft/world/item/ItemStack;)Lnet/minecraft/client/resources/model/BakedModel;", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/renderer/ItemModelShaper;getItemModel(Lnet/minecraft/world/item/Item;)Lnet/minecraft/client/resources/model/BakedModel;"))
    public Item astages$getItemModel(Item item) {
        if (item instanceof BlockItem blockItem) {
            return AClientRestrictionManager.ORE_INSTANCE.getReplacement(AClientHolder.serverAndPlayer(), blockItem);
        }

        return item;
    }
}