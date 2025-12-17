package com.alessandro.astages.command;

import com.alessandro.astages.api.AStagesClientUtils;
import com.alessandro.astages.api.AStagesUtils;
import com.alessandro.astages.api.constant.AStatus;
import com.alessandro.astages.api.develop.NotYetImplemented;
import com.alessandro.astages.api.holder.AClientHolder;
import com.alessandro.astages.api.holder.AHolder;
import com.alessandro.astages.api.nullability.NotNullParams;
import com.alessandro.astages.api.nullability.Nullable;
import com.alessandro.astages.capability.OfflinePlayerStage;
import com.alessandro.astages.command.argument.AStagesAddArgument;
import com.alessandro.astages.command.argument.AStagesRemoveArgument;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.BoolArgumentType;
import com.mojang.brigadier.context.CommandContext;
import net.minecraft.ChatFormatting;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.arguments.EntityArgument;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;

import java.util.Collection;

@NotNullParams
public class AStagesModificationCommands {
    public static void register(CommandDispatcher<CommandSourceStack> dispatcher) {
//        dispatcher.register(Commands.literal("astages").requires(c -> c.hasPermission(2))
//            .then(Commands.literal("add").then(Commands.argument("player", AStagesPlayerArgument.onlineAndOfflinePlayers()).then(Commands.argument("stage", AStagesAddArgument.stages())
//                .executes(context -> addStage(context, AStagesPlayerArgument.getPlayer(context, "player"), AStagesAddArgument.getStage(context, "stage"), true, false))
//            )))
//            .then(Commands.literal("add").then(Commands.argument("player", AStagesPlayerArgument.onlineAndOfflinePlayers()).then(Commands.argument("stage", AStagesAddArgument.stages()).then(Commands.argument("silentChat", BoolArgumentType.bool()).then(Commands.argument("silentTitle", BoolArgumentType.bool())
//                .executes(context -> addStage(context, AStagesPlayerArgument.getPlayer(context, "player"), AStagesAddArgument.getStage(context, "stage"), BoolArgumentType.getBool(context, "silentChat"), BoolArgumentType.getBool(context, "silentTitle")))
//            )))))
//            .then(Commands.literal("remove").then(Commands.argument("player", AStagesPlayerArgument.onlineAndOfflinePlayers()).then(Commands.argument("stage", AStagesRemoveArgument.stages())
//                .executes(context -> removeStage(context, AStagesPlayerArgument.getPlayer(context, "player"), AStagesRemoveArgument.getStage(context, "stage"), false, true))
//            )))
//            .then(Commands.literal("remove").then(Commands.argument("player", AStagesPlayerArgument.onlineAndOfflinePlayers()).then(Commands.argument("stage", AStagesRemoveArgument.stages()).then(Commands.argument("silentChat", BoolArgumentType.bool()).then(Commands.argument("silentTitle", BoolArgumentType.bool())
//                .executes(context -> removeStage(context, AStagesPlayerArgument.getPlayer(context, "player"), AStagesRemoveArgument.getStage(context, "stage"), BoolArgumentType.getBool(context, "silentChat"), BoolArgumentType.getBool(context, "silentTitle")))
//            )))))
//            .then(Commands.literal("remove_all").then(Commands.argument("player", AStagesPlayerArgument.onlineAndOfflinePlayers())
//                .executes(context -> removeAllStages(context, AStagesPlayerArgument.getPlayer(context, "player"), false, true))
//            ))
//            .then(Commands.literal("remove_all").then(Commands.argument("player", AStagesPlayerArgument.onlineAndOfflinePlayers()).then(Commands.argument("silentChat", BoolArgumentType.bool()).then(Commands.argument("silentTitle", BoolArgumentType.bool())
//                .executes(context -> removeAllStages(context, AStagesPlayerArgument.getPlayer(context, "player"), BoolArgumentType.getBool(context, "silentChat"), BoolArgumentType.getBool(context, "silentTitle")))
//            ))))
//            .then(Commands.literal("info")
//                .executes(context -> info(context, context.getSource().getPlayer(), context.getSource().getPlayer()))
//            )
//            .then(Commands.literal("info").then(Commands.argument("player", AStagesPlayerArgument.onlineAndOfflinePlayers())
//                .executes(context -> info(context, AStagesPlayerArgument.getPlayer(context, "player"), context.getSource().getPlayer()))
//            ))
//            .then(Commands.literal("client_info")
//                .executes(context -> clientInfo(context.getSource().getPlayer()))
//            )
//        );

        dispatcher.register(Commands.literal("astages").requires(c -> c.hasPermission(2))
            .then(Commands.literal("add").then(Commands.argument("player", EntityArgument.players()).then(Commands.argument("stage", AStagesAddArgument.stages())
                .executes(context -> addStage(context, EntityArgument.getPlayers(context, "player"), AStagesAddArgument.getStage(context, "stage"), true, false))
            )))
            .then(Commands.literal("add").then(Commands.argument("player", EntityArgument.players()).then(Commands.argument("stage", AStagesAddArgument.stages()).then(Commands.argument("silentChat", BoolArgumentType.bool()).then(Commands.argument("silentTitle", BoolArgumentType.bool())
                .executes(context -> addStage(context, EntityArgument.getPlayers(context, "player"), AStagesAddArgument.getStage(context, "stage"), BoolArgumentType.getBool(context, "silentChat"), BoolArgumentType.getBool(context, "silentTitle")))
            )))))
            .then(Commands.literal("remove").then(Commands.argument("player", EntityArgument.players()).then(Commands.argument("stage", AStagesRemoveArgument.stages())
                .executes(context -> removeStage(context, EntityArgument.getPlayers(context, "player"), AStagesRemoveArgument.getStage(context, "stage"), false, true))
            )))
            .then(Commands.literal("remove").then(Commands.argument("player", EntityArgument.players()).then(Commands.argument("stage", AStagesRemoveArgument.stages()).then(Commands.argument("silentChat", BoolArgumentType.bool()).then(Commands.argument("silentTitle", BoolArgumentType.bool())
                .executes(context -> removeStage(context, EntityArgument.getPlayers(context, "player"), AStagesRemoveArgument.getStage(context, "stage"), BoolArgumentType.getBool(context, "silentChat"), BoolArgumentType.getBool(context, "silentTitle")))
            )))))
            .then(Commands.literal("remove_all").then(Commands.argument("player", EntityArgument.players())
                .executes(context -> removeAllStages(context, EntityArgument.getPlayers(context, "player"), false, true))
            ))
            .then(Commands.literal("remove_all").then(Commands.argument("player", EntityArgument.players()).then(Commands.argument("silentChat", BoolArgumentType.bool()).then(Commands.argument("silentTitle", BoolArgumentType.bool())
                .executes(context -> removeAllStages(context, EntityArgument.getPlayers(context, "player"), BoolArgumentType.getBool(context, "silentChat"), BoolArgumentType.getBool(context, "silentTitle")))
            ))))
            .then(Commands.literal("info")
                .executes(context -> info(context, context.getSource().getPlayer(), context.getSource().getPlayer()))
            )
            .then(Commands.literal("info").then(Commands.argument("player", EntityArgument.players())
                .executes(context -> info(context, EntityArgument.getPlayer(context, "player"), context.getSource().getPlayer()))
            ))
            .then(Commands.literal("client_info")
                .executes(context -> clientInfo(context.getSource().getPlayer()))
            )
        );
    }

