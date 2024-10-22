package com.alessandro.astages.command.argument;

import com.alessandro.astages.core.ARestrictionManager;
import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.exceptions.DynamicCommandExceptionType;
import com.mojang.brigadier.suggestion.Suggestions;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.SharedSuggestionProvider;
import net.minecraft.network.chat.Component;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.Collection;
import java.util.concurrent.CompletableFuture;

public class AStagesAddArgument implements ArgumentType<String> {
    private static final Collection<String> EXAMPLES = Arrays.asList("test_stage_1", "test_stage_2");
    private static final DynamicCommandExceptionType ERROR_INVALID_STAGE = new DynamicCommandExceptionType(s -> Component.literal("Invalid stage argument: " + s));

    @Contract(value = " -> new", pure = true)
    public static @NotNull AStagesAddArgument stages() {
        return new AStagesAddArgument();
    }

    public static String getStage(@NotNull CommandContext<CommandSourceStack> context, String name) {
        return context.getArgument(name, String.class);
    }

    @Override
    public String parse(@NotNull StringReader stringReader) throws CommandSyntaxException {
        var stageString = stringReader.readUnquotedString();

        if (stageString == null) { throw ERROR_INVALID_STAGE.create(null); }

        return stageString;
    }

    @Override
    public <S> CompletableFuture<Suggestions> listSuggestions(CommandContext<S> context, SuggestionsBuilder builder) {
        return SharedSuggestionProvider.suggest(ARestrictionManager.ALL_STAGES.stream().sorted(), builder);
    }

    @Override
    public Collection<String> getExamples() {
        return EXAMPLES;
    }
}
