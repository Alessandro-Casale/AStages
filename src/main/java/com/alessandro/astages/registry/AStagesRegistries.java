package com.alessandro.astages.registry;

import com.alessandro.astages.AStages;
import com.alessandro.astages.api.AResourceLocation;
import com.alessandro.astages.api.nullability.NotNullParamsAndMethodsReturn;
import com.alessandro.astages.simple.ASimpleRestrictionTypeDeprecated;
import com.alessandro.astages.store.ARestrictionType;
import com.alessandro.astages.store.ASimpleRestrictionType;
import com.alessandro.astages.store.Attribute;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;

@NotNullParamsAndMethodsReturn
public class AStagesRegistries {
    public static final Registry<Attribute<?>> ATTRIBUTES = AStages.ATTRIBUTES_REGISTRY;
    public static final Registry<ARestrictionType> RESTRICTION_TYPES = AStages.RESTRICTION_TYPES_REGISTRY;
    public static final Registry<ASimpleRestrictionType> SIMPLE_RESTRICTION_TYPES = AStages.SIMPLE_RESTRICTION_TYPES_REGISTRY;

    public static class Keys {
        public static final ResourceKey<Registry<Attribute<?>>> ATTRIBUTES = ResourceKey.createRegistryKey(AResourceLocation.fromNamespaceAndPath("attributes"));
        public static final ResourceKey<Registry<ARestrictionType>> RESTRICTION_TYPES = ResourceKey.createRegistryKey(AResourceLocation.fromNamespaceAndPath("restriction_types"));
        public static final ResourceKey<Registry<ASimpleRestrictionType>> SIMPLE_RESTRICTION_TYPES = ResourceKey.createRegistryKey(AResourceLocation.fromNamespaceAndPath("simple_restriction_types"));
    }
}
