package com.alessandro.astages.infrastructure.integration.kubejs.util;

import com.alessandro.astages.engine.AClientModelManager;
import com.alessandro.astages.engine.store.AModel;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;

import java.util.function.Predicate;

@SuppressWarnings("unused")
public class KubeJSClientModelUtils {
    public static AModel<Predicate<ItemStack>> createPredicateModel(ResourceLocation resourceLocation, Predicate<ItemStack> predicate) {
        return AClientModelManager.MODELS.registerModel(resourceLocation, new AModel<>(predicate));
    }
}