package com.alessandro.astages.api.manager;

import com.alessandro.astages.api.constant.AEvaluationMode;
import com.alessandro.astages.api.holder.ARestrictionHolder;
import com.alessandro.astages.api.manager.registry.ACommonRegistry;
import com.alessandro.astages.api.restriction.ACommonRestriction;
import com.alessandro.astages.api.stage.AStageContext;
import com.alessandro.astages.api.store.ARestrictionType;
import com.alessandro.astages.api.store.container.AStore;

public interface ACommonManager<R extends ACommonRestriction<?> & AStore<?>, V> {
    R getRestriction(String id);
    default ARestrictionHolder<R> getHolder(String id) {
        return ARestrictionHolder.hold(getRestriction(id));
    }

    R getRestriction(AStageContext context, V object);
    default ARestrictionHolder<R> getHolder(AStageContext context, V object) {
        return ARestrictionHolder.hold(getRestriction(context, object));
    }

    void removeRestriction(String id);

    ACommonRegistry<R> getRegistry();

    default ACommonManager<R, V> withForcedEvaluationMode(AEvaluationMode mode) {
        getRegistry().withForcedEvaluationMode(mode);
        return this;
    }

    ARestrictionType associatedType();
}
