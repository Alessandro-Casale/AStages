package com.alessandro.astages.infrastructure.hook.restriction;

import com.alessandro.astages.AStages;
import com.alessandro.astages.api.holder.AClientHolder;
import com.alessandro.astages.api.nullability.NotNullParams;
import com.alessandro.astages.engine.AClientRestrictionManager;
import com.alessandro.astages.engine.store.Attributes;
import net.minecraft.network.chat.Component;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.RenderTooltipEvent;
import net.neoforged.neoforge.event.entity.player.ItemTooltipEvent;

@NotNullParams
@EventBusSubscriber(modid = AStages.MODID, value = Dist.CLIENT)
public class ItemClientEvents {
    @SubscribeEvent
    public static void onItemTooltip(ItemTooltipEvent event) {
        if (event.getEntity() != null && AClientRestrictionManager.didJeiFinishReloading()) {
            var stack = event.getItemStack();
            var restriction = AClientRestrictionManager.ITEM_INSTANCE.getRestriction(AClientHolder.serverAndPlayer(), stack);
            var properties = AClientRestrictionManager.ITEM_INSTANCE.getProperties(AClientHolder.serverAndPlayer(), stack);

            if (restriction != null && restriction.isEnabled(Attributes.HIDING_TOOLTIP)) {
                event.getToolTip().clear();

                if (properties != null) {
                    event.getToolTip().add(properties.hiddenName());
                } else {
                    event.getToolTip().addFirst(Component.literal("%[ASTAGES_FROM_ITEMTOOLTIPEVENT]%"));
                }
            }
        }
    }

    @SubscribeEvent
    public static void onTooltipRender(RenderTooltipEvent.GatherComponents event) {
        for (var either : event.getTooltipElements()) {
            var component = either.left().orElse(null);

            if (component != null) {
                if (component.getString().equals("%[ASTAGES_FROM_ITEMTOOLTIPEVENT]%")) {
                    event.setCanceled(true);
                    break;
                }
            }
        }
    }
}
