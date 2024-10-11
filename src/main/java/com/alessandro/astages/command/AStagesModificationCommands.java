package com.alessandro.astages.command;

import com.alessandro.astages.capability.PlayerStage;
import com.alessandro.astages.capability.PlayerStageProvider;
import com.alessandro.astages.command.argument.AStagesAddArgument;
import com.alessandro.astages.command.argument.AStagesRemoveArgument;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.StringArgumentType;
import net.minecraft.ChatFormatting;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.arguments.EntityArgument;
import net.minecraft.network.chat.Component;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

public class AStagesModificationCommands {
    public static void register(@NotNull CommandDispatcher<CommandSourceStack> dispatcher) {
        dispatcher.register(Commands.literal("astages").requires(c -> c.hasPermission(2))
            .then(Commands.literal("add").then(Commands.argument("player", EntityArgument.player()).then(Commands.argument("stage", AStagesAddArgument.stages())
                .executes(context -> {
                    var stageToAdd = AStagesAddArgument.getStage(context, "stage");
                    var player = EntityArgument.getPlayer(context, "player");

                    player.getCapability(PlayerStageProvider.PLAYER_STAGE).ifPresent(playerStage -> {
                        playerStage.addStage(stageToAdd);
                        player.sendSystemMessage(Component.literal("Stage \"" + stageToAdd + "\" added successfully!").withStyle(ChatFormatting.GREEN));
                        playerStage.setChangedFor(player, PlayerStage.Operation.ADD, stageToAdd);
                    });

                    return 1;
                })
            )))
            .then(Commands.literal("remove").then(Commands.argument("player", EntityArgument.player()).then(Commands.argument("stage", AStagesRemoveArgument.stages())
                .executes(context -> {
                    var stageToRemove = AStagesRemoveArgument.getStage(context, "stage");
                    var player = EntityArgument.getPlayer(context, "player");

                    player.getCapability(PlayerStageProvider.PLAYER_STAGE).ifPresent(playerStage -> {
                        if (playerStage.removeStage(stageToRemove) == PlayerStage.Status.SUCCESS) {
                            player.sendSystemMessage(Component.literal("Stage \"" + stageToRemove + "\" removed successfully!").withStyle(ChatFormatting.GREEN));
                            playerStage.setChangedFor(player, PlayerStage.Operation.REMOVE, stageToRemove);
                        } else {
                            player.sendSystemMessage(Component.literal("Stage \"" + stageToRemove + "\" is not present in your stages!").withStyle(ChatFormatting.RED));
                        }
                    });

                    return 1;
                })
            )))
            .then(Commands.literal("remove_all").then(Commands.argument("player", EntityArgument.player())
                .executes(context -> {
                    var player = EntityArgument.getPlayer(context, "player");

                    player.getCapability(PlayerStageProvider.PLAYER_STAGE).ifPresent(playerStage -> {
//                            var stages = playerStage.getStages();
//                            stages.forEach(playerStage::removeStage);

                        playerStage.removeAllStages();
                        playerStage.setChangedFor(player, PlayerStage.Operation.REMOVE_ALL, null);
                    });

                    return 1;
                })
            ))
        );
    }
}
