package com.alessandro.astages.command.argument;

import com.alessandro.astages.api.ACommandUtils;
import com.alessandro.astages.api.AStagesClientUtils;
import com.alessandro.astages.api.holder.AClientHolder;
import com.alessandro.astages.api.nullability.NotNullParamsAndMethodsReturn;
import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.suggestion.Suggestions;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.SharedSuggestionProvider;
import org.jetbrains.annotations.Contract;

import java.util.Arrays;
import java.util.Collection;
import java.util.concurrent.CompletableFuture;

@NotNullParamsAndMethodsReturn
public class AStagesRemoveArgument implements ArgumentType<String> {
    private static final Collection<String> EXAMPLES = Arrays.asList("test_stage_1", "test_stage_2");

    @Contract(value = " -> new", pure = true)
    public static AStagesRemoveArgument stages() {
        return new AStagesRemoveArgument();
    }

    public static String getStage(CommandContext<CommandSourceStack> context, String name) {
        return context.getArgument(name, String.class);
    }

    @Override
    public String parse(StringReader stringReader) throws CommandSyntaxException {
        return ACommandUtils.parseGenericString(stringReader);
    }

    @Override
    public <S> CompletableFuture<Suggestions> listSuggestions(CommandContext<S> context, SuggestionsBuilder builder) {
        return SharedSuggestionProvider.suggest(AStagesClientUtils.getStages(AClientHolder.player()).stream().sorted(), builder);
    }

    @Override
    public Collection<String> getExamples() {
        return EXAMPLES;
    }
}
