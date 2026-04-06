package com.alessandro.astages.infrastructure.hook;

import com.alessandro.astages.api.nullability.NotNullParams;
import com.alessandro.astages.api.nullability.Nullable;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@NotNullParams
public class CommonEventSettings {
    private static boolean isInventoryChanged = false;
    private static Integer slotChanged = null;

    private static boolean isContainerChanged = false;

    private static final Map<UUID, AbstractContainerMenu> containerOpenedByPlayers = new HashMap<>();

    public static void allInventoryChanged() {
        isInventoryChanged = true;
        slotChanged = null;
    }

    public static void slotChanged(int slot) {
        isInventoryChanged = true;
        slotChanged = slot;
    }

    public static void resetSlotChanged() {
        isInventoryChanged = false;
    }

    public static Integer getSlotChanged() {
        return slotChanged;
    }

    @SuppressWarnings("BooleanMethodIsAlwaysInverted")
    public static boolean requireSlotCheck() {
        return isInventoryChanged;
    }

    public static void containerChanged() {
        isContainerChanged = true;
    }

    public static boolean requireContainerCheck() {
        return isContainerChanged;
    }

    public static void resetContainerChanged() {
        isContainerChanged = false;
    }

    public static boolean hasPlayerAnotherContainerOpened(Player player) {
        return containerOpenedByPlayers.getOrDefault(player.getUUID(), null) != null;
    }

    public static void setPlayerAnotherContainerOpened(Player player, @Nullable AbstractContainerMenu value) {
        containerOpenedByPlayers.put(player.getUUID(), value);
    }

    public static AbstractContainerMenu getContainerOpenedByPlayer(Player player) {
        return containerOpenedByPlayers.getOrDefault(player.getUUID(), null);
    }
}