    //private static int addStage(CommandContext<CommandSourceStack> context, String username, String stageToAdd, boolean silentChat, boolean silentTitle) {
    private static int addStage(CommandContext<CommandSourceStack> context, Collection<ServerPlayer> players, String stageToAdd, boolean silentChat, boolean silentTitle) {
//        var uuid = OfflinePlayerStage.USERNAME_UUID.get(username);
//        AStagesUtils.addStage(AHolder.player(uuid), stageToAdd, silentTitle);
//
//        var player = APlayerUtils.getPlayerFromCommand(context, uuid);
//        if (player != null && !silentChat) {
//            player.sendSystemMessage(Component.translatable("chat.astages.add", stageToAdd).withStyle(ChatFormatting.GREEN));
//        }

        for (var player : players) {
            AStagesUtils.addStage(AHolder.player(player), stageToAdd, silentTitle);

            if (!silentChat) {
                player.sendSystemMessage(Component.translatable("chat.astages.add", stageToAdd).withStyle(ChatFormatting.GREEN));
            }
        }

        return 1;
    }

    //private static int removeStage(CommandContext<CommandSourceStack> context, String username, String stageToRemove, boolean silentChat, boolean silentTitle) {
    private static int removeStage(CommandContext<CommandSourceStack> context, Collection<ServerPlayer> players, String stageToRemove, boolean silentChat, boolean silentTitle) {
//        var uuid = OfflinePlayerStage.USERNAME_UUID.get(username);
//        var result = AStagesUtils.removeStage(AHolder.player(uuid), stageToRemove, silentTitle);
//
//        var player = APlayerUtils.getPlayerFromCommand(context, uuid);
//        if (player != null && !silentChat) {
//            if (result == AStatus.SUCCESS) {
//                player.sendSystemMessage(Component.translatable("chat.astages.remove", stageToRemove).withStyle(ChatFormatting.GREEN));
//            } else {
//                player.sendSystemMessage(Component.translatable("chat.astages.not_present", stageToRemove).withStyle(ChatFormatting.RED));
//            }
//        }

        for (var player : players) {
            var result = AStagesUtils.removeStage(AHolder.player(player), stageToRemove, silentTitle);

            if (!silentChat) {
                if (result == AStatus.SUCCESS) {
                    player.sendSystemMessage(Component.translatable("chat.astages.remove", stageToRemove).withStyle(ChatFormatting.GREEN));
                } else {
                    player.sendSystemMessage(Component.translatable("chat.astages.not_present", stageToRemove).withStyle(ChatFormatting.RED));
                }
            }
        }

        return 1;
    }

