package com.alessandro.astages.mixin.item;

import com.alessandro.astages.api.holder.AClientHolder;
import com.alessandro.astages.core.AClientRestrictionManager;
import com.alessandro.astages.store.Attributes;
import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.Gui;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.Style;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import java.util.function.UnaryOperator;

@Mixin(Gui.class)
public class AGui {
    @Shadow private ItemStack lastToolHighlight;

    @Redirect(method = "renderSelectedItemName(Lnet/minecraft/client/gui/GuiGraphics;I)V", at = @At(value = "INVOKE", target = "Lnet/minecraft/network/chat/MutableComponent;withStyle(Ljava/util/function/UnaryOperator;)Lnet/minecraft/network/chat/MutableComponent;"))
    public MutableComponent renderSelectedItemName(MutableComponent instance, UnaryOperator<Style> styleFunc) {
        var restriction = AClientRestrictionManager.ITEM_INSTANCE.getRestriction(AClientHolder.serverAndPlayer(), this.lastToolHighlight);
        var properties = AClientRestrictionManager.ITEM_INSTANCE.getProperties(AClientHolder.serverAndPlayer(), this.lastToolHighlight);

        if (restriction != null && properties != null && restriction.isDisabled(Attributes.RENDERING_NAME)) {
            return Component.empty().append(properties.hiddenName()).withStyle(ChatFormatting.RED);
        }

        return instance;
    }
}
