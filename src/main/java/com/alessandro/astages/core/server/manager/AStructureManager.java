package com.alessandro.astages.core.server.manager;

import com.alessandro.astages.api.ARestrictionUtils;
import com.alessandro.astages.api.base.OrderedMultiMap;
import com.alessandro.astages.api.constant.AStageType;
import com.alessandro.astages.api.holder.AHolder;
import com.alessandro.astages.api.nullability.NotNullParams;
import com.alessandro.astages.core.server.restriction.AStructureRestriction;
import com.alessandro.astages.event.structure.ServerEventHandler;
import com.alessandro.astages.store.ARestrictionType;
import com.alessandro.astages.store.ARestrictionTypes;
import com.alessandro.astages.store.server.AManager;
import net.minecraft.resources.ResourceLocation;

@NotNullParams
public class AStructureManager extends AManager<AStructureRestriction, ResourceLocation, ResourceLocation> {
    public final OrderedMultiMap<ResourceLocation, AStructureRestriction> CACHE = OrderedMultiMap.create();

    @Override
    public void reloadBeforeScripts() {
        super.reloadBeforeScripts();
        ServerEventHandler.playerIsInStructure.clear();
        CACHE.clear();
    }

    @Override
    public void addRestriction(AStructureRestriction restriction) {
        super.addRestriction(restriction);

        for (var structure : restriction.getStructures()) {
            CACHE.put(structure, restriction);
        }
    }

    @Override
    public AStructureRestriction getRestriction(AHolder holder, ResourceLocation structure) {
        if (holder.isServerActive()) {
            var serverRestriction = ARestrictionUtils.getRestrictionFromCache(holder, AStageType.SERVER, CACHE, structure);
            if (serverRestriction == null) { return null; }
        }

        if (holder.isPlayerActive()) {
            return ARestrictionUtils.getRestrictionFromCache(holder, AStageType.PLAYER, CACHE, structure);
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
        return ARestrictionTypes.STRUCTURE;
    }
}
