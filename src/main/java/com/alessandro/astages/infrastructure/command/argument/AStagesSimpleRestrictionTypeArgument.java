package com.alessandro.astages.infrastructure.command.argument;

import com.alessandro.astages.api.AResourceLocation;
import com.alessandro.astages.api.develop.ToDo;
import com.alessandro.astages.api.nullability.NotNullParamsAndMethodsReturn;
import com.alessandro.astages.api.store.ASimpleRestrictionType;
import com.alessandro.astages.infrastructure.registry.AStagesRegistries;
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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.concurrent.CompletableFuture;

@NotNullParamsAndMethodsReturn
public class AStagesSimpleRestrictionTypeArgument implements ArgumentType<ASimpleRestrictionType> {
    private static final Collection<String> EXAMPLES = Arrays.asList("item", "recipe");
    private static final DynamicCommandExceptionType ERROR_INVALID_TYPE = new DynamicCommandExceptionType(s -> Component.literal("Unknown type: " + s));

    public static AStagesSimpleRestrictionTypeArgument types() {
        return new AStagesSimpleRestrictionTypeArgument();
    }

    public static ASimpleRestrictionType getType(CommandContext<CommandSourceStack> context, String name) {
        return context.getArgument(name, ASimpleRestrictionType.class);
    }

    @SuppressWarnings("ConstantConditions")
    @Override
    public ASimpleRestrictionType parse(StringReader stringReader) throws CommandSyntaxException {
        var typeString = stringReader.readString();
        ResourceLocation associatedResourceLocation;

        if (!typeString.contains(":")) {
            associatedResourceLocation = AResourceLocation.fromNamespaceAndPath(typeString);
        } else {
            associatedResourceLocation = AResourceLocation.parse(typeString);
        }

        try {
            return AStagesRegistries.SIMPLE_RESTRICTION_TYPES.get(associatedResourceLocation);
        } catch (IllegalArgumentException exception) {
            throw ERROR_INVALID_TYPE.create(typeString);
        }
    }

    @ToDo("Choose to show shortcuts or not!")
    @Override
    public <S> CompletableFuture<Suggestions> listSuggestions(CommandContext<S> context, SuggestionsBuilder builder) {
        var suggestionList = new ArrayList<String>(); // Order-maintaining needed!

        AStagesRegistries.SIMPLE_RESTRICTION_TYPES
            .keySet()
            .forEach(key -> {
//                if (key.getNamespace().equals(AStages.MODID)) {
//                    suggestionList.add(key.getPath());
//                }

                suggestionList.add(key.toString());
            });

        return SharedSuggestionProvider.suggest(suggestionList, builder);
    }

    @Override
    public Collection<String> getExamples() {
        return EXAMPLES;
    }
}