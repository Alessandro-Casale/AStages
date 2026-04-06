package com.alessandro.astages.engine;

import com.alessandro.astages.engine.store.AModel;
import net.minecraft.resources.ResourceLocation;

import java.util.HashMap;

public class AModelManager {
    public static final ARegisteredModels MODELS = new ARegisteredModels();

    public static class ARegisteredModels {
        private final HashMap<ResourceLocation, AModel<?>> MODELS = new HashMap<>();

        public <T> AModel<T> registerModel(ResourceLocation id, AModel<T> model) {
            // if (MODELS.containsKey(id)) { AStages.LOGGER.error("Model with ID {} already found in registered models!", id); }
            if (MODELS.containsKey(id)) { return null; }
            MODELS.put(id, model);
            return model;
        }

        public AModel<?> getModel(ResourceLocation id) {
            return MODELS.get(id);
        }
    }
}