package com.alessandro.astages.api.constant;

import net.minecraft.util.ByIdMap;

import java.util.function.IntFunction;

public enum ReloadType {
    CLIENT_BEFORE(0),
    CLIENT_SYNC(1),
    RELOAD_BEFORE(2),
    MODEL_BEFORE(3),
    ORE(4),

    // For JEI!
    JEI_ITEM(5),
    JEI_RECIPE(6),

    // For MarkAsDirty methods!
    ITEM(7),
    RECIPE(8);

    // NeoForge Part
    public static final IntFunction<ReloadType> BY_ID =
        ByIdMap.continuous(
            ReloadType::getId,
            ReloadType.values(),
            ByIdMap.OutOfBoundsStrategy.ZERO
        );

    private final int id;

    ReloadType(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }
}
