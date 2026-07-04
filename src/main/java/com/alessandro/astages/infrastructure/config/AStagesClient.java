package com.alessandro.astages.infrastructure.config;

import net.neoforged.neoforge.common.ModConfigSpec;

public class AStagesClient {
    private static final ModConfigSpec.Builder BUILDER = new ModConfigSpec.Builder();
    public static final ModConfigSpec SPEC;

    public static final ModConfigSpec.ConfigValue<Boolean> LEVEL_CHUNK_EXPERIMENTAL_SETTINGS;
    public static final ModConfigSpec.ConfigValue<Boolean> LEVEL_CHUNK_SECTION_EXPERIMENTAL_SETTINGS;

    static {
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