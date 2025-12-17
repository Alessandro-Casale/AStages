package com.alessandro.astages.integration.jade;

import com.alessandro.astages.api.ABlockStateUtils;
import com.alessandro.astages.api.holder.AClientHolder;
import com.alessandro.astages.api.nullability.NotNullParams;
import com.alessandro.astages.core.AClientRestrictionManager;
import com.alessandro.astages.integration.Mods;
import com.alessandro.astages.store.Attributes;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import snownee.jade.api.*;

@NotNullParams
@WailaPlugin
public class AStagesJadePlugin implements IWailaPlugin {
    @Override
    public void register(IWailaCommonRegistration registration) {
        registration.registerBlockDataProvider(AStagesBlockComponentProvider.INSTANCE, BlockEntity.class);
    }

    @Override
    public void registerClient(IWailaClientRegistration registration) {
        if (!Mods.JADE.isLoaded()) { return; }
        registration.registerBlockComponent(AStagesBlockComponentProvider.INSTANCE, Block.class);

        registration.addRayTraceCallback((hitResult, accessor, originalAccessor) -> {
            if (accessor instanceof BlockAccessor blockAccessor) {
                var original = blockAccessor.getBlockState();
                var restriction = AClientRestrictionManager.ORE_INSTANCE.getRestriction(AClientHolder.serverAndPlayer(), original);

                if (restriction != null) {
                    return registration.blockAccessor().from(blockAccessor).blockState(restriction.getReplacement()).build();
                }
            }

            return accessor;
        });

        registration.addTooltipCollectedCallback((tooltip, accessor) -> {
            if (accessor instanceof EntityAccessor entityAccessor) {
                var entity = entityAccessor.getEntity();
                var type = entity.getType();

                var restriction = AClientRestrictionManager.MOB_INSTANCE.getRestriction(AClientHolder.serverAndPlayer(), type);

                if (restriction != null) {
                    tooltip.getTooltip().clear();

                    if (!restriction.isValueNull(Attributes.Mob.JADE_MOB_MESSAGE)) {
                        tooltip.getTooltip().add(restriction.get(Attributes.Mob.JADE_MOB_MESSAGE).get());
                    }
                }
            }

            if (accessor instanceof BlockAccessor blockAccessor) {
                var original = blockAccessor.getBlock();
                var stack = ABlockStateUtils.blockToStack(original);
                var restriction = AClientRestrictionManager.ITEM_INSTANCE.getRestriction(AClientHolder.serverAndPlayer(), stack);
                var properties = AClientRestrictionManager.ITEM_INSTANCE.getProperties(AClientHolder.serverAndPlayer(), stack);

                if (restriction != null && properties != null && restriction.isEnabled(Attributes.HIDING_TOOLTIP)) {
                    tooltip.getTooltip().clear();

                    if (properties.jadeBlockMessage() != null) {
                        tooltip.getTooltip().add(properties.jadeBlockMessage());
                    }
                }
            }

            if (accessor instanceof EntityAccessor entityAccessor) {
                var original = entityAccessor.getEntity();

                if (original instanceof ItemEntity itemEntity) {
                    var restriction = AClientRestrictionManager.ITEM_INSTANCE.getRestriction(AClientHolder.serverAndPlayer(), itemEntity.getItem());
                    var properties = AClientRestrictionManager.ITEM_INSTANCE.getProperties(AClientHolder.serverAndPlayer(), itemEntity.getItem());

                    if (restriction != null && properties != null && restriction.isEnabled(Attributes.HIDING_TOOLTIP)) {
                        tooltip.getTooltip().clear();

                        if (properties.jadeItemMessage() != null) {
                            tooltip.getTooltip().add(properties.jadeItemMessage());
                        }
                    }
                }
            }
        });
    }
}
