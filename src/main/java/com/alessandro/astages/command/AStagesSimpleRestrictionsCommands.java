package com.alessandro.astages.command;

import com.alessandro.astages.api.nullability.NotNullParams;
import com.alessandro.astages.command.argument.AStagesSimpleRestrictionTypeArgument;
import com.alessandro.astages.command.argument.AStagesSimpleRestrictionsIdsArgument;
import com.alessandro.astages.simple.ASimpleElaborator;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.BoolArgumentType;
import com.mojang.brigadier.arguments.StringArgumentType;
import net.minecraft.commands.CommandBuildContext;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.arguments.DimensionArgument;
import net.minecraft.commands.arguments.ResourceArgument;
import net.minecraft.commands.arguments.ResourceKeyArgument;
import net.minecraft.commands.arguments.ResourceLocationArgument;
import net.minecraft.commands.arguments.blocks.BlockStateArgument;
import net.minecraft.commands.arguments.item.ItemArgument;
import net.minecraft.commands.synchronization.SuggestionProviders;
import net.minecraft.core.registries.Registries;

@NotNullParams
public class AStagesSimpleRestrictionsCommands {
    public static void register(CommandDispatcher<CommandSourceStack> dispatcher, CommandBuildContext context) {
        dispatcher.register(Commands.literal("astages").requires(c -> c.hasPermission(2))
            .then(Commands.literal("restrict").then(Commands.argument("id", StringArgumentType.string()).then(Commands.argument("stage", StringArgumentType.string())
                .then(Commands.literal("item").then(Commands.argument("item", ItemArgument.item(context)).executes(ASimpleElaborator::commandItem)))
                .then(Commands.literal("mod").then(Commands.argument("mod", StringArgumentType.string()).executes(ASimpleElaborator::commandMod)))
                .then(Commands.literal("dimension").then(Commands.argument("dimension", DimensionArgument.dimension()).executes(ASimpleElaborator::commandDimension)))
                .then(Commands.literal("gui").then(Commands.argument("gui", StringArgumentType.string()).executes(ASimpleElaborator::commandGui)))
                .then(Commands.literal("ore").then(Commands.argument("original", BlockStateArgument.block(context)).then(Commands.argument("replacement", BlockStateArgument.block(context)).executes(ASimpleElaborator::commandOreWithDefaultValue))))
                .then(Commands.literal("ore").then(Commands.argument("original", BlockStateArgument.block(context)).then(Commands.argument("replacement", BlockStateArgument.block(context)).then(Commands.argument("affects_player_actions", BoolArgumentType.bool()).executes(ASimpleElaborator::commandOre)))))
                .then(Commands.literal("structure").then(Commands.argument("structure", ResourceKeyArgument.key(Registries.STRUCTURE)).executes(ASimpleElaborator::commandStructure)))
                .then(Commands.literal("biome").then(Commands.argument("biome", StringArgumentType.string()).executes(ASimpleElaborator::commandBiome)))
                .then(Commands.literal("tame").then(Commands.argument("tame", ResourceArgument.resource(context, Registries.ENTITY_TYPE)).suggests(SuggestionProviders.SUMMONABLE_ENTITIES).executes(ASimpleElaborator::commandTame)))
                .then(Commands.literal("mount").then(Commands.argument("mount", ResourceArgument.resource(context, Registries.ENTITY_TYPE)).suggests(SuggestionProviders.SUMMONABLE_ENTITIES).executes(ASimpleElaborator::commandMount)))
                .then(Commands.literal("recipe").then(Commands.argument("recipe", ResourceLocationArgument.id()).executes(ASimpleElaborator::commandRecipe)))
                .then(Commands.literal("armor").then(Commands.argument("item", ItemArgument.item(context)).executes(ASimpleElaborator::commandArmor)))
            )))
            .then(Commands.literal("remove_restrict").then(Commands.argument("id", AStagesSimpleRestrictionsIdsArgument.simpleRestrictionIds()).then(Commands.argument("type", AStagesSimpleRestrictionTypeArgument.types()).executes(ASimpleElaborator::removeRestriction))))
        );
    }
}
