package com.alessandro.astages.config;

import com.alessandro.astages.simple.ASimpleLocation;
import net.minecraft.ChatFormatting;
import net.neoforged.neoforge.common.ModConfigSpec;

public class AStagesCommon {
    private static final ModConfigSpec.Builder BUILDER = new ModConfigSpec.Builder();
    public static final ModConfigSpec SPEC;

    public static final ModConfigSpec.ConfigValue<Boolean> ENABLE_TITLE_AFTER_STAGE_ADDING;
    public static final ModConfigSpec.EnumValue<ChatFormatting> TITLE_COLOR;
    public static final ModConfigSpec.ConfigValue<Integer> TICK_STRUCTURE_UPDATING;
    public static final ModConfigSpec.ConfigValue<Boolean> ENABLE_LOGS;
    public static final ModConfigSpec.ConfigValue<Boolean> ENABLE_STAGE_WARNING;
    public static final ModConfigSpec.EnumValue<ASimpleLocation> SIMPLE_RESTRICTIONS_FOLDER;
    public static final ModConfigSpec.IntValue SIMPLE_RESTRICTIONS_RELOADABLE;
    public static final ModConfigSpec.ConfigValue<Boolean> ENABLE_TEST_MODE;

    static {
        BUILDER.push("Configs for AStages Mod");

        ENABLE_TITLE_AFTER_STAGE_ADDING = BUILDER.comment("Enable or disable if mod show a title for each stage add to a player")
            .define("Enable Titles", true);

        TITLE_COLOR = BUILDER.comment("If previous setting is enabled, set the color of the title that appears")
            .defineEnum("Title color", ChatFormatting.RED);

        TICK_STRUCTURE_UPDATING = BUILDER.comment("Every how many ticks the updating of the structures in which the player is located is required")
            .define("Tick Structure Updating", 1);

        ENABLE_LOGS = BUILDER.comment("Show logs related to dev things!")
            .define("Enable Logs", true);

        ENABLE_STAGE_WARNING = BUILDER.comment("Show warning when a stage is not associated to any restriction")
            .define("Enable Warning", true);

        SIMPLE_RESTRICTIONS_FOLDER = BUILDER.comment("Choose if simple restrictions must be read in world/server folder or config folder")
            .defineEnum("Read Simple Restrictions", ASimpleLocation.CONFIG_FOLDER);

        SIMPLE_RESTRICTIONS_RELOADABLE = BUILDER.comment("Set how many times simple restrictions file is re-written")
            .defineInRange("Update file After X Additions", 5, 1, Integer.MAX_VALUE);

        ENABLE_TEST_MODE = BUILDER.comment("Enable test-mode used by developers to check if all restrictions work as expected")
            .define("Enable Test Mode", false);

        BUILDER.pop();

        SPEC = BUILDER.build();
    }
}
