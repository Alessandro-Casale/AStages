package com.alessandro.astages.command;

import com.alessandro.astages.command.argument.AStagesDimensionArgument;
import com.alessandro.astages.core.server.restriction.ADimensionRestriction;
import com.alessandro.astages.api.nullability.NotNullParams;
import com.alessandro.astages.api.nullability.Nullable;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import net.minecraft.ChatFormatting;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.arguments.EntityArgument;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;

import java.util.Collection;
import java.util.List;

@NotNullParams
public class AStagesTimerCommands {
    public static void register(CommandDispatcher<CommandSourceStack> dispatcher) {
        dispatcher.register(Commands.literal("astages").requires(c -> c.hasPermission(2))
            .then(Commands.literal("timer")
                .then(Commands.literal("reset_all")
                    .then(Commands.argument("players", EntityArgument.players())
                        .then(Commands.argument("restriction_id", AStagesDimensionArgument.dimensionIds())
                            .executes(context -> removeTimerAndAccessForPlayers(context.getSource().getPlayer(), EntityArgument.getPlayers(context, "players"), AStagesDimensionArgument.getDimensionId(context, "restriction_id"), new Reset[] { Reset.ACCESS, Reset.TIMER }))
                        )
                    )
                )
                .then(Commands.literal("reset_timer")
                    .then(Commands.argument("players", EntityArgument.players())
                        .then(Commands.argument("restriction_id", AStagesDimensionArgument.dimensionIds())
                            .executes(context -> removeTimerAndAccessForPlayers(context.getSource().getPlayer(),EntityArgument.getPlayers(context, "players"), AStagesDimensionArgument.getDimensionId(context, "restriction_id"), new Reset[] { Reset.TIMER }))
                        )
                    )
                )
                .then(Commands.literal("reset_access")
                    .then(Commands.argument("players", EntityArgument.players())
                        .then(Commands.argument("restriction_id", AStagesDimensionArgument.dimensionIds())
                            .executes(context -> removeTimerAndAccessForPlayers(context.getSource().getPlayer(), EntityArgument.getPlayers(context, "players"), AStagesDimensionArgument.getDimensionId(context, "restriction_id"), new Reset[] { Reset.ACCESS }))
                        )
                    )
                )
                .then(Commands.literal("set_timer")
                    .then(Commands.argument("players", EntityArgument.players())
                        .then(Commands.argument("restriction_id", AStagesDimensionArgument.dimensionIds())
                            .then(Commands.argument("value", IntegerArgumentType.integer())
                                .executes(context -> setTimerForPlayers(context.getSource().getPlayer(), EntityArgument.getPlayers(context, "players"), AStagesDimensionArgument.getDimensionId(context, "restriction_id"), IntegerArgumentType.getInteger(context, "value")))
                            )
                        )
                    )
                )
                .then(Commands.literal("set_access")
                    .then(Commands.argument("players", EntityArgument.players())
                        .then(Commands.argument("restriction_id", AStagesDimensionArgument.dimensionIds())
                            .then(Commands.argument("value", IntegerArgumentType.integer())
                                .executes(context -> setAccessForPlayers(context.getSource().getPlayer(),EntityArgument.getPlayers(context, "players"), AStagesDimensionArgument.getDimensionId(context, "restriction_id"), IntegerArgumentType.getInteger(context, "value")))
                            )
                        )
                    )
                )
                .then(Commands.literal("increase_access")
                    .then(Commands.argument("players", EntityArgument.players())
                        .then(Commands.argument("restriction_id", AStagesDimensionArgument.dimensionIds())
                            .then(Commands.argument("value", IntegerArgumentType.integer())
                                .executes(context -> increaseOrDecreaseAccessForPlayers(context.getSource().getPlayer(),EntityArgument.getPlayers(context, "players"), AStagesDimensionArgument.getDimensionId(context, "restriction_id"), IntegerArgumentType.getInteger(context, "value")))
                            )
                        )
                    )
                )
                .then(Commands.literal("decrease_access")
                    .then(Commands.argument("players", EntityArgument.players())
                        .then(Commands.argument("restriction_id", AStagesDimensionArgument.dimensionIds())
                            .then(Commands.argument("value", IntegerArgumentType.integer())
                                .executes(context -> increaseOrDecreaseAccessForPlayers(context.getSource().getPlayer(),EntityArgument.getPlayers(context, "players"), AStagesDimensionArgument.getDimensionId(context, "restriction_id"), -IntegerArgumentType.getInteger(context, "value")))
                            )
                        )
                    )
                )
            )
        );
    }

