package com.alessandro.astages.api.feature;

import com.alessandro.astages.store.server.ARestriction;
import com.alessandro.astages.api.nullability.Nullable;
import com.alessandro.astages.api.develop.Info;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.entity.player.Player;

@Info("Implement this interface to add support for server stages!")
public interface ServerStageReadable<R extends ARestriction<R, ?, V>, V> {
    R getRestriction(MinecraftServer server, V object);

    R getRestriction(V object, @Nullable Player player, @Nullable MinecraftServer server);
}
