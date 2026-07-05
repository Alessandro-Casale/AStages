package com.alessandro.astages.infrastructure.command;

import com.alessandro.astages.api.command.AStagesSuggestions;
import com.alessandro.astages.api.constant.AStageSource;
import com.alessandro.astages.api.holder.AHolder;
import com.alessandro.astages.api.nullability.NotNullParams;
import com.alessandro.astages.api.nullability.Nullable;
import com.alessandro.astages.api.util.AStagesUtils;
import com.alessandro.astages.infrastructure.networking.Networking;
import com.alessandro.astages.infrastructure.networking.packet.stages.RequestClientStagesS2C;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.BoolArgumentType;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.context.CommandContext;
import net.minecraft.ChatFormatting;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.arguments.EntityArgument;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.neoforged.neoforge.server.ServerLifecycleHooks;

import java.util.Collection;

@NotNullParams
public class StageCommands {
    public static void register(CommandDispatcher<CommandSourceStack> dispatcher) {
        dispatcher.register(Commands.literal("astages").requires(c -> c.hasPermission(2))
            .then(Commands.literal("add").then(Commands.argument("player", EntityArgument.players()).then(Commands.argument("stage", StringArgumentType.string()).suggests(AStagesSuggestions.PLAYER_ADD)
                .executes(context -> addStage(context, EntityArgument.getPlayers(context, "player"), StringArgumentType.getString(context, "stage"), true, true, true))
            )))
            .then(Commands.literal("add").then(Commands.argument("player", EntityArgument.players()).then(Commands.argument("stage", StringArgumentType.string()).suggests(AStagesSuggestions.PLAYER_ADD).then(Commands.argument("showTitle", BoolArgumentType.bool()).then(Commands.argument("displayChatMessage", BoolArgumentType.bool()).then(Commands.argument("displayActionBarMessage", BoolArgumentType.bool())
                .executes(context -> addStage(context, EntityArgument.getPlayers(context, "player"), StringArgumentType.getString(context, "stage"), BoolArgumentType.getBool(context, "showTitle"), BoolArgumentType.getBool(context, "displayChatMessage"), BoolArgumentType.getBool(context, "displayActionBarMessage")))
            ))))))
            .then(Commands.literal("remove").then(Commands.argument("player", EntityArgument.players()).then(Commands.argument("stage", StringArgumentType.string()).suggests(AStagesSuggestions.PLAYER_REMOVE)
                .executes(context -> removeStage(context, EntityArgument.getPlayers(context, "player"), StringArgumentType.getString(context, "stage"), true, true, true))
            )))
            .then(Commands.literal("remove").then(Commands.argument("player", EntityArgument.players()).then(Commands.argument("stage", StringArgumentType.string()).suggests(AStagesSuggestions.PLAYER_REMOVE).then(Commands.argument("showTitle", BoolArgumentType.bool()).then(Commands.argument("displayChatMessage", BoolArgumentType.bool()).then(Commands.argument("displayActionBarMessage", BoolArgumentType.bool())
                .executes(context -> removeStage(context, EntityArgument.getPlayers(context, "player"), StringArgumentType.getString(context, "stage"), BoolArgumentType.getBool(context, "showTitle"), BoolArgumentType.getBool(context, "displayChatMessage"), BoolArgumentType.getBool(context, "displayActionBarMessage")))
            ))))))
            .then(Commands.literal("remove_all").then(Commands.argument("player", EntityArgument.players())
                .executes(context -> removeAllStages(context, EntityArgument.getPlayers(context, "player"), true, true, true))
            ))
            .then(Commands.literal("remove_all").then(Commands.argument("player", EntityArgument.players()).then(Commands.argument("showTitle", BoolArgumentType.bool()).then(Commands.argument("displayChatMessage", BoolArgumentType.bool()).then(Commands.argument("displayActionBarMessage", BoolArgumentType.bool())
                .executes(context -> removeAllStages(context, EntityArgument.getPlayers(context, "player"), BoolArgumentType.getBool(context, "showTitle"), BoolArgumentType.getBool(context, "displayChatMessage"), BoolArgumentType.getBool(context, "displayActionBarMessage")))
            )))))
            .then(Commands.literal("info")
                .executes(context -> info(context, context.getSource().getPlayer(), context.getSource().getPlayer()))
            )
            .then(Commands.literal("info").then(Commands.argument("player", EntityArgument.players())
                .executes(context -> info(context, context.getSource().getPlayer(), EntityArgument.getPlayer(context, "player")))
            ))
            .then(Commands.literal("client_info")
                .executes(context -> clientInfo(context, context.getSource().getPlayer(), context.getSource().getPlayer()))
            )
            .then(Commands.literal("client_info").then(Commands.argument("player", EntityArgument.players())
                .executes(context -> clientInfo(context, context.getSource().getPlayer(), EntityArgument.getPlayer(context, "player")))
            ))
        );
    }

