package com.alessandro.astages.api.stage.event;

import com.alessandro.astages.api.nullability.NotNullParams;
import com.alessandro.astages.api.nullability.Nullable;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.entity.player.Player;

@NotNullParams
public class GrantedEvent extends GenericEvent {
    public GrantedEvent(@Nullable Player player, @Nullable MinecraftServer server, boolean isClientSide) {
        super(player, server, isClientSide);
    }
}
