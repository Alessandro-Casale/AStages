package com.alessandro.astages.api.constant;

import net.minecraft.util.ByIdMap;

import java.util.function.IntFunction;

public enum ReloadType {
    CLIENT_BEFORE(0),
    CLIENT_SYNC(1),
    RELOAD_BEFORE(2),
    ORE(3),

    // For JEI!
    JEI_ITEM(4),
    JEI_RECIPE(5),

    // For MarkAsDirty methods!
    ITEM(6),
    RECIPE(7);

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
