package com.alessandro.astages.api.stage;

import com.alessandro.astages.api.nullability.Nullable;

import java.util.HashMap;
import java.util.Map;

public abstract class AStageClientBaseManager<T> {
    private final Map<String, T> STAGES = new HashMap<>();

    public void reloadBeforeScripts() {
        STAGES.clear();
    }

    public void reloadAfterScripts() { }

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
