package com.alessandro.astages.event;

import com.alessandro.astages.event.custom.PlayerInventoryChangedEvent;
import com.alessandro.astages.api.nullability.NotNullParams;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerListener;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.common.NeoForge;

@NotNullParams
public record AInventorySlotListener(Player player) implements ContainerListener {
    @Override
    public void slotChanged(AbstractContainerMenu container, int index, ItemStack stack) {
        if (!stack.isEmpty() && container.getSlot(index).container == player.getInventory()) {
            NeoForge.EVENT_BUS.post(new PlayerInventoryChangedEvent(player, stack, index));
        }
    }

    @Override
    public void dataChanged(AbstractContainerMenu container, int index, int item) { }
}
