package com.alessandro.astages.api.manager.evaluator;

import com.alessandro.astages.api.constant.AStageType;
import com.alessandro.astages.api.holder.AHolder;
import com.alessandro.astages.api.manager.registry.ARegistry;
import com.alessandro.astages.api.nullability.NotNullParams;
import com.alessandro.astages.api.nullability.Nullable;
import com.alessandro.astages.api.restriction.ARestriction;
import com.alessandro.astages.api.util.AStagesUtils;

@NotNullParams
public record AEvaluator<R extends ARestriction<R, ?, V>, V>(ARegistry<R> registry) {
    public @Nullable R evaluate(AHolder holder, V object) {
        if (holder.isServerActive()) {
            var serverRestriction = registry.stream().filter(r ->
                !AStagesUtils.hasStage(holder, AStageType.SERVER, r.getStage()) &&
                    r.isRestricted(object)
            ).findFirst().orElse(null);

            if (serverRestriction == null) { return null; } // If the stage is unlocked in the server, pass!
        }

        if (holder.isPlayerActive()) {
            return registry.stream().filter(r ->
                !AStagesUtils.hasStage(holder, AStageType.PLAYER, r.getStage()) &&
                    r.isRestricted(object)
            ).findFirst().orElse(null);
        }

        return null;
    }
}