package com.alessandro.astages.integration;

import net.minecraftforge.fml.ModList;
import org.jetbrains.annotations.NotNull;

import java.util.Locale;

public enum Mods {
    JEI,
    KUBEJS;

    public boolean isLoaded() {
        return ModList.get().isLoaded(asId());
    }

    public @NotNull String asId() {
        return toString().toLowerCase(Locale.ROOT);
    }

//    public void ifInstalled() {
//
//    }
}
