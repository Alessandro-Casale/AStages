package com.alessandro.astages.api.manager.evaluator;

import com.alessandro.astages.api.manager.registry.AClientRegistry;
import com.alessandro.astages.api.nullability.NotNullParams;
import com.alessandro.astages.api.nullability.Nullable;
import com.alessandro.astages.api.restriction.AClientRestriction;
import com.alessandro.astages.api.stage.AStageContext;
import com.alessandro.astages.api.util.AEvaluatorUtils;

@NotNullParams
public record AClientEvaluator<R extends AClientRestriction<R, ?, V>, V>(AClientRegistry<R> registry) {
    public @Nullable R evaluate(AStageContext context, V object) {
        return AEvaluatorUtils.evaluateRestriction(registry.getRestrictions(), AEvaluatorUtils.calculateEvaluationMode(registry), context, object);
    }
}