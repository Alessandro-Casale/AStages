package com.alessandro.astages.core.client.model;

import com.alessandro.astages.AStages;
import com.alessandro.astages.store.AModel;
import net.minecraft.resources.ResourceLocation;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class AClientRegisteredModels {
    private final Map<ResourceLocation, AModel<?>> MODELS = new HashMap<>();

    public <T> AModel<T> registerModel(ResourceLocation id, AModel<T> model) {
        if (MODELS.containsKey(id)) {
            AStages.LOGGER.error("Model with id `{}` already found in client registered models, skipped!", id);
            return null;
        }

        MODELS.put(id, model);
        return model;
    }

    public AModel<?> getModel(ResourceLocation id) {
        return MODELS.get(id);
    }

    public Set<ResourceLocation> getModels() {
        return MODELS.keySet();
    }
}