    private static int addStage(CommandContext<CommandSourceStack> context, Collection<ServerPlayer> players, String stageToAdd, boolean showTitle, boolean displayChatMessage, boolean displayActionBarMessage) {
        for (var player : players) {
            AStagesUtils.addStage(AHolder.player(player), stageToAdd, showTitle, displayChatMessage, displayActionBarMessage);
        }

        return 1;
    }

    private static int removeStage(CommandContext<CommandSourceStack> context, Collection<ServerPlayer> players, String stageToRemove, boolean showTitle, boolean displayChatMessage, boolean displayActionBarMessage) {
        for (var player : players) {
            AStagesUtils.removeStage(AHolder.player(player), stageToRemove, showTitle, displayChatMessage, displayActionBarMessage);
        }

        return 1;
    }

    private static int removeAllStages(CommandContext<CommandSourceStack> context, Collection<ServerPlayer> players, boolean showTitle, boolean displayChatMessage, boolean displayActionBarMessage) {
        for (var player : players) {
            AStagesUtils.removeAllStages(AHolder.player(player), showTitle, displayChatMessage, displayActionBarMessage);
        }

        return 1;
    }

    private static int info(CommandContext<CommandSourceStack> context, @Nullable ServerPlayer executor, @Nullable ServerPlayer player) {
        if (player == null) { return 0; }

        var server = ServerLifecycleHooks.getCurrentServer();
        var username = player.getGameProfile().getName();
        var uuid = player.getUUID(); // OfflinePlayerStage.USERNAME_UUID.get(player.getGameProfile().getName());
        var stages = AStagesUtils.getStages(AHolder.player(uuid));

        if (executor != null) {
            if (stages.isEmpty()) {
                executor.sendSystemMessage(Component.translatable("message.astages.player.info.no_stages", username).withStyle(ChatFormatting.RED));
            } else {
                executor.sendSystemMessage(Component.translatable("message.astages.player.info.has_stages", username).withStyle(ChatFormatting.GREEN));
                for (var stage : stages) {
                    executor.sendSystemMessage(Component.translatable("message.astages.player.list_item", stage));
                }
            }
        } else if (server != null) {
            if (stages.isEmpty()) {
                server.sendSystemMessage(Component.translatable("message.astages.player.info.no_stages", username).withStyle(ChatFormatting.RED));
            } else {
                server.sendSystemMessage(Component.translatable("message.astages.player.info.has_stages", username).withStyle(ChatFormatting.GREEN));
                for (var stage : stages) {
                    server.sendSystemMessage(Component.translatable("message.astages.player.list_item", stage));
                }
            }
        }

        return 1;
    }

    private static int clientInfo(CommandContext<CommandSourceStack> context, @Nullable ServerPlayer executor, @Nullable ServerPlayer player) {
        if (player == null) { return 0; }
        Networking.sendToPlayer(executor, new RequestClientStagesS2C(AStageSource.PLAYER, AStageSource.PLAYER, executor.getUUID(), player.getUUID()));

        return 1;
    }
}