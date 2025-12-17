package com.alessandro.astages.registry;

import com.alessandro.astages.AStages;
import com.alessandro.astages.api.AResourceLocation;
import com.alessandro.astages.api.nullability.NotNullParamsAndMethodsReturn;
import com.alessandro.astages.store.Attribute;
import com.alessandro.astages.store.ARestrictionType;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import org.jetbrains.annotations.Unmodifiable;

import java.util.Collection;

@NotNullParamsAndMethodsReturn
public class AStagesRegistries {
    public static @Unmodifiable Collection<Attribute<?>> getAllAttributeEntries() {
        return AStages.ATTRIBUTES_REGISTRY.stream().toList();
    }

    public static @Unmodifiable Collection<ARestrictionType> getAllRestrictionTypeEntries() {
        return AStages.RESTRICTION_TYPES_REGISTRY.stream().toList();
    }

    public static class Keys {
        public static final ResourceKey<Registry<Attribute<?>>> ATTRIBUTES = ResourceKey.createRegistryKey(AResourceLocation.fromNamespaceAndPath("attributes"));
        public static final ResourceKey<Registry<ARestrictionType>> RESTRICTION_TYPES = ResourceKey.createRegistryKey(AResourceLocation.fromNamespaceAndPath("restriction_types"));
    }
}
