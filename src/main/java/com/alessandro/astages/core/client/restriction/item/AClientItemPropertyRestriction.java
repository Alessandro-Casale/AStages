package com.alessandro.astages.core.client.restriction.item;

import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;

public record AClientItemPropertyRestriction(String id, String stage, ItemStack stack,
//                                             boolean renderItemName, boolean hideTooltip,
                                             Component hiddenName, Component jadeItemMessage,
                                             Component jadeBlockMessage) {
}
