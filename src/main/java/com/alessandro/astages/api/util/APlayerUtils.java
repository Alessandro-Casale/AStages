package com.alessandro.astages.api.util;

import com.alessandro.astages.api.nullability.NotNullParamsAndMethodsReturn;
import com.alessandro.astages.api.nullability.Nullable;
import com.mojang.brigadier.context.CommandContext;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.core.BlockPos;
import net.minecraft.network.protocol.Packet;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import net.neoforged.neoforge.common.util.FakePlayer;
import net.neoforged.neoforge.server.ServerLifecycleHooks;

import java.util.UUID;
import java.util.function.Consumer;

@NotNullParamsAndMethodsReturn
public class APlayerUtils {
    public static void sendVanillaPacket(ServerPlayer player, Packet<?> packet) {
        player.connection.send(packet);
    }

    public static void runOnceASecond(Player player, Consumer<Player> consumer) {
        if (player.tickCount % 20 == 0) {
            consumer.accept(player);
        }
    }

    public static @Nullable ServerPlayer getPlayerFromCommand(CommandContext<CommandSourceStack> context, UUID uuid) {
        return context.getSource().getServer().getPlayerList().getPlayer(uuid);
    }

    public static @Nullable ServerPlayer getPlayerFromUUID(UUID uuid) {
        return getPlayerFromUUID(ServerLifecycleHooks.getCurrentServer(), uuid);
    }

    public static @Nullable ServerPlayer getPlayerFromUUID(MinecraftServer server, UUID uuid) {
        return server.getPlayerList().getPlayer(uuid);
    }

    public static @Nullable Player getNearestPlayer(Level level, BlockPos pos) {
        return getNearestPlayer(level, new Vec3(pos.getX(), pos.getY(), pos.getZ()));
    }

    public static @Nullable Player getNearestPlayer(Level level, Vec3 pos) {
        var players = level.players();
        var minDistance = Double.MAX_VALUE;
        Player toReturn = null;

        for (Player player : players) {
            var distance = player.distanceToSqr(pos.x + 0.5, pos.y + 0.5, pos.z + 0.5);
            if (distance < minDistance) {
                minDistance = distance;
                toReturn = player;
            }
        }

        return toReturn;
    }

    public static boolean isRealPlayer(Player player) {
        return player instanceof ServerPlayer && !(player instanceof FakePlayer);
    }
}
