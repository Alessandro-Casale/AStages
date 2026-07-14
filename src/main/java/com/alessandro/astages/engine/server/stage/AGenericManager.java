package com.alessandro.astages.engine.server.stage;

import com.alessandro.astages.AStages;
import com.alessandro.astages.api.nullability.NotNullParams;
import com.alessandro.astages.api.stage.AStageBaseManager;
import com.alessandro.astages.api.stage.BaseStage;
import com.alessandro.astages.api.stage.implementation.AGrantable;
import com.alessandro.astages.engine.server.MiscStorage;
import com.alessandro.astages.engine.store.StageAttributes;

import java.util.HashSet;
import java.util.Set;

@NotNullParams
public class AGenericManager extends AStageBaseManager<BaseStage<?>> {
    public void addStage(BaseStage<?> stage) {
        if (checkForDuplicates(stage)) {
            addStageInternal(stage.getStage(), stage);
        }
    }

    public void addStageNoCheck(BaseStage<?> stage) {
        addStageInternal(stage.getStage(), stage);
    }

    public boolean checkForDuplicates(BaseStage<?> stage) {
        if (getStages().containsKey(stage.getStage())) {
            AStages.LOGGER.warn("Trying to modify stage {} twice! Operation not allowed!", stage.getStage());
            return false;
        }

        return true;
    }

    @Override
    public void onReloadFinished() {
        super.onReloadFinished();

        getStages().values().forEach(stage -> {
            if (stage.isDisabled(StageAttributes.SERVER_ONLY)) {
                MiscStorage.ALL_STAGES.add(stage.getStage());
            }
        });
    }

    public Set<AGrantable> getStagesWithCustomGrantedEvent(Set<String> stageKeys) {
        var toReturn = new HashSet<AGrantable>();

        for (var stageKey : stageKeys) {
            var stage = getStage(stageKey);

            if (stage != null && !stage.isValueNull(StageAttributes.GRANTED_EVENT)) {
                toReturn.add(stage);
            }
        }

        return toReturn;
    }

    public boolean isServerOnly(String stageKey) {
        var stage = getStage(stageKey);
        if (stage != null) { return stage.get(StageAttributes.SERVER_ONLY); }

        return false;
    }

    public boolean isPlayerOnly(String stageKey) {
        var stage = getStage(stageKey);
        if (stage != null) { return stage.get(StageAttributes.PLAYER_ONLY); }

        return false;
    }
}