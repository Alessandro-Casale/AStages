package com.alessandro.astages.api.stage;

import com.alessandro.astages.api.nullability.NotNullParamsAndMethodsReturn;

import java.util.List;
import java.util.Set;

@NotNullParamsAndMethodsReturn
@FunctionalInterface
public interface AStageContext {
    AStageContext NONE = stage -> false;

    boolean isUnlocked(String stage);

    default List<AStageContext> types() {
        return List.of(this);
    }

    static AStageContext of(Set<String> stages) {
        return stages::contains;
    }
}
