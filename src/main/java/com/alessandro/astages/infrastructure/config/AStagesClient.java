package com.alessandro.astages.infrastructure.config;

import com.alessandro.astages.infrastructure.integration.RecipeViewerMods;
import net.neoforged.neoforge.common.ModConfigSpec;

public class AStagesClient {
    private static final ModConfigSpec.Builder BUILDER = new ModConfigSpec.Builder();
    public static final ModConfigSpec SPEC;

    public static final ModConfigSpec.ConfigValue<RecipeViewerMods> RECIPE_VIEWER_MOD;

    public static final ModConfigSpec.BooleanValue ASYNC_CACHE_BUILD;

    public static final ModConfigSpec.ConfigValue<Boolean> LEVEL_CHUNK_EXPERIMENTAL_SETTINGS;
    public static final ModConfigSpec.ConfigValue<Boolean> LEVEL_CHUNK_SECTION_EXPERIMENTAL_SETTINGS;

    static {
        // =========================================
        //               MECHANICS
        // =========================================
        BUILDER.push("Mechanics");

        RECIPE_VIEWER_MOD = BUILDER
            .comment(
                "Which recipe viewer integration to use.",
                "DEFAULT automatically chooses the highest priority installed mod (REI > EMI > JEI).",
                "NONE disables all recipe viewer integrations.",
                "Changes require a server reload to take effect."
            )
            .defineEnum("Recipe Viewer", RecipeViewerMods.DEFAULT);

        BUILDER.pop();

        // =========================================
        //              PERFORMANCES
        // =========================================
        BUILDER.push("Performances");

        ASYNC_CACHE_BUILD = BUILDER
            .comment(
                "NOT YET IMPLEMENTED.",
                "If true, item and fluid recipe viewer caches for stage restrictions are built",
                "asynchronously, reducing stutter upon (re)load at the cost of a slight delay",
                "before restrictions take effect."
            )
            .define("Build Caches Asynchronously", false);

        BUILDER.pop();

        // =========================================
        //              EXPERIMENTAL
        // =========================================
        BUILDER.push("Experimental");

        LEVEL_CHUNK_EXPERIMENTAL_SETTINGS = BUILDER
            .comment("Enable experimental settings for the LevelChunk class (Client-side only).", "Warning: May cause visual glitches.")
            .define("Enable LevelChunk Experiments", false);

        LEVEL_CHUNK_SECTION_EXPERIMENTAL_SETTINGS = BUILDER
            .comment("Enable experimental settings for the LevelChunkSection class (Client-side only).", "Warning: May cause visual glitches.")
            .define("Enable LevelChunkSection Experiments", false);

        BUILDER.pop();

        SPEC = BUILDER.build();
    }
}