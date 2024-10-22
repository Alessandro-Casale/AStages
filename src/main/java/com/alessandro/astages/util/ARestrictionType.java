package com.alessandro.astages.util;

import org.jetbrains.annotations.NotNull;

import java.util.Locale;

public enum ARestrictionType {
    ITEM,
    MOB,
    DIMENSION,
    TIME,
    STRUCTURE,
    RECIPE,
    SCREEN,
    ORE;

    public @NotNull String getId() {
        return toString().toLowerCase(Locale.ROOT);
    }
}
