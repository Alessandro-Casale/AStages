package com.alessandro.astages.util.underdevelopment;

import com.alessandro.astages.api.constant.AOperation;
import com.alessandro.astages.api.constant.AStageType;
import com.alessandro.astages.api.nullability.NotNullParamsAndMethodsReturn;
import com.alessandro.astages.api.nullability.Nullable;
import com.google.common.collect.Sets;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;
import java.util.UUID;

@NotNullParamsAndMethodsReturn
public class StageWork {
    static {
        var customWork = new StageWork(AStageType.PLAYER, AOperation.ADD, null, false)
            .setStages(Sets.newHashSet("stage1"));

        // On Login
        if (customWork.mustBePerformed()) { /* PERFORM */ }
    }

    private final AStageType containerType;
    private final AOperation operation;
    private final LocalDateTime performWhen;
    private final boolean destroyAfterExecution;
    private List<UUID> players;
    private Set<String> stages;

    public StageWork(AStageType containerType, AOperation operation, @Nullable LocalDateTime performWhen, boolean destroyAfterExecution) {
        this.containerType = containerType;
        this.operation = operation;
        this.performWhen = performWhen;
        this.destroyAfterExecution = destroyAfterExecution;
    }

    public StageWork addPlayer(UUID uuid) {
        players.add(uuid);
        return this;
    }

    public StageWork setStages(Set<String> stages) {
        this.stages = stages;
        return this;
    }

    public boolean mustBePerformed() {
        if (performWhen == null) { return true; }
        return performWhen.isBefore(LocalDateTime.now());
    }

    public boolean isForAllPlayers() {
        return players == null;
    }
}
