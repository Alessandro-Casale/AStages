package com.alessandro.astages.command;

import com.alessandro.astages.api.AStagesUtils;
import com.alessandro.astages.api.holder.AHolder;
import com.alessandro.astages.api.nullability.NotNullParams;
import com.alessandro.astages.api.nullability.Nullable;
import com.alessandro.astages.command.argument.AStagesServerRemoveArgument;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.StringArgumentType;
import net.minecraft.ChatFormatting;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;

@NotNullParams
public class AStagesServerCommands {
    public static void register(CommandDispatcher<CommandSourceStack> dispatcher) {
        dispatcher.register(Commands.literal("astages").requires(c -> c.hasPermission(2)).then(Commands.literal("server")
            .then(Commands.literal("add").then(Commands.argument("stage", StringArgumentType.string())
                .executes(context -> addServerStageCommand(StringArgumentType.getString(context, "stage")))
            ))
            .then(Commands.literal("remove").then(Commands.argument("stage", AStagesServerRemoveArgument.stages())
                .executes(context -> removeServerStageCommand(AStagesServerRemoveArgument.getStage(context, "stage")))
            ))
            .then(Commands.literal("remove_all")
                .executes(context -> removeAllServerStageCommand())
            )
            .then(Commands.literal("info")
                .executes(context -> infoCommand(context.getSource().getPlayer()))
            )
        ));
    }

    private static int addServerStageCommand(String stageToAdd) {
        AStagesUtils.addStage(AHolder.server(), stageToAdd, false);
        return 1;
    }

    private static int removeServerStageCommand(String stageToRemove) {
        AStagesUtils.removeStage(AHolder.server(), stageToRemove, false);
        return 1;
    }

    private static int removeAllServerStageCommand() {
        AStagesUtils.removeAllStages(AHolder.server(), false);
        return 1;
    }

    private static int infoCommand(@Nullable ServerPlayer executor) {
        var serverStage = AStagesUtils.getStages(AHolder.server());

        if (executor != null) {
            if (serverStage.isEmpty()) {
                executor.sendSystemMessage(Component.translatable("chat.astages.info.server.no_stages").withStyle(ChatFormatting.RED));
            } else {
                executor.sendSystemMessage(Component.translatable("chat.astages.info.server.has_stages").withStyle(ChatFormatting.GREEN));
                for (var stage : serverStage) {
                    executor.sendSystemMessage(Component.translatable("chat.astages.info.server.list_item", stage));
                }
            }
        }

        return 1;
    }
}
