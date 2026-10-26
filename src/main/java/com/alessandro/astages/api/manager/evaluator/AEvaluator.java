package com.alessandro.astages.api.manager.evaluator;

import com.alessandro.astages.api.holder.AHolder;
import com.alessandro.astages.api.manager.registry.ARegistry;
import com.alessandro.astages.api.nullability.NotNullParams;
import com.alessandro.astages.api.nullability.Nullable;
import com.alessandro.astages.api.restriction.ARestriction;
import com.alessandro.astages.api.util.AEvaluatorUtils;

@NotNullParams
public record AEvaluator<R extends ARestriction<R, ?, V>, V>(ARegistry<R> registry) {
    public @Nullable R evaluate(AHolder holder, V object) {
        return AEvaluatorUtils.evaluateRestriction(registry.getRestrictions(), AEvaluatorUtils.calculateEvaluationMode(registry), holder, object);
    }
}