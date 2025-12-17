package com.alessandro.astages.api;

import com.alessandro.astages.AStages;
import com.alessandro.astages.api.nullability.NotNullMethodsReturn;
import net.minecraft.resources.ResourceLocation;

@NotNullMethodsReturn
public class AResourceLocation {
    public static ResourceLocation fromNamespaceAndPath(String path) {
        return ResourceLocation.fromNamespaceAndPath(AStages.MODID, path);
    }

    public static ResourceLocation fromNamespaceAndPath(String namespace, String path) {
        return ResourceLocation.fromNamespaceAndPath(namespace, path);
    }

    public static ResourceLocation parse(String location) {
        return ResourceLocation.parse(location);
    }
}
