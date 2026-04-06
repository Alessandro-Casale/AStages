package com.alessandro.astages.api.event.world;

import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.event.entity.player.PlayerEvent;

public class PlayerInventoryChangedEvent extends PlayerEvent {
    private final ItemStack item;
    private final int slot;

    public PlayerInventoryChangedEvent(Player player, ItemStack item, int slot) {
        super(player);
        this.item = item;

        // Solving hotbar indexes
        if (slot >= 36 && slot <= 44) {
            this.slot = slot - 36;

        // Solving second-hand index
        } else if (slot == 45) {
            this.slot = 40;

        // Solving armor indexes
        } else if (slot == 5) {
            this.slot = 39;
        } else if (slot == 6) {
            this.slot = 38;
        } else if (slot == 7) {
            this.slot = 37;
        } else if (slot == 8) {
            this.slot = 36;

        } else {
            this.slot = slot;
        }
    }

    public ItemStack getItem() {
        return item;
    }

    public int getSlot() {
        return slot;
    }
}
