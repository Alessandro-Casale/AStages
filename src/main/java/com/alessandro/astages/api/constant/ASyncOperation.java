package com.alessandro.astages.api.constant;

import net.minecraft.util.ByIdMap;

import java.util.function.IntFunction;

public enum ASyncOperation {
    ADD(0),
    REMOVE(1);

    // NeoForge Part
    public static final IntFunction<ASyncOperation> BY_ID =
        ByIdMap.continuous(
            ASyncOperation::getId,
            ASyncOperation.values(),
            ByIdMap.OutOfBoundsStrategy.ZERO
        );

    private final int id;

    ASyncOperation(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }
}
