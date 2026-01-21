package com.alessandro.astages.api;

import com.alessandro.astages.api.nullability.NotNullParamsAndMethodsReturn;
import net.minecraft.core.Registry;

import java.util.HashSet;
import java.util.Set;

@NotNullParamsAndMethodsReturn
public class ARegistryUtils {
    public static Set<String> getAllUniqueKeys(Registry<?> registry) {
        Set<String> namespaces = new HashSet<>();

        for (var resourceLocation : registry.keySet()) {
            namespaces.add(resourceLocation.getNamespace());
        }

        return namespaces;
    }
}