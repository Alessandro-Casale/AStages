package com.alessandro.astages.api.stage;

import com.alessandro.astages.api.nullability.Nullable;
import com.alessandro.astages.api.reload.AReloadable;

import java.util.HashMap;
import java.util.Map;

public abstract class AStageClientBaseManager<T> implements AReloadable {
    private final Map<String, T> STAGES = new HashMap<>();

    @Override
    public void onReloadStarted() {
        STAGES.clear();
    }

    @Override
    public void onReloadFinished() { }

    public void addStageInternal(String key, T stage) {
        STAGES.put(key, stage);
    }

    public Map<String, T> getStages() {
        return STAGES;
    }

    public @Nullable T getStage(String stageKey) {
        return STAGES.getOrDefault(stageKey, null);
    }
}
