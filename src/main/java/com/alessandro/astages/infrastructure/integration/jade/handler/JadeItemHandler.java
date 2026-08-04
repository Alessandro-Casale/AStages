package com.alessandro.astages.infrastructure.integration.jade.handler;

import com.alessandro.astages.api.holder.AClientHolder;
import com.alessandro.astages.api.nullability.NotNullParams;
import com.alessandro.astages.api.util.ABlockStateUtils;
import com.alessandro.astages.engine.AClientRestrictionManager;
import com.alessandro.astages.engine.store.Attributes;
import net.minecraft.world.entity.item.ItemEntity;
import snownee.jade.api.BlockAccessor;
import snownee.jade.api.EntityAccessor;
import snownee.jade.api.IWailaClientRegistration;

@NotNullParams
public class JadeItemHandler {
    public static void registerClient(IWailaClientRegistration registration) {
        registration.addTooltipCollectedCallback((tooltip, accessor) -> {
            if (accessor instanceof BlockAccessor blockAccessor) {
                var original = blockAccessor.getBlock();
                var stack = ABlockStateUtils.blockToStack(original);
                var restriction = AClientRestrictionManager.ITEM_INSTANCE.getRestriction(AClientHolder.serverAndPlayer(), stack);
                var properties = AClientRestrictionManager.ITEM_INSTANCE.getProperties(AClientHolder.serverAndPlayer(), stack);

                if (restriction != null && properties != null && restriction.isDisabled(Attributes.SHOW_JADE_BLOCK_NAME)) {
                    tooltip.getTooltip().clear();
                    tooltip.getTooltip().add(properties.getMessage(Attributes.Item.JADE_BLOCK_MESSAGE, stack));
                }
            }

            if (accessor instanceof EntityAccessor entityAccessor) {
                var original = entityAccessor.getEntity();

                if (original instanceof ItemEntity itemEntity) {
                    var stack = itemEntity.getItem();
                    var restriction = AClientRestrictionManager.ITEM_INSTANCE.getRestriction(AClientHolder.serverAndPlayer(), stack);
                    var properties = AClientRestrictionManager.ITEM_INSTANCE.getProperties(AClientHolder.serverAndPlayer(), stack);

                    if (restriction != null && properties != null && restriction.isDisabled(Attributes.SHOW_JADE_ITEM_NAME)) {
                        tooltip.getTooltip().clear();
                        tooltip.getTooltip().add(properties.getMessage(Attributes.Item.JADE_ITEM_MESSAGE, stack));
                    }
                }
            }
        });
    }
}