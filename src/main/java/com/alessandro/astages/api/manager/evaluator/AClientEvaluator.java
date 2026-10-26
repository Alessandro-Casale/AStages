package com.alessandro.astages.api.manager.evaluator;

import com.alessandro.astages.api.holder.AClientHolder;
import com.alessandro.astages.api.manager.registry.AClientRegistry;
import com.alessandro.astages.api.nullability.NotNullParams;
import com.alessandro.astages.api.nullability.Nullable;
import com.alessandro.astages.api.restriction.AClientRestriction;
import com.alessandro.astages.api.util.AClientEvaluatorUtils;

@NotNullParams
public record AClientEvaluator<R extends AClientRestriction<R, ?, V>, V>(AClientRegistry<R> registry) {
    public @Nullable R evaluate(AClientHolder holder, V object) {
        return AClientEvaluatorUtils.evaluateRestriction(registry.getRestrictions(), AClientEvaluatorUtils.calculateEvaluationMode(registry), holder, object);
    }
}