package com.alessandro.astages.integration;

import com.alessandro.astages.api.nullability.NotNullMethodsReturn;
import net.neoforged.fml.ModList;

import java.util.Locale;

@NotNullMethodsReturn
public enum Mods {
    JEI,
    KUBEJS,
    JADE;

    public boolean isLoaded() {
        return ModList.get().isLoaded(asId());
    }

    public String asId() {
        return toString().toLowerCase(Locale.ROOT);
    }
}
