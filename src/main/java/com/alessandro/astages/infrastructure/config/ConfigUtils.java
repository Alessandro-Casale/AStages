package com.alessandro.astages.infrastructure.config;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.MobSpawnType;

import java.util.function.Predicate;

public class ConfigUtils {
    public static final Predicate<Object> ENTITY_TYPE_VALIDATOR = obj -> {
        if (obj instanceof String s) {
            ResourceLocation rl = ResourceLocation.tryParse(s.toLowerCase());
            if (rl != null) {
                return BuiltInRegistries.ENTITY_TYPE.containsKey(rl);
            }
        }
        return false;
    };

    public static final Predicate<Object> SPAWN_TYPE_VALIDATOR = obj -> {
        if (obj instanceof String s) {
            try {
                MobSpawnType.valueOf(s.toUpperCase());
                return true;
            } catch (IllegalArgumentException e) {
                return false;
            }
        }
        return false;
    };
}
