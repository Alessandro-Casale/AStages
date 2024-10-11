package com.alessandro.astages.event.custom;

import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.event.entity.player.PlayerEvent;

public class PlayerInventoryChangedEvent extends PlayerEvent {
    private final int slotIndex;
    private final ItemStack stack;
    private final AbstractContainerMenu menu;

    public PlayerInventoryChangedEvent(Player player, int slotIndex, ItemStack stack, AbstractContainerMenu menu) {
        super(player);
        this.slotIndex = slotIndex;
        this.stack = stack;
        this.menu = menu;
    }

    public int getSlotIndex() {
        return slotIndex;
    }

    public ItemStack getStack() {
        return stack;
    }

    public AbstractContainerMenu getMenu() {
        return menu;
    }
}
