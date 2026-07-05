package com.alessandro.astages.api.command;

import com.alessandro.astages.api.holder.AClientHolder;
import com.alessandro.astages.api.util.AStagesClientUtils;
import com.alessandro.astages.engine.client.ClientMiscStorage;
import com.mojang.brigadier.suggestion.SuggestionProvider;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.SharedSuggestionProvider;

import java.util.HashSet;

public class AStagesSuggestions {
    public static final SuggestionProvider<CommandSourceStack> PLAYER_ADD = (context, builder) -> {
        var toReturn = new HashSet<>(ClientMiscStorage.ALL_STAGES);
        AStagesClientUtils.getStages(AClientHolder.player()).forEach(toReturn::remove);
        toReturn.removeAll(ClientMiscStorage.STAGES_ONLY_FOR_SERVER);
        return SharedSuggestionProvider.suggest(toReturn.stream().sorted(), builder);
    };

    public static final SuggestionProvider<CommandSourceStack> PLAYER_REMOVE = (context, builder) ->
        SharedSuggestionProvider.suggest(AStagesClientUtils.getStages(AClientHolder.player()).stream().sorted(), builder);

    public static final SuggestionProvider<CommandSourceStack> SERVER_ADD = (context, builder) -> {
        var toReturn = new HashSet<>(ClientMiscStorage.ALL_STAGES);
        AStagesClientUtils.getStages(AClientHolder.server()).forEach(toReturn::remove);
        toReturn.removeAll(ClientMiscStorage.STAGES_ONLY_FOR_PLAYER);
        return SharedSuggestionProvider.suggest(toReturn.stream().sorted(), builder);
    };

    public static final SuggestionProvider<CommandSourceStack> SERVER_REMOVE = (context, builder) ->
        SharedSuggestionProvider.suggest(AStagesClientUtils.getStages(AClientHolder.server()), builder);
}