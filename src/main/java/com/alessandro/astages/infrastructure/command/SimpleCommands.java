package com.alessandro.astages.infrastructure.command;

import com.alessandro.astages.api.nullability.NotNullParams;
import com.alessandro.astages.engine.ASimpleRestrictionManager;
import com.alessandro.astages.engine.simple.ASimpleElaborator;
import com.alessandro.astages.infrastructure.command.argument.AStagesSimpleRestrictionTypeArgument;
import com.alessandro.astages.infrastructure.command.argument.AStagesSimpleRestrictionsIdsArgument;
import com.alessandro.astages.infrastructure.registry.AStagesRegistries;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.StringArgumentType;
import net.minecraft.commands.CommandBuildContext;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;

@NotNullParams
public class SimpleCommands {
    public static void register(CommandDispatcher<CommandSourceStack> dispatcher, CommandBuildContext context) {
        var root = Commands.literal("astages_simple").requires(c -> c.hasPermission(2));
        var restrictLiteral = Commands.literal("restrict");
        var idArgument = Commands.argument("id", StringArgumentType.string());
        var stageArgument = Commands.argument("stage", StringArgumentType.string());

        ASimpleRestrictionManager.COMMAND_MAP
            .forEach((type, consumer) -> {
                var registry = AStagesRegistries.SIMPLE_RESTRICTION_TYPES.getKey(type);
                if (registry != null) {
                    var literal = Commands.literal(registry.toString());
                    consumer.accept(context, literal);

                    stageArgument.then(literal);
                }
            });

        root.then(restrictLiteral.then(idArgument.then(stageArgument)));
        root
            .then(Commands.literal("remove")
                .then(Commands.argument("id", AStagesSimpleRestrictionsIdsArgument.simpleRestrictionIds())
                    .executes(ASimpleElaborator::removeRestrictionNoTypeDefined)
                    .then(Commands.argument("type", AStagesSimpleRestrictionTypeArgument.types())
                        .executes(ASimpleElaborator::removeRestriction)
                    )
                )
            );

        dispatcher.register(root);
    }
}