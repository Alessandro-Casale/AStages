package com.alessandro.astages.engine.server.manager;

import com.alessandro.astages.api.cache.server.ResourceLocationCache;
import com.alessandro.astages.api.holder.AHolder;
import com.alessandro.astages.api.manager.AManager;
import com.alessandro.astages.api.nullability.NotNullParams;
import com.alessandro.astages.api.store.ARestrictionType;
import com.alessandro.astages.engine.server.restriction.ADimensionRestriction;
import com.alessandro.astages.engine.store.ARestrictionTypes;
import net.minecraft.resources.ResourceLocation;

@NotNullParams
public class ADimensionManager extends AManager<ADimensionRestriction, ResourceLocation, ResourceLocation> {
    public final ResourceLocationCache<ADimensionRestriction> dimensionCache = new ResourceLocationCache<>() {
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
    public ADimensionRestriction getRestriction(AHolder holder, ResourceLocation dimension) {
        return dimensionCache.find(holder, dimension);
    }

    @Override
    public ARestrictionType associatedType() {
        return ARestrictionTypes.DIMENSION;
    }
}