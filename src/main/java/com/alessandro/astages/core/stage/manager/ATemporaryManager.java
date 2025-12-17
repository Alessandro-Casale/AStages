package com.alessandro.astages.core.stage.manager;

import com.alessandro.astages.api.develop.Info;
import com.alessandro.astages.api.nullability.NotNullParams;
import com.alessandro.astages.api.nullability.Nullable;
import com.alessandro.astages.api.stage.TemporaryStage;
import com.alessandro.astages.api.stage.TemporaryStageContainer;
import com.alessandro.astages.core.AStageManager;
import com.alessandro.astages.store.stage.AStageBaseManager;

import java.util.*;

@NotNullParams
public class ATemporaryManager extends AStageBaseManager<TemporaryStage> {
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
}
