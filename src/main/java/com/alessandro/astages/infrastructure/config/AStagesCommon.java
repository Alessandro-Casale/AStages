package com.alessandro.astages.infrastructure.config;

import com.alessandro.astages.api.config.DisplayType;
import com.alessandro.astages.api.config.SimpleLocation;
import com.alessandro.astages.engine.store.StageAttributes;
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

public class AStagesCommon {
    private static final ModConfigSpec.Builder BUILDER = new ModConfigSpec.Builder();
    public static final ModConfigSpec SPEC;

    // --- NOTIFICATIONS ---
    public static final ModConfigSpec.ConfigValue<Boolean> ENABLE_STAGE_WARNING;

    // --- STAGE ADDITION NOTIFICATIONS ---
    public static final ModConfigSpec.EnumValue<DisplayType> STAGE_ADD_DISPLAY_TYPE;
    public static final ModConfigSpec.EnumValue<ChatFormatting> STAGE_ADD_COLOR;
    public static final ModConfigSpec.IntValue STAGE_ADD_FADE_IN_TICKS;
    public static final ModConfigSpec.IntValue STAGE_ADD_STAY_TICKS;
    public static final ModConfigSpec.IntValue STAGE_ADD_FADE_OUT_TICKS;

    // --- STAGE REMOVAL NOTIFICATIONS ---
    public static final ModConfigSpec.EnumValue<DisplayType> STAGE_REMOVE_DISPLAY_TYPE;
    public static final ModConfigSpec.EnumValue<ChatFormatting> STAGE_REMOVE_COLOR;
    public static final ModConfigSpec.IntValue STAGE_REMOVE_FADE_IN_TICKS;
    public static final ModConfigSpec.IntValue STAGE_REMOVE_STAY_TICKS;
    public static final ModConfigSpec.IntValue STAGE_REMOVE_FADE_OUT_TICKS;

    // --- FILE MANAGEMENT ---
    public static final ModConfigSpec.EnumValue<SimpleLocation> SIMPLE_RESTRICTIONS_FOLDER;
    public static final ModConfigSpec.IntValue SIMPLE_RESTRICTIONS_RELOADABLE;
    public static final ModConfigSpec.ConfigValue<Boolean> PRETTY_PRINT_SIMPLE_RESTRICTIONS;

    // --- MECHANICS ---
    public static final ModConfigSpec.ConfigValue<Boolean> ENABLE_ADD_ALL_OPERATION;
    public static final ModConfigSpec.ConfigValue<Boolean> ENABLE_REMOVE_ALL_OPERATION;
    public static final ModConfigSpec.ConfigValue<Boolean> FORCE_LAST_LOOT_MODIFIER;
//    public static final ModConfigSpec.EnumValue<RestrictedItemBehavior> RESTRICTED_ITEM_BEHAVIOR;
//    public static final ModConfigSpec.IntValue MESSAGE_COOLDOWN_TICKS;

    // --- DEVELOPER ---
    public static final ModConfigSpec.ConfigValue<Boolean> ENABLE_TEST_MODE;
    public static final ModConfigSpec.ConfigValue<Boolean> ENABLE_DEV_LOGS;
    public static final ModConfigSpec.ConfigValue<Boolean> SHOW_SPAWN_TYPES;
    public static final ModConfigSpec.ConfigValue<List<? extends String>> WHITELIST_SPAWN_TYPES;
    public static final ModConfigSpec.ConfigValue<List<? extends String>> WHITELIST_ENTITY_TYPES;

