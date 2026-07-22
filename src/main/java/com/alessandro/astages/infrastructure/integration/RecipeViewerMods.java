package com.alessandro.astages.infrastructure.integration;

import com.alessandro.astages.api.nullability.NotNullParams;
import com.alessandro.astages.api.nullability.Nullable;
import com.alessandro.astages.infrastructure.config.AStagesClient;

import java.util.Arrays;
import java.util.Comparator;

@NotNullParams
public enum RecipeViewerMods {
    JEI(1),
    REI(10),
    EMI(9),
    DEFAULT(-1),
    NONE(-2);

    private static @Nullable RecipeViewerMods CACHE;
    private final int priority;

    RecipeViewerMods(int priority) {
        this.priority = priority;
    }

    public int getPriority() {
        return priority;
    }

    public static boolean isViewerActive(RecipeViewerMods viewer) {
        if (CACHE == null) {
            buildCache();
        }

        return CACHE == viewer;
    }

    private static void buildCache() {
        var configValue = AStagesClient.RECIPE_VIEWER_MOD.get();

        switch (configValue) {
            case JEI, REI, EMI:
                var modValue = getModValue(configValue);
                if (modValue != null && modValue.isLoaded()) {
                    CACHE = configValue;
                } else {
                    CACHE = resolveHighestPriorityLoaded();
                }
                break;
            case DEFAULT:
                CACHE = resolveHighestPriorityLoaded();
                break;
            case NONE:
                CACHE = NONE;
                break;
        }
    }

    private static RecipeViewerMods resolveHighestPriorityLoaded() {
        var sortedValues = Arrays.stream(RecipeViewerMods.values())
            .filter(v -> v.priority > 0)
            .sorted(Comparator.comparingInt(v -> -v.getPriority()))
            .toList();

        for (var viewer : sortedValues) {
            var modValue = getModValue(viewer);
            if (modValue != null && modValue.isLoaded()) {
                return viewer;
            }
        }

        return NONE;
    }

    private static @Nullable Mods getModValue(RecipeViewerMods viewer) {
        return switch (viewer) {
            case JEI -> Mods.JEI;
            case REI -> Mods.ROUGHLYENOUGHITEMS;
            case EMI -> Mods.EMI;
            default -> null;
        };
    }

    public static void clearCache() {
        CACHE = null;
    }
}
