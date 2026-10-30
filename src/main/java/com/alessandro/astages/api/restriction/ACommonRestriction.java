package com.alessandro.astages.api.restriction;

import com.alessandro.astages.api.nullability.NotNullParamsAndMethodsReturn;
import com.alessandro.astages.api.store.ARestrictionResult;

@NotNullParamsAndMethodsReturn
public interface ACommonRestriction<V> {
    ARestrictionResult isRestricted(V object);

    String getId();
    String getStage();
    int getPriority();
}
