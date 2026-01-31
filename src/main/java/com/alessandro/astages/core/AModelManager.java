package com.alessandro.astages.core;

import com.alessandro.astages.api.misc.Twin;
import com.alessandro.astages.api.nullability.NotNullMethodsReturn;
import com.alessandro.astages.core.server.model.ARegisteredModels;
import net.minecraft.resources.ResourceLocation;

import java.util.HashSet;
import java.util.Set;

@NotNullMethodsReturn
public class AModelManager {
    public static final ARegisteredModels MODELS = new ARegisteredModels();

    public static Twin<Set<ResourceLocation>, Set<ResourceLocation>> missingAndAdditionalModels(Set<ResourceLocation> clientModelIds) {
        var missingModels = new HashSet<>(MODELS.getModels());
        var additionalModels = new HashSet<>(clientModelIds);

        missingModels.removeAll(clientModelIds);
        additionalModels.removeAll(MODELS.getModels());

        return new Twin<>(missingModels, additionalModels);
    }
}