    static {
        // =========================================
        //             NOTIFICATIONS
        // =========================================
        BUILDER.push("Notifications");

        ENABLE_STAGE_WARNING = BUILDER
            .comment("Show a chat warning to admins when a stage is not associated with any restriction.")
            .define("Enable Missing Stage Warning", true);

        BUILDER.push("Stage Addition Visuals");

        STAGE_ADD_DISPLAY_TYPE = BUILDER
            .comment("How the notification is shown when a player gains a stage. Options: TITLE, ACTION_BAR, CHAT")
            .defineEnum("Display Type", DisplayType.TITLE);

        STAGE_ADD_COLOR = BUILDER
            .comment("The color of the text when a stage is added")
            .defineEnum("Text Color", ChatFormatting.GREEN);

        STAGE_ADD_FADE_IN_TICKS = BUILDER
            .comment("Animation fade-in time in ticks for the title notification")
            .defineInRange("Fade In Ticks", StageAttributes.FADE_IN.getDefaultValue(), 0, 72000);

        STAGE_ADD_STAY_TICKS = BUILDER
            .comment("How long the title notification stays on screen in ticks")
            .defineInRange("Stay Ticks", StageAttributes.STAY.getDefaultValue(), 0, 72000);

        STAGE_ADD_FADE_OUT_TICKS = BUILDER
            .comment("Animation fade-out time in ticks for the title notification")
            .defineInRange("Fade Out Ticks", StageAttributes.FADE_OUT.getDefaultValue(), 0, 72000);

        BUILDER.pop();

        BUILDER.push("Stage Removal Visuals");

        STAGE_REMOVE_DISPLAY_TYPE = BUILDER
            .comment("How the notification is shown when a player loses a stage.")
            .defineEnum("Display Type", DisplayType.CHAT);

        STAGE_REMOVE_COLOR = BUILDER
            .comment("The color of the text when a stage is removed")
            .defineEnum("Text Color", ChatFormatting.RED);

        STAGE_REMOVE_FADE_IN_TICKS = BUILDER
            .comment("Animation fade-in time in ticks for the title notification")
            .defineInRange("Fade In Ticks", 20, 0, 72000);

        STAGE_REMOVE_STAY_TICKS = BUILDER
            .comment("How long the title notification stays on screen in ticks")
            .defineInRange("Stay Ticks", 60, 0, 72000);

        STAGE_REMOVE_FADE_OUT_TICKS = BUILDER
            .comment("Animation fade-out time in ticks for the title notification")
            .defineInRange("Fade Out Ticks", 20, 0, 72000);

        BUILDER.pop();

        BUILDER.pop();

        // =========================================
        //            FILE MANAGEMENT
        // =========================================
        BUILDER.push("File Management");

        SIMPLE_RESTRICTIONS_FOLDER = BUILDER
            .comment("Choose if simple restrictions must be read from the world/server folder or the global config folder.")
            .defineEnum("Read Simple Restrictions", SimpleLocation.CONFIG_FOLDER);

        SIMPLE_RESTRICTIONS_RELOADABLE = BUILDER
            .comment("Set how many times the simple restrictions file is rewritten after additions before forcing a save.")
            .defineInRange("Update File After X Additions", 5, 1, Integer.MAX_VALUE);

        PRETTY_PRINT_SIMPLE_RESTRICTIONS = BUILDER
            .comment("If true, the simple restrictions JSON file will be formatted with clean spacing. If false, it will be saved on a single line (saves space).")
            .define("Pretty Print Simple Restrictions", true);

        BUILDER.pop();

        // =========================================
        //               MECHANICS
        // =========================================
        BUILDER.push("Mechanics");

        ENABLE_ADD_ALL_OPERATION = BUILDER
            .comment(
                "If true, execute stage addition operations (adding multiple stages at once via code) will show an alert. ",
                "If false, alerts will not be shown."
            )
            .define("Enable Add All Operation Stage Alert", true);

        ENABLE_REMOVE_ALL_OPERATION = BUILDER
            .comment(
                "If true, bulk stage removal operations (removing multiple stages at once via code) will show an alert. ",
                "If false, alerts will not be shown."
            )
            .define("Enable Remove All Operation Stage Alert", true);

        FORCE_LAST_LOOT_MODIFIER = BUILDER
            .comment(
                "If true, uses an injection Mixin to force AStages' loot restrictions to run as the absolute last check, after all other mods have finished.",
                "If false, relies on Forge's standard Global Loot Modifier registry order (JSON)."
            )
            .define("Force Last Loot Modifier Execution", false);

//        RESTRICTED_ITEM_BEHAVIOR = BUILDER
//            .comment("What happens when a player somehow gets a restricted item they haven't unlocked yet. Options: DROP (drops it on ground), DELETE (removes it), INVENTORY_LOCK (keeps it but makes it un-interactable)")
//            .defineEnum("Restricted Item Behavior", RestrictedItemBehavior.DROP);

//        MESSAGE_COOLDOWN_TICKS = BUILDER
//            .comment("The cooldown (in ticks) before a player can see the action-blocked message again. Prevents chat/actionbar spam when right-clicking rapidly.")
//            .defineInRange("Message Cooldown Ticks", 20, 0, 1200);

        BUILDER.pop();

        // =========================================
        //               DEVELOPER
        // =========================================
        BUILDER.push("Developer");

        ENABLE_TEST_MODE = BUILDER
            .comment("Enable test-mode used by developers to check if all restrictions work as expected.")
            .define("Enable Test Mode", false);

        ENABLE_DEV_LOGS = BUILDER
            .comment("Show detailed backend logs related to development and parsing.")
            .define("Enable Dev Logs", false);

        SHOW_SPAWN_TYPES = BUILDER
            .comment("Log the spawn types for every entity generated via FinalizeSpawnEvent.")
            .define("Enable Mob Spawn Logs", false);

        WHITELIST_SPAWN_TYPES = BUILDER
            .comment("Whitelist spawn types shown in logs if `Enable Mob Spawn Logs` is true.",
                "If empty, all types will be logged. Allowed values: " + Arrays.toString(MobSpawnType.values()))
            .defineList("Whitelist Spawn Types", List.of(), () -> "NATURAL", ConfigUtils.SPAWN_TYPE_VALIDATOR);

        WHITELIST_ENTITY_TYPES = BUILDER
            .comment("Whitelist entity types shown in logs (e.g., 'minecraft:zombie'). If empty, all entities are logged.")
            .defineList("Whitelist Entity Types", List.of(), () -> "minecraft:zombie", ConfigUtils.ENTITY_TYPE_VALIDATOR);

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