    @NotYetImplemented("Not present message for remove_all action!")
    // private static int removeAllStages(CommandContext<CommandSourceStack> context, String username, boolean silentChat, boolean silentTitle) {
    private static int removeAllStages(CommandContext<CommandSourceStack> context, Collection<ServerPlayer> players, boolean silentChat, boolean silentTitle) {
//        var uuid = OfflinePlayerStage.USERNAME_UUID.get(username);
//        var result = AStagesUtils.removeAllStages(AHolder.player(uuid), silentTitle);
//
//        var player = APlayerUtils.getPlayerFromCommand(context, uuid);
//        if (player != null && !silentChat) {
//            if (result == AStatus.SUCCESS) {
//                player.sendSystemMessage(Component.translatable("chat.astages.remove_all").withStyle(ChatFormatting.GREEN));
//            } else {
//                player.sendSystemMessage(Component.translatable("chat.astages.not_present").withStyle(ChatFormatting.RED));
//            }
//        }

        for (var player : players) {
            var result = AStagesUtils.removeAllStages(AHolder.player(player), silentTitle);

            if (!silentChat) {
                if (result == AStatus.SUCCESS) {
                    player.sendSystemMessage(Component.translatable("chat.astages.remove_all").withStyle(ChatFormatting.GREEN));
                } else {
                    player.sendSystemMessage(Component.translatable("chat.astages.not_present").withStyle(ChatFormatting.RED));
                }
            }
        }

        return 1;
    }

    private static int info(CommandContext<CommandSourceStack> context, @Nullable ServerPlayer player, @Nullable ServerPlayer executor) {
        if (player == null) { return 0; }
        return info(context, player.getGameProfile().getName(), executor);
    }

    @NotYetImplemented("Prefer AChatBundle")
    private static int info(CommandContext<CommandSourceStack> context, String username, @Nullable ServerPlayer executor) {
        var uuid = OfflinePlayerStage.USERNAME_UUID.get(username);
        var stages = AStagesUtils.getStages(AHolder.player(uuid));

        if (stages.isEmpty()) {
            executor.sendSystemMessage(Component.translatable("chat.astages.info.no_stages", username).withStyle(ChatFormatting.RED));
        } else {
            executor.sendSystemMessage(Component.translatable("chat.astages.info.has_stages", username).withStyle(ChatFormatting.GREEN));
            for (var stage : stages) {
                executor.sendSystemMessage(Component.translatable("chat.astages.info.list_item", stage));
            }
        }

        return 1;
    }

    private static int clientInfo(@Nullable ServerPlayer executor) {
        var stages = AStagesClientUtils.getStages(AClientHolder.player());

        if (stages.isEmpty()) {
            executor.sendSystemMessage(Component.translatable("chat.astages.info.no_stages", executor.getName()).withStyle(ChatFormatting.RED));
        } else {
            executor.sendSystemMessage(Component.translatable("chat.astages.info.has_stages", executor.getName()).withStyle(ChatFormatting.GREEN));
            for (var stage : stages) {
                executor.sendSystemMessage(Component.translatable("chat.astages.info.list_item", stage));
            }
        }

        return 1;
    }
}
