package com.alessandro.astages.api;

import com.alessandro.astages.api.nullability.NotNullParamsAndMethodsReturn;
import net.minecraft.network.protocol.game.ClientboundContainerSetSlotPacket;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;

@NotNullParamsAndMethodsReturn
public class AInventoryUtils {
    public static void updateSelectedSlot(Player player) {
        updateSelectedSlot((ServerPlayer) player);
    }

    public static void updateSelectedSlot(ServerPlayer player) {
        // Synchronize changes with client!
        var slot = player.getInventory().selected;
        player.connection.send(new ClientboundContainerSetSlotPacket(-2, 0, slot, player.getInventory().getItem(slot)));
    }
}
