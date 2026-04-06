package com.alessandro.astages.api.manager.evaluator;

import com.alessandro.astages.api.constant.AStageType;
import com.alessandro.astages.api.holder.AClientHolder;
import com.alessandro.astages.api.manager.registry.AClientRegistry;
import com.alessandro.astages.api.nullability.NotNullParams;
import com.alessandro.astages.api.nullability.Nullable;
import com.alessandro.astages.api.restriction.AClientRestriction;
import com.alessandro.astages.api.util.AStagesClientUtils;

@NotNullParams
public record AClientEvaluator<R extends AClientRestriction<R, ?, V>, V>(AClientRegistry<R> registry) {
    public @Nullable R evaluate(AClientHolder holder, V object) {
        if (holder.isServerActive()) {
            var serverRestriction = registry.stream().filter(r ->
                !AStagesClientUtils.hasStage(holder, AStageType.SERVER, r.getStage()) &&
                    r.isRestricted(object)
            ).findFirst().orElse(null);

            if (serverRestriction == null) { return null; } // If the stage is unlocked in the server, pass!
        }

        if (holder.isPlayerActive()) {
            return registry.stream().filter(r ->
                !AStagesClientUtils.hasStage(holder, AStageType.PLAYER, r.getStage()) &&
                    r.isRestricted(object)
            ).findFirst().orElse(null);
        }

        return null;
    }
}