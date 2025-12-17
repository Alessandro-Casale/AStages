package com.alessandro.astages.integration.kubejs;

import com.alessandro.astages.core.AModelManager;
import com.alessandro.astages.store.AModel;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;

import java.util.function.Predicate;

@SuppressWarnings("unused")
public class AStagesModelJSUtil {
    public static AModel<Predicate<ItemStack>> createPredicateModel(ResourceLocation resourceLocation, Predicate<ItemStack> predicate) {
        return AModelManager.MODELS.registerModel(resourceLocation, new AModel<>(predicate));
    }
}