    private static int removeTimerAndAccessForPlayers(@Nullable ServerPlayer executor, Collection<ServerPlayer> players, String restrictionId, Reset[] reset) {
        var resetAccess = List.of(reset).contains(Reset.ACCESS);
        var resetTimers = List.of(reset).contains(Reset.TIMER);

        for (var player : players) {
            var persistentData = player.getPersistentData();
            if (resetAccess) { persistentData.remove(ADimensionRestriction.getNbtAccessForRestrictionId(restrictionId)); }
            if (resetTimers) { persistentData.remove(ADimensionRestriction.getNbtIdForRestrictionId(restrictionId)); }
        }

        if (executor != null) {
            if (resetTimers && resetAccess) {
                executor.sendSystemMessage(Component.translatable("chat.astages.timer.reset_all", restrictionId).withStyle(ChatFormatting.GREEN));
            } else if (resetTimers) {
                executor.sendSystemMessage(Component.translatable("chat.astages.timer.reset_timer", restrictionId).withStyle(ChatFormatting.GREEN));
            } else {
                executor.sendSystemMessage(Component.translatable("chat.astages.timer.reset_access", restrictionId).withStyle(ChatFormatting.GREEN));
            }
        }

        return 1;
    }

    private static int setTimerForPlayers(@Nullable ServerPlayer executor, Collection<ServerPlayer> players, String restrictionId, int value) {
        for (var player : players) {
            var persistentData = player.getPersistentData();
            persistentData.putInt(ADimensionRestriction.getNbtIdForRestrictionId(restrictionId), value);
        }

        if (executor != null) {
            executor.sendSystemMessage(Component.translatable("chat.astages.timer.set_timer", restrictionId, value).withStyle(ChatFormatting.GREEN));
        }

        return 1;
    }

    private static int setAccessForPlayers(@Nullable ServerPlayer executor, Collection<ServerPlayer> players, String restrictionId, int value) {
        for (var player : players) {
            var persistentData = player.getPersistentData();
            persistentData.putInt(ADimensionRestriction.getNbtAccessForRestrictionId(restrictionId), value);
        }

        if (executor != null) {
            executor.sendSystemMessage(Component.translatable("chat.astages.timer.set_access", restrictionId, value).withStyle(ChatFormatting.GREEN));
        }

        return 1;
    }

    private static int increaseOrDecreaseAccessForPlayers(@Nullable ServerPlayer executor, Collection<ServerPlayer> players, String restrictionId, int value) {
        for (var player : players) {
            var persistentData = player.getPersistentData();
            persistentData.putInt(
                ADimensionRestriction.getNbtAccessForRestrictionId(restrictionId),
                persistentData.getInt(ADimensionRestriction.getNbtAccessForRestrictionId(restrictionId)) + value
            );
        }

        if (executor != null) {
            if (value > 0) {
                executor.sendSystemMessage(Component.translatable("chat.astages.timer.increase_access", restrictionId, value).withStyle(ChatFormatting.GREEN));
            } else if (value < 0) {
                executor.sendSystemMessage(Component.translatable("chat.astages.timer.decrease_access", restrictionId, value).withStyle(ChatFormatting.GREEN));
            } else {
                executor.sendSystemMessage(Component.translatable("chat.astages.timer.invalid_value_access", restrictionId).withStyle(ChatFormatting.RED));
            }
        }

        return 1;
    }

    private enum Reset {
        ACCESS, TIMER
    }
}
