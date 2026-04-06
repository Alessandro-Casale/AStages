package com.alessandro.astages.infrastructure.command.argument;

import com.alessandro.astages.AStages;
import com.alessandro.astages.api.nullability.NotNullParamsAndMethodsReturn;
import com.alessandro.astages.engine.client.ClientMiscStorage;
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

import java.util.Arrays;
import java.util.Collection;
import java.util.concurrent.CompletableFuture;

@NotNullParamsAndMethodsReturn
public class AStagesSimpleRestrictionsIdsArgument implements ArgumentType<String> {
    private static final Collection<String> EXAMPLES = Arrays.asList("test_simple_id_1", "test_simple_id_2");
    private static final DynamicCommandExceptionType ERROR_INVALID_ID = new DynamicCommandExceptionType(s -> Component.literal("Invalid id argument: " + s));

    public static AStagesSimpleRestrictionsIdsArgument simpleRestrictionIds() {
        return new AStagesSimpleRestrictionsIdsArgument();
    }

    public static String getSimpleRestrictionId(CommandContext<CommandSourceStack> context, String name) {
        return context.getArgument(name, String.class);
    }

    @Override
    public String parse(StringReader stringReader) throws CommandSyntaxException {
        var simpleRestrictionId = stringReader.readUnquotedString();

        if (simpleRestrictionId == null) { throw ERROR_INVALID_ID.create(null); }

        return simpleRestrictionId;
    }

    @Override
    public <S> CompletableFuture<Suggestions> listSuggestions(CommandContext<S> context, SuggestionsBuilder builder) {
        AStages.LOGGER.debug("Suggestion list: {}", ClientMiscStorage.SIMPLE_IDS);
        return SharedSuggestionProvider.suggest(ClientMiscStorage.SIMPLE_IDS, builder);
    }

    @Override
    public Collection<String> getExamples() {
        return EXAMPLES;
    }
}
