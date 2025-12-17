package com.alessandro.astages.api;

import net.minecraft.world.item.ItemStack;

public class AItemStackUtils {
    public static boolean itemStacksMatchesIgnoringCount(ItemStack stack, ItemStack other) {
        if (stack == other) {
            return true;
        } else {
            return ItemStack.matches(stack, other);
        }
    }
}
