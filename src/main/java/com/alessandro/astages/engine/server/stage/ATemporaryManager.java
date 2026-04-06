package com.alessandro.astages.engine.server.stage;

import com.alessandro.astages.api.develop.Info;
import com.alessandro.astages.api.feature.ClientSynchronizable;
import com.alessandro.astages.api.nullability.NotNullParams;
import com.alessandro.astages.api.nullability.Nullable;
import com.alessandro.astages.api.stage.AStageBaseManager;
import com.alessandro.astages.api.stage.TemporaryStage;
import com.alessandro.astages.api.stage.TemporaryStageContainer;
import com.alessandro.astages.engine.AStageManager;
import com.alessandro.astages.engine.store.StageAttributes;
import com.alessandro.astages.infrastructure.networking.Networking;
import com.alessandro.astages.infrastructure.networking.packet.stages.SyncTemporaryStageS2C;
import net.minecraft.server.level.ServerPlayer;

import java.util.*;

@NotNullParams
public class ATemporaryManager extends AStageBaseManager<TemporaryStage> implements ClientSynchronizable {
    @Info("For events! Null key store server stages!")
    private final HashMap<UUID, HashMap<String, TemporaryStageContainer>> TEMPORARY_STAGES = new HashMap<>();

    public void addStage(TemporaryStage stage) {
        if (AStageManager.GENERIC_INSTANCE.checkForDuplicates(stage)) {
            AStageManager.GENERIC_INSTANCE.addStageNoCheck(stage);
            addStageInternal(stage.getStage(), stage);
        }
    }

    public Set<TemporaryStage> getStages(Set<String> stageKeys) {
        var toReturn = new HashSet<TemporaryStage>();

        for (var stageKey : stageKeys) {
            var stage = getStage(stageKey);

            if (stage != null) {
                toReturn.add(stage);
            }
        }

        return toReturn;
    }

    public @Nullable Collection<TemporaryStageContainer> getStageContainersForPlayer(UUID uuid) {
        if (!TEMPORARY_STAGES.containsKey(uuid)) { return null; }
        return TEMPORARY_STAGES.get(uuid).values();
    }

    public @Nullable Collection<TemporaryStageContainer> getStageContainersForServer() {
        if (!TEMPORARY_STAGES.containsKey(null)) { return null; }
        return TEMPORARY_STAGES.get(null).values();
    }

    public void addAlreadyObtainedStageToExpire(@Nullable UUID uuid, String stageKey, Integer actualTimer) {
        TEMPORARY_STAGES.computeIfAbsent(uuid, k -> new HashMap<>())
            .put(stageKey, new TemporaryStageContainer(AStageManager.TEMPORARY_INSTANCE.getStage(stageKey), actualTimer));
    }

    public void addAlreadyObtainedServerStageToExpire(String stageKey, Integer actualTimer) {
        addAlreadyObtainedStageToExpire(null, stageKey, actualTimer);
    }

    public void addStageToExpire(@Nullable UUID uuid, String stageKey) {
        TEMPORARY_STAGES.computeIfAbsent(uuid, k -> new HashMap<>())
            .put(stageKey, new TemporaryStageContainer(AStageManager.TEMPORARY_INSTANCE.getStage(stageKey)));
    }

    public void addServerStageToExpire(String stageKey) {
        addStageToExpire(null, stageKey);
    }

    @Override
    public void synchronizeWithClient(@Nullable ServerPlayer player) {
        getStages().forEach((stageKey, stage) ->
            Networking.sendTo(player, new SyncTemporaryStageS2C(stage.getStage(), stage.get(StageAttributes.ICON)))
        );
    }
}