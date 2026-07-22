package com.alessandro.astages.infrastructure.integration;

import com.alessandro.astages.api.nullability.NotNullMethodsReturn;
import net.neoforged.fml.ModList;

import java.util.Locale;

@NotNullMethodsReturn
public enum Mods {
    JEI,
    ROUGHLYENOUGHITEMS,
    EMI,
    KUBEJS,
    JADE,
    LOOTJS;

    public boolean isLoaded() {
        return ModList.get().isLoaded(asId());
    }

    public String asId() {
        return toString().toLowerCase(Locale.ROOT);
    }
}
