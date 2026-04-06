package com.alessandro.astages.infrastructure.integration.kubejs.util;

import com.alessandro.astages.engine.AModelManager;
import com.alessandro.astages.engine.store.AModel;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;

import java.util.function.Predicate;

@SuppressWarnings("unused")
public class KubeJSModelUtils {
    public static AModel<Predicate<ItemStack>> createPredicateModel(ResourceLocation resourceLocation, Predicate<ItemStack> predicate) {
        return AModelManager.MODELS.registerModel(resourceLocation, new AModel<>(predicate));
    }
}
