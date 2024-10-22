package com.alessandro.astages.mixin.item;

import com.alessandro.astages.core.AItemRestriction;
import com.alessandro.astages.core.ARestrictionManager;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.client.extensions.common.IClientItemExtensions;
import org.spongepowered.asm.mixin.*;

@Mixin(Gui.class)
public class AGui {
    @Final @Shadow protected Minecraft minecraft;
    @Shadow protected int toolHighlightTimer;
    @Shadow protected ItemStack lastToolHighlight;
    @Shadow protected int screenWidth;
    @Shadow protected int screenHeight;

    @Unique
    private Gui gui$self() {
        return (Gui) (Object) this;
    }

    /**
     * @author Alessandro
     * @reason Change hover name for item stages
     */
    @Overwrite(remap = false)
    public void renderSelectedItemName(GuiGraphics guiGraphics, int yShift) {
        this.minecraft.getProfiler().push("selectedItemName");
        if (this.toolHighlightTimer > 0 && !this.lastToolHighlight.isEmpty()) {

            // New part
            MutableComponent mutablecomponent = Component.empty();
            AItemRestriction restriction = ARestrictionManager.ITEM_INSTANCE.getRestriction(this.lastToolHighlight);

            if (restriction != null && !restriction.renderItemName) {
                mutablecomponent.append("Unfamiliar Item").withStyle(ChatFormatting.RED);
            } else {
                mutablecomponent.append(this.lastToolHighlight.getHoverName()).withStyle(this.lastToolHighlight.getRarity().getStyleModifier());

                if (this.lastToolHighlight.hasCustomHoverName()) {
                    mutablecomponent.withStyle(ChatFormatting.ITALIC);
                }
            }

            // Old part
            Component highlightTip = this.lastToolHighlight.getHighlightTip(mutablecomponent);
            int i = gui$self().getFont().width(highlightTip);
            int j = (this.screenWidth - i) / 2;
            int k = this.screenHeight - Math.max(yShift, 59);
            if (this.minecraft.gameMode != null && !this.minecraft.gameMode.canHurtPlayer()) {
                k += 14;
            }

            int l = (int) ((float) this.toolHighlightTimer * 256.0F / 10.0F);
            if (l > 255) {
                l = 255;
            }

            if (l > 0) {
                guiGraphics.fill(j - 2, k - 2, j + i + 2, k + 9 + 2, this.minecraft.options.getBackgroundColor(0));
                Font font = IClientItemExtensions.of(this.lastToolHighlight).getFont(this.lastToolHighlight, IClientItemExtensions.FontContext.SELECTED_ITEM_NAME);
                if (font == null) {
                    guiGraphics.drawString(gui$self().getFont(), highlightTip, j, k, 16777215 + (l << 24));
                } else {
                    j = (this.screenWidth - font.width(highlightTip)) / 2;
                    guiGraphics.drawString(font, highlightTip, j, k, 16777215 + (l << 24));
                }
            }
        }

        this.minecraft.getProfiler().pop();
    }
}
