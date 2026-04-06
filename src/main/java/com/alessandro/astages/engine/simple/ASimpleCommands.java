package com.alessandro.astages.engine.simple;

import com.alessandro.astages.api.nullability.NotNullParams;
import com.mojang.brigadier.arguments.BoolArgumentType;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.builder.ArgumentBuilder;
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
public class ASimpleCommands {
    public static void item(CommandBuildContext context, ArgumentBuilder<CommandSourceStack, ?> literal) {
        literal.then(Commands.argument("item", ItemArgument.item(context))
            .executes(ASimpleElaborator::commandItem)
        );
    }

    public static void mod(CommandBuildContext ignoredContext, ArgumentBuilder<CommandSourceStack, ?> literal) {
        literal.then(Commands.argument("mod", StringArgumentType.string())
            .executes(ASimpleElaborator::commandMod)
        );
    }

    public static void dimension(CommandBuildContext ignoredContext, ArgumentBuilder<CommandSourceStack, ?> literal) {
        literal.then(Commands.argument("dimension", DimensionArgument.dimension())
            .executes(ASimpleElaborator::commandDimension)
        );
    }

    public static void gui(CommandBuildContext ignoredContext, ArgumentBuilder<CommandSourceStack, ?> literal) {
        literal.then(Commands.argument("gui", StringArgumentType.string())
            .executes(ASimpleElaborator::commandGui)
        );
    }

    public static void ore(CommandBuildContext context, ArgumentBuilder<CommandSourceStack, ?> literal) {
        literal.then(Commands.argument("original", BlockStateArgument.block(context))
            .then(Commands.argument("replacement", BlockStateArgument.block(context))
                .executes(ASimpleElaborator::commandOreWithDefaultValue)
            )
        );

        literal.then(Commands.argument("original", BlockStateArgument.block(context))
            .then(Commands.argument("replacement", BlockStateArgument.block(context))
                .then(Commands.argument("affects_player_actions", BoolArgumentType.bool())
                    .executes(ASimpleElaborator::commandOre)
                )
            )
        );
    }

    public static void structure(CommandBuildContext ignoredContext, ArgumentBuilder<CommandSourceStack, ?> literal) {
        literal.then(Commands.argument("structure", ResourceKeyArgument.key(Registries.STRUCTURE))
            .executes(ASimpleElaborator::commandStructure)
        );
    }

    public static void tame(CommandBuildContext context, ArgumentBuilder<CommandSourceStack, ?> literal) {
        literal.then(Commands.argument("tame", ResourceArgument.resource(context, Registries.ENTITY_TYPE)).suggests(SuggestionProviders.SUMMONABLE_ENTITIES)
            .executes(ASimpleElaborator::commandTame)
        );
    }

    public static void mount(CommandBuildContext context, ArgumentBuilder<CommandSourceStack, ?> literal) {
        literal.then(Commands.argument("mount", ResourceArgument.resource(context, Registries.ENTITY_TYPE)).suggests(SuggestionProviders.SUMMONABLE_ENTITIES)
            .executes(ASimpleElaborator::commandMount)
        );
    }

    public static void recipe(CommandBuildContext ignoredContext, ArgumentBuilder<CommandSourceStack, ?> literal) {
        literal.then(Commands.argument("recipe", ResourceLocationArgument.id())
            .executes(ASimpleElaborator::commandRecipe)
        );
    }

    public static void armor(CommandBuildContext context, ArgumentBuilder<CommandSourceStack, ?> literal) {
        literal.then(Commands.argument("item", ItemArgument.item(context))
            .executes(ASimpleElaborator::commandArmor)
        );
    }
}