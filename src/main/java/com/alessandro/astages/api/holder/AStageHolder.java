package com.alessandro.astages.api.holder;

import com.alessandro.astages.api.constant.AStageType;
import com.alessandro.astages.api.nullability.NotNullMethodsReturn;
import org.jetbrains.annotations.Contract;

import java.util.*;

@NotNullMethodsReturn
public class AStageHolder {
    private final Map<AStageType, Set<String>> stages = new HashMap<>();

    @Contract(" -> new")
    public static AStageHolder init() {
        return new AStageHolder();
    }

    public static AStageHolder initAndHold(AStageType type, Set<String> stages) {
        return AStageHolder.init().hold(type, stages);
    }

    public AStageHolder hold(AStageType type, Set<String> stages) {
        this.stages.put(type, stages);
        return this;
    }

    public Set<String> getForType(AStageType type) {
        return stages.getOrDefault(type, new HashSet<>());
    }

    public Set<String> getAllStages() {
        var toReturn = new HashSet<String>();
        stages.forEach((type, stages) -> toReturn.addAll(stages));
        return toReturn;
    }
}
