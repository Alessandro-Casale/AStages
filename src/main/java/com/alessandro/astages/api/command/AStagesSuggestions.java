package com.alessandro.astages.api.command;

import com.alessandro.astages.api.holder.AHolder;
import com.alessandro.astages.api.util.AStagesUtils;
import com.alessandro.astages.engine.server.MiscStorage;
import com.mojang.brigadier.suggestion.SuggestionProvider;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.SharedSuggestionProvider;
import net.minecraft.commands.arguments.EntityArgument;

import java.util.HashSet;

public class AStagesSuggestions {
    public static final SuggestionProvider<CommandSourceStack> PLAYER_ADD = (context, builder) -> {
        var toReturn = new HashSet<>(MiscStorage.ALL_STAGES);
        AStagesUtils.getStages(AHolder.players(EntityArgument.getPlayers(context, "player"))).forEach(toReturn::remove);
        toReturn.removeAll(MiscStorage.STAGES_ONLY_FOR_SERVER);
        return SharedSuggestionProvider.suggest(toReturn.stream().sorted(), builder);
    };

    public static final SuggestionProvider<CommandSourceStack> PLAYER_REMOVE = (context, builder) ->
        SharedSuggestionProvider.suggest(AStagesUtils.getStages(AHolder.players(EntityArgument.getPlayers(context, "player"))).stream().sorted(), builder);

    public static final SuggestionProvider<CommandSourceStack> SERVER_ADD = (context, builder) -> {
        var toReturn = new HashSet<>(MiscStorage.ALL_STAGES);
        AStagesUtils.getStages(AHolder.server()).forEach(toReturn::remove);
        toReturn.removeAll(MiscStorage.STAGES_ONLY_FOR_PLAYER);
        return SharedSuggestionProvider.suggest(toReturn.stream().sorted(), builder);
    };

    public static final SuggestionProvider<CommandSourceStack> SERVER_REMOVE = (context, builder) ->
        SharedSuggestionProvider.suggest(AStagesUtils.getStages(AHolder.server()).stream().sorted(), builder);
}