package com.alessandro.astages.infrastructure.mixin.item;

import com.alessandro.astages.api.holder.AClientHolder;
import com.alessandro.astages.engine.AClientRestrictionManager;
import com.alessandro.astages.engine.store.Attributes;
import net.minecraft.client.gui.Gui;
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

        if (restriction != null && properties != null && restriction.isDisabled(Attributes.SHOW_ACTION_BAR_NAME)) {
            return properties.getMessage(Attributes.Item.ACTION_BAR_MESSAGE, this.lastToolHighlight).copy();
        }

        return instance;
    }
}
