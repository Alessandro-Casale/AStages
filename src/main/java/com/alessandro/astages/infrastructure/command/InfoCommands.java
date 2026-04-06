package com.alessandro.astages.infrastructure.command;

import com.alessandro.astages.api.chat.AChatBundle;
import com.alessandro.astages.api.chat.AChatUtils;
import com.alessandro.astages.api.nullability.NotNullParams;
import com.alessandro.astages.api.restriction.ARestriction;
import com.alessandro.astages.engine.ARestrictionManager;
import com.alessandro.astages.engine.server.MiscStorage;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.BoolArgumentType;
import com.mojang.brigadier.context.CommandContext;
import net.minecraft.ChatFormatting;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.item.Items;

@NotNullParams
public class InfoCommands {
    public static void register(CommandDispatcher<CommandSourceStack> dispatcher) {
        dispatcher.register(Commands.literal("astages_utils").requires(c -> c.hasPermission(2))
            .then(Commands.literal("hand").executes(InfoCommands::getItemInHandInfo))
            .then(Commands.literal("stages").executes(c -> InfoCommands.allServerStages(c, false)))
            .then(Commands.literal("stages").then(Commands.argument("printInLogs", BoolArgumentType.bool()).executes(c -> InfoCommands.allServerStages(c, BoolArgumentType.getBool(c, "printInLogs")))))
            .then(Commands.literal("ids").executes(c -> InfoCommands.allServerIds(c, false)))
            .then(Commands.literal("ids").then(Commands.argument("printInLogs", BoolArgumentType.bool()).executes(c -> InfoCommands.allServerIds(c, BoolArgumentType.getBool(c, "printInLogs")))))
        );
    }

    @SuppressWarnings("NullPointerException")
    private static int getItemInHandInfo(CommandContext<CommandSourceStack> context) {
        var player = context.getSource().getPlayer();
        if (player == null) { return 0; }
        var stack = player.getItemInHand(InteractionHand.MAIN_HAND);
        if (stack.is(Items.AIR)) { return 1; }
        var item = stack.getItem();

        player.sendSystemMessage(Component.literal("Item Info:"));
        player.sendSystemMessage(AChatUtils.dashItem(AChatUtils.copy(BuiltInRegistries.ITEM.getKey(item).toString(), "Item ID", ChatFormatting.GREEN)));

        player.sendSystemMessage(AChatUtils.emptyRow());
        AChatUtils.sendRestrictionsInfo(player, "Item restrictions:", "Item restriction", () -> ARestrictionManager.ITEM_INSTANCE.getAllRestrictions(stack), ARestriction::getId);

        return 1;
    }

    private static int allServerStages(CommandContext<CommandSourceStack> context, boolean printInLogs) {
        var player = context.getSource().getPlayer();
        if (player == null) { return 0; }

        var bundle = new AChatBundle("Stages Printing", player);

        bundle.literal("Stages Info printed in latest.log file!").markAsAlwaysSendInChat().markAsPrintOnlyWhenPrintInLogsIsCalled();
        bundle.literal("Stages Info:").markAsPrintOnlyWhenPrintInChatIsCalled();

        if (printInLogs) {
            for (var stage : MiscStorage.ALL_STAGES) {
                bundle.literal(stage);
            }
        } else {
            for (var stage : MiscStorage.ALL_STAGES) {
                bundle.dashItem(stage, ChatFormatting.GOLD);
            }
        }

        bundle.buildAndDiscriminate(printInLogs);

        return 1;
    }

    private static int allServerIds(CommandContext<CommandSourceStack> context, boolean printInLogs) {
        var player = context.getSource().getPlayer();
        if (player == null) { return 0; }

        var bundle = new AChatBundle("Ids Printing", player);

        bundle.literal("Ids Info printed in latest.log file!").markAsAlwaysSendInChat().markAsPrintOnlyWhenPrintInLogsIsCalled();
        bundle.literal("Ids Info:").markAsPrintOnlyWhenPrintInChatIsCalled();

        if (printInLogs) {
            for (var id : MiscStorage.ALL_IDS) {
                bundle.literal(id);
            }
        } else {
            for (var id : MiscStorage.ALL_IDS) {
                bundle.dashItem(id, ChatFormatting.GOLD);
            }
        }

        bundle.buildAndDiscriminate(printInLogs);

        return 1;
    }
}
