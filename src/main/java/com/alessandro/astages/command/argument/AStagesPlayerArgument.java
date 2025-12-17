package com.alessandro.astages.command.argument;

import com.alessandro.astages.api.ACommandUtils;
import com.alessandro.astages.api.develop.NotYetImplemented;
import com.alessandro.astages.api.nullability.NotNullParamsAndMethodsReturn;
import com.alessandro.astages.capability.OfflinePlayerStage;
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
import java.util.HashSet;
import java.util.concurrent.CompletableFuture;

@NotNullParamsAndMethodsReturn
public class AStagesPlayerArgument implements ArgumentType<String> {
    private static final Collection<String> EXAMPLES = Arrays.asList("player_username_1", "player_username_2");

    @Contract(value = " -> new", pure = true)
    public static AStagesPlayerArgument onlineAndOfflinePlayers() {
        return new AStagesPlayerArgument();
    }

    public static String getPlayer(CommandContext<CommandSourceStack> context, String name) {
        return context.getArgument(name, String.class);
    }

    @Override
    public String parse(StringReader reader) throws CommandSyntaxException {
        return ACommandUtils.parseGenericString(reader);
    }

    @NotYetImplemented("Add entity selector support")
    @Override
    public <S> CompletableFuture<Suggestions> listSuggestions(CommandContext<S> context, SuggestionsBuilder builder) {
        S source = context.getSource();
        var suggestionList = new HashSet<String>(); // TODO: Maintain insertion order! (First online players, then offline ones!)

        if (source instanceof CommandSourceStack commandSource) {
            var server = commandSource.getServer();
            var players = server.getPlayerList().getPlayers();

            players.forEach(player -> suggestionList.add(player.getGameProfile().getName()));
        }

        suggestionList.addAll(OfflinePlayerStage.USERNAME_UUID.keySet());

        return SharedSuggestionProvider.suggest(suggestionList, builder);
    }

    @Override
    public Collection<String> getExamples() {
        return EXAMPLES;
    }
}
