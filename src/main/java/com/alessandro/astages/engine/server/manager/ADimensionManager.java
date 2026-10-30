package com.alessandro.astages.engine.server.manager;

import com.alessandro.astages.api.cache.ResourceLocationCache;
import com.alessandro.astages.api.manager.AManager;
import com.alessandro.astages.api.nullability.NotNullParams;
import com.alessandro.astages.api.stage.AStageContext;
import com.alessandro.astages.api.store.ARestrictionType;
import com.alessandro.astages.engine.server.restriction.ADimensionRestriction;
import com.alessandro.astages.engine.store.ARestrictionTypes;
import net.minecraft.resources.ResourceLocation;

@NotNullParams
public class ADimensionManager extends AManager<ADimensionRestriction, ResourceLocation, ResourceLocation> {
    private final ResourceLocationCache<ADimensionRestriction> dimensionCache = new ResourceLocationCache<>(this) {
        @Override
        public void index(ADimensionRestriction restriction) {
            for (var dimension : restriction.getDimensions()) {
                add(dimension, restriction);
            }
        }
    };

    public ADimensionManager() {
        registerCaches(dimensionCache);
    }

    @Override
    public ADimensionRestriction getRestriction(AStageContext context, ResourceLocation dimension) {
        return dimensionCache.find(context, dimension);
    }

    @Override
    public ARestrictionType associatedType() {
        return ARestrictionTypes.DIMENSION;
    }
}