package com.alessandro.astages;

import net.minecraft.world.item.ItemStack;

import java.util.Collection;

public abstract class RecipeViewerWrapper {
    public abstract Collection<ItemStack> getAllStacks();

    public abstract void showStacks(Collection<ItemStack> stacks);
    public abstract void hideStacks(Collection<ItemStack> stacks);

    public boolean isRuntimeAvailable() {
        return true;
    }
}
