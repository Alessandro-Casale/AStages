package com.alessandro.astages.api.event.world;

import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.neoforged.neoforge.event.entity.player.PlayerEvent;

public class ContainerChangedEvent extends PlayerEvent {
    private final AbstractContainerMenu container;
    private final int slot;

    public ContainerChangedEvent(Player player, AbstractContainerMenu container, int slot) {
        super(player);
        this.container = container;
        this.slot = slot;
    }

    public int getSlot() {
        return slot;
    }

    public AbstractContainerMenu getContainer() {
        return container;
    }
}