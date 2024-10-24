package com.alessandro.astages.util;

import com.alessandro.astages.AStages;
import com.alessandro.astages.capability.PlayerStageProvider;
import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.client.model.IDynamicBakedModel;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.Function;

public class AStagesUtil {
    // Util class

//    static {
//        AStagesUtil.runForSide(
//            true,
//            () -> AStages.LOGGER.debug("Client!"),
//            () -> AStages.LOGGER.debug("Server!")
//        );
//    }

    public static void runForSide(boolean discriminantForClient, Runnable client, Runnable server) {
        if (discriminantForClient) {
            client.run();
        } else {
            server.run();
        }
    }

    public static boolean isRealPlayer(Player player) {
        return player instanceof ServerPlayer;
    }

    public static boolean hasStage(@NotNull Player player, String stage) {
        AtomicBoolean toReturn = new AtomicBoolean(false);

        player.getCapability(PlayerStageProvider.PLAYER_STAGE).ifPresent(playerStage -> toReturn.set(playerStage.getStages().contains(stage)));

        return toReturn.get();
    }

    public static Player getPlayerFromUUID(@NotNull MinecraftServer server, UUID uuid) {
        return server.getPlayerList().getPlayer(uuid);
    }

    public static @NotNull BakedModel getBakedModelFromState(BlockState state) {
        return Minecraft.getInstance().getBlockRenderer().getBlockModel(state);
    }

    public static void setBakedModelForState(BlockState state, BakedModel bakedModel) {
        Minecraft.getInstance().getBlockRenderer().getBlockModelShaper().modelByStateCache.put(state, bakedModel);
    }

    @Contract("_ -> new")
    public static @NotNull ItemStack stateToStack(@NotNull BlockState state) {
        return new ItemStack(state.getBlock());
    }

    @Contract("_ -> new")
    public static @NotNull ItemStack blockToStack(Block block) {
        return new ItemStack(block);
    }
}
