package com.alessandro.astages.api.constant;

@Deprecated(forRemoval = true, since = "3.0.0")
public enum ReloadType {
    CLIENT_BEFORE,
    CLIENT_SYNC,
    RELOAD_BEFORE,
    ORE,

    // For JEI!
    JEI_ITEM,
    JEI_RECIPE,

    // For MarkAsDirty methods!
    ITEM,
    RECIPE
}
