package com.alessandro.astages.infrastructure.integration.kubejs.wrapper;

import com.mojang.serialization.DataResult;
import dev.latvian.mods.kubejs.error.KubeRuntimeException;
import dev.latvian.mods.kubejs.script.SourceLine;
import dev.latvian.mods.rhino.Context;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EntityType;

import java.util.function.Function;

public class AKubeJSWrappers {
    public static EntityType<?> wrapEntityType(Context context, Object object) {
        return switch (object) {
            case CharSequence cs -> findEntityType(cs.toString())
                .getOrThrow(error -> new KubeRuntimeException("Failed to read item from %s: %s".formatted(cs, error))
                    .source(SourceLine.of(context)));
            case null, default -> EntityType.PIG;
        };
    }

    public static DataResult<EntityType<?>> findEntityType(String s) {
        s = s.trim();
        return switch (s) {
            case "", "-", "pig", "minecraft:pig" -> DataResult.success(EntityType.PIG);
            default -> ResourceLocation.read(s).flatMap(AKubeJSWrappers::findEntityType).map(Holder::value);
        };
    }

    public static DataResult<Holder<EntityType<?>>> findEntityType(ResourceLocation id) {
        return BuiltInRegistries.ENTITY_TYPE
            .getHolder(id)
            .map(DataResult::success)
            .orElseGet(() -> DataResult.error(() -> "Item with ID " + id + " does not exist!"))
            .map(Function.identity());
    }
}
