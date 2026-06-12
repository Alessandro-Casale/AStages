package com.alessandro.astages.api;

import com.alessandro.astages.AStages;
import com.alessandro.astages.api.develop.Info;
import com.alessandro.astages.api.nullability.NotNullParamsAndMethodsReturn;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;

@NotNullParamsAndMethodsReturn
public class AResourceLocation {
    public static ResourceLocation fromTag(TagKey<Item> tag) {
        return tag.location();
    }

    public static ResourceLocation fromTag(String tag) {
        if (tag.startsWith("#") && tag.contains(":")) {
            String[] parts = tag.substring(1).split(":", 2);
            String namespace = parts[0];
            String path = parts[1];

            return fromNamespaceAndPath(namespace, path);
        } else {
            throw new RuntimeException("Invalid tag: " + tag + "!");
        }

    }

    @Info("Default namespace is `astages`!")
    public static ResourceLocation fromNamespaceAndPath(String path) {
        return ResourceLocation.fromNamespaceAndPath(AStages.MODID, path);
    }

    public static ResourceLocation fromNamespaceAndPath(String namespace, String path) {
        return ResourceLocation.fromNamespaceAndPath(namespace, path);
    }

    public static ResourceLocation parse(String location) {
        return ResourceLocation.parse(location);
    }

    public static ResourceLocation withMinecraftNamespace(String location) {
        return ResourceLocation.withDefaultNamespace(location);
    }
}
