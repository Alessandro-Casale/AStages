package com.alessandro.astages.command.argument;

import com.alessandro.astages.AStages;
import com.alessandro.astages.api.develop.MustBeRefactored;
import com.alessandro.astages.api.develop.UnderDevelopment;
import com.alessandro.astages.api.nullability.NotNullParamsAndMethodsReturn;
import com.alessandro.astages.registry.AStagesRegistries;
import com.alessandro.astages.store.ARestrictionType;
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
import net.minecraft.resources.ResourceLocation;

import java.util.Arrays;
import java.util.Collection;
import java.util.concurrent.CompletableFuture;

@MustBeRefactored
@NotNullParamsAndMethodsReturn
public class AStagesRestrictionTypeArgument implements ArgumentType<ARestrictionType> {
    private static final Collection<String> EXAMPLES = Arrays.asList("item", "recipe");
    private static final DynamicCommandExceptionType ERROR_INVALID_TYPE = new DynamicCommandExceptionType(s -> Component.literal("Unknown type: " + s));

    public static AStagesRestrictionTypeArgument types() {
        return new AStagesRestrictionTypeArgument();
    }

    public static ARestrictionType getType(CommandContext<CommandSourceStack> context, String name) {
        return context.getArgument(name, ARestrictionType.class);
    }

    @UnderDevelopment("Check for nullability!")
    @Override
    public ARestrictionType parse(StringReader stringReader) throws CommandSyntaxException {
        var typeString = ResourceLocation.read(stringReader); // stringReader.readUnquotedString();

        try {
            return AStages.RESTRICTION_TYPES_REGISTRY.get(typeString);
        } catch (IllegalArgumentException exception) {
            throw ERROR_INVALID_TYPE.create(typeString);
        }
    }

    @Override
    public <S> CompletableFuture<Suggestions> listSuggestions(CommandContext<S> context, SuggestionsBuilder builder) {
        return SharedSuggestionProvider.suggest(AStagesRegistries.getAllRestrictionTypeEntries().stream().map(ARestrictionType::getType), builder);
    }

    @Override
    public Collection<String> getExamples() {
        return EXAMPLES;
    }
}
