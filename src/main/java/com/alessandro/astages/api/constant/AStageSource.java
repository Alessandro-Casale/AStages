package com.alessandro.astages.api.constant;

import net.minecraft.util.ByIdMap;

import java.util.function.IntFunction;

public enum AStageSource {
    PLAYER(0), SERVER(1), BOTH(2);

    AStageSource(int id) {
        this.id = id;
    }

    // NeoForge Part
    public static final IntFunction<AStageSource> BY_ID =
        ByIdMap.continuous(
            AStageSource::getId,
            AStageSource.values(),
            ByIdMap.OutOfBoundsStrategy.ZERO
        );

    private final int id;

    public int getId() {
        return id;
    }
}
