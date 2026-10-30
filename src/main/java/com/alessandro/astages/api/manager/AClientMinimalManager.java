package com.alessandro.astages.api.manager;

import com.alessandro.astages.api.constant.AEvaluationMode;
import com.alessandro.astages.api.manager.registry.AClientMinimalRegistry;
import com.alessandro.astages.api.reload.AReloadable;
import com.alessandro.astages.api.restriction.AClientRestriction;

public interface AClientMinimalManager<R extends AClientRestriction<?, ?, ?>, V> extends ACommonManager<R, V>, AReloadable {
    @Override
    AClientMinimalRegistry<R> getRegistry();

    @Override
    default AClientMinimalManager<R, V> withForcedEvaluationMode(AEvaluationMode mode) {
        getRegistry().withForcedEvaluationMode(mode);
        return this;
    }
}