package com.alessandro.astages.api.manager;

import com.alessandro.astages.api.constant.AEvaluationMode;
import com.alessandro.astages.api.manager.registry.AMinimalRegistry;
import com.alessandro.astages.api.reload.AReloadable;
import com.alessandro.astages.api.restriction.ARestriction;

public interface AMinimalManager<R extends ARestriction<?, ?, ?>, V> extends ACommonManager<R, V>, AReloadable {
    @Override
    AMinimalRegistry<R> getRegistry();

    @Override
    default AMinimalManager<R, V> withForcedEvaluationMode(AEvaluationMode mode) {
        getRegistry().withForcedEvaluationMode(mode);
        return this;
    }
}