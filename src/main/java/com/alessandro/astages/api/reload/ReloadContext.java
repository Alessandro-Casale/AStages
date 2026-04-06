package com.alessandro.astages.api.reload;

import com.alessandro.astages.api.nullability.Nullable;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;

public record ReloadContext(@Nullable MinecraftServer server, @Nullable ServerPlayer player) {
    public ReloadContext(MinecraftServer server) {
        this(server, null);
    }

    public ReloadContext(ServerPlayer player) {
        this(null, player);
    }
}