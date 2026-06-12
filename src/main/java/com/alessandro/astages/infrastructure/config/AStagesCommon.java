package com.alessandro.astages.infrastructure.config;

import com.alessandro.astages.api.constant.ASimpleLocation;
import com.alessandro.astages.api.nullability.NotNullMethodsReturn;
import net.minecraft.ChatFormatting;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobSpawnType;
import net.neoforged.neoforge.common.ModConfigSpec;
import org.jetbrains.annotations.Unmodifiable;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

@NotNullMethodsReturn
public class AStagesCommon {
    private static final ModConfigSpec.Builder BUILDER = new ModConfigSpec.Builder();
    public static final ModConfigSpec SPEC;

    public static final ModConfigSpec.ConfigValue<Boolean> ENABLE_TITLE_AFTER_STAGE_ADDING;
    public static final ModConfigSpec.EnumValue<ChatFormatting> TITLE_COLOR;
    public static final ModConfigSpec.ConfigValue<Integer> TICK_STRUCTURE_UPDATING;
    public static final ModConfigSpec.ConfigValue<Boolean> ENABLE_DEV_LOGS;
    public static final ModConfigSpec.ConfigValue<Boolean> ENABLE_STAGE_WARNING;
    public static final ModConfigSpec.EnumValue<ASimpleLocation> SIMPLE_RESTRICTIONS_FOLDER;
    public static final ModConfigSpec.IntValue SIMPLE_RESTRICTIONS_RELOADABLE;
    public static final ModConfigSpec.ConfigValue<Boolean> ENABLE_TEST_MODE;
    public static final ModConfigSpec.ConfigValue<Boolean> SHOW_SPAWN_TYPES;
    public static final ModConfigSpec.ConfigValue<List<? extends String>> WHITELIST_SPAWN_TYPES;
    public static final ModConfigSpec.ConfigValue<List<? extends String>> WHITELIST_ENTITY_TYPES;

    static {
        BUILDER.push("Configs for AStages Mod");

        ENABLE_TITLE_AFTER_STAGE_ADDING = BUILDER.comment("Enable or disable if mod show id title for each stage add to id player")
            .define("Enable Titles", true);

        TITLE_COLOR = BUILDER.comment("If previous setting is enabled, set the color of the title that appears")
            .defineEnum("Title color", ChatFormatting.RED);

        TICK_STRUCTURE_UPDATING = BUILDER.comment("Every how many ticks the updating of the structures in which the player is located is required")
            .define("Tick Structure Updating", 1);

        ENABLE_STAGE_WARNING = BUILDER.comment("Show warning when id stage is not associated to any restriction")
            .define("Enable Warning", true);

        SIMPLE_RESTRICTIONS_FOLDER = BUILDER.comment("Choose if simple restrictions must be read in world/server folder or config folder")
            .defineEnum("Read Simple Restrictions", ASimpleLocation.CONFIG_FOLDER);

        SIMPLE_RESTRICTIONS_RELOADABLE = BUILDER.comment("Set how many times simple restrictions file is re-written")
            .defineInRange("Update file After X Additions", 5, 1, Integer.MAX_VALUE);

        ENABLE_TEST_MODE = BUILDER.comment("Enable test-mode used by developers to check if all restrictions work as expected")
            .define("Enable Test Mode", false);

        BUILDER.push("Dev");

        ENABLE_DEV_LOGS = BUILDER.comment("Show logs related to dev things!")
            .define("Enable Dev Logs", false);

        SHOW_SPAWN_TYPES = BUILDER.comment("Show spawn types in logs for every entity type that is generated via FinalizeSpawnEvent")
            .define("Enable Mob Related Logs", false);

        WHITELIST_SPAWN_TYPES = BUILDER.comment("Whitelist spawn types showed in logs if `Enable Spawn Types Logs` is true (if empty, all types will be logged). " +
                "Allowed values: " + Arrays.toString(MobSpawnType.values()))
            .defineList("Whitelist Spawn Types", List.of(), () -> "NATURAL", ConfigUtils.SPAWN_TYPE_VALIDATOR);

        WHITELIST_ENTITY_TYPES = BUILDER.comment("Whitelist entity types showed in logs if `Enable Spawn Types Logs` is true (if empty, all types will be logged). (es: 'minecraft:zombie')")
            .defineList("Whitelist Entity Types", List.of(), () -> "minecraft:pig", ConfigUtils.ENTITY_TYPE_VALIDATOR);

        BUILDER.pop();

        BUILDER.pop();

        SPEC = BUILDER.build();
    }

    public static @Unmodifiable Set<MobSpawnType> getWhitelistSpawnTypes() {
        return WHITELIST_SPAWN_TYPES.get().stream()
            .map(String::toUpperCase)
            .map(MobSpawnType::valueOf)
            .collect(Collectors.toSet());
    }

    public static @Unmodifiable Set<EntityType<?>> getWhitelistEntityTypes() {
        return WHITELIST_ENTITY_TYPES.get().stream()
            .map(s -> {
                ResourceLocation rl = ResourceLocation.tryParse(s);
                return rl != null ? BuiltInRegistries.ENTITY_TYPE.get(rl) : null;
            })
            .filter(Objects::nonNull) // Remove null values if something went wrong
            .collect(Collectors.toSet());
    }
}
