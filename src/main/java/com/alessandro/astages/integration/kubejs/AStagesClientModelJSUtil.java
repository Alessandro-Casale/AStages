package com.alessandro.astages.integration.kubejs;

import com.alessandro.astages.core.AClientModelManager;
import com.alessandro.astages.store.AModel;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;

import java.util.function.Predicate;

public class AStagesClientModelJSUtil {
    public static AModel<Predicate<ItemStack>> createPredicateModel(ResourceLocation resourceLocation, Predicate<ItemStack> predicate) {
        return AClientModelManager.MODELS.registerModel(resourceLocation, new AModel<>(predicate));
    }
}
