package com.alessandro.astages.infrastructure.config;

import net.neoforged.neoforge.common.ModConfigSpec;

public class AStagesClient {
    private static final ModConfigSpec.Builder BUILDER = new ModConfigSpec.Builder();
    public static final ModConfigSpec SPEC;

//    public static final ModConfigSpec.ConfigValue<Boolean> ENABLE_CLIENT_EXPERIMENTAL_SETTINGS;
    public static final ModConfigSpec.ConfigValue<Boolean> LEVEL_CHUNK_EXPERIMENTAL_SETTINGS;
    public static final ModConfigSpec.ConfigValue<Boolean> LEVEL_CHUNK_SECTION_EXPERIMENTAL_SETTINGS;

    static {
        BUILDER.push("Client Configs for AStages Mod");

//        ENABLE_CLIENT_EXPERIMENTAL_SETTINGS = BUILDER.comment("Enable experimental settings for ore restrictions, client side")
//                .define("Enable Experiments", false);

        LEVEL_CHUNK_EXPERIMENTAL_SETTINGS = BUILDER.comment("Enable experimental settings for LevelChunk class, client side")
                .define("Enable Experiments for LevelChunk", false);

        LEVEL_CHUNK_SECTION_EXPERIMENTAL_SETTINGS = BUILDER.comment("Enable experimental settings for LevelChunkSection class, client side")
                .define("Enable Experiments for LevelChunkSection", false);

        BUILDER.pop();

        SPEC = BUILDER.build();
    }
}