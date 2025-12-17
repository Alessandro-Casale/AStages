package com.alessandro.astages.command.argument;

import com.alessandro.astages.api.ACommandUtils;
import com.alessandro.astages.api.nullability.NotNullParamsAndMethodsReturn;
import com.alessandro.astages.core.AClientRestrictionManager;
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
public class AStagesDimensionArgument implements ArgumentType<String> {
    private static final Collection<String> EXAMPLES = Arrays.asList("test_dimension_id_1", "test_dimension_id_2");

    @Contract(value = " -> new", pure = true)
    public static AStagesDimensionArgument dimensionIds() {
        return new AStagesDimensionArgument();
    }

    public static String getDimensionId(CommandContext<CommandSourceStack> context, String name) {
        return context.getArgument(name, String.class);
    }

    @Override
    public String parse(StringReader stringReader) throws CommandSyntaxException {
        return ACommandUtils.parseGenericString(stringReader);
    }

    @Override
    public <S> CompletableFuture<Suggestions> listSuggestions(CommandContext<S> context, SuggestionsBuilder builder) {
        return SharedSuggestionProvider.suggest(AClientRestrictionManager.DIMENSION_IDS, builder);
    }

    @Override
    public Collection<String> getExamples() {
        return EXAMPLES;
    }
}
