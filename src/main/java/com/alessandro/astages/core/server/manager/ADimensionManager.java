package com.alessandro.astages.core.server.manager;

import com.alessandro.astages.api.ARestrictionUtils;
import com.alessandro.astages.api.base.OrderedMultiMap;
import com.alessandro.astages.api.constant.AStageType;
import com.alessandro.astages.api.holder.AHolder;
import com.alessandro.astages.api.nullability.NotNullParams;
import com.alessandro.astages.core.server.restriction.ADimensionRestriction;
import com.alessandro.astages.store.ARestrictionType;
import com.alessandro.astages.store.ARestrictionTypes;
import com.alessandro.astages.store.server.AManager;
import net.minecraft.resources.ResourceLocation;

@NotNullParams
public class ADimensionManager extends AManager<ADimensionRestriction, ResourceLocation, ResourceLocation> {
    public final OrderedMultiMap<ResourceLocation, ADimensionRestriction> CACHE = OrderedMultiMap.create();

    @Override
    public void reloadBeforeScripts() {
        super.reloadBeforeScripts();
        CACHE.clear();
    }

    @Override
    public void addRestriction(ADimensionRestriction restriction) {
        super.addRestriction(restriction);

        for (var dimension : restriction.getDimensions()) {
            CACHE.put(dimension, restriction);
        }
    }

    @Override
    public ADimensionRestriction getRestriction(AHolder holder, ResourceLocation dimension) {
        if (holder.isServerActive()) {
            var serverRestriction = ARestrictionUtils.getRestrictionFromCache(holder, AStageType.SERVER, CACHE, dimension);
            if (serverRestriction == null) { return null; }
        }

        if (holder.isPlayerActive()) {
            return ARestrictionUtils.getRestrictionFromCache(holder, AStageType.PLAYER, CACHE, dimension);
        }

        return null;
    }

    @Override
    public void removeRestriction(String id) {
        super.removeRestriction(id);
        CACHE.removeValues(restriction -> restriction.getId().equals(id));
    }

    @Override
    public ARestrictionType associatedType() {
        return ARestrictionTypes.DIMENSION;
    }
}
