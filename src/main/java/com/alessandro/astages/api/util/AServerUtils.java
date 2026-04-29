package com.alessandro.astages.api.util;

import com.alessandro.astages.api.nullability.NotNullParams;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;

import java.util.function.Consumer;

@NotNullParams
public class AServerUtils {
    public static void runOnceASecond(MinecraftServer server, Consumer<MinecraftServer> consumer) {
        if (server.getTickCount() % 20 == 0) {
            consumer.accept(server);
        }
    }

    public static void runForSide(boolean discriminantForClient, Runnable client, Runnable server) {
        if (discriminantForClient) {
            client.run();
        } else {
            server.run();
        }
    }

    public static void forEachPlayer(MinecraftServer server, Consumer<ServerPlayer> consumer) {
        server.getPlayerList().getPlayers().forEach(consumer);
    };
}
