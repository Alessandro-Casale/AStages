package com.alessandro.astages.engine.client.manager;

import com.alessandro.astages.api.cache.client.ResourceLocationClientCache;
import com.alessandro.astages.api.develop.UnderDevelopment;
import com.alessandro.astages.api.holder.AClientHolder;
import com.alessandro.astages.api.manager.AClientManager;
import com.alessandro.astages.api.nullability.NotNullParams;
import com.alessandro.astages.api.store.ARestrictionType;
import com.alessandro.astages.engine.client.restriction.AClientStructureRestriction;
import com.alessandro.astages.engine.AClientStructureCollisionManager;
import com.alessandro.astages.engine.store.ARestrictionTypes;
import net.minecraft.resources.ResourceLocation;

@NotNullParams
public class AClientStructureManager extends AClientManager<AClientStructureRestriction, ResourceLocation, ResourceLocation> {
    public final ResourceLocationClientCache<AClientStructureRestriction> structureCache = new ResourceLocationClientCache<>() {
        @Override
        public void index(AClientStructureRestriction restriction) {
            for (var structure : restriction.getStructures()) {
                add(structure, restriction);
            }
        }
    };

    public AClientStructureManager() {
        registerCaches(structureCache);
    }

    @Override
    public void onReloadStarted() {
        super.onReloadStarted();
        AClientStructureCollisionManager.onReloadStarted();
    }

    @UnderDevelopment("Check correct priority!")
    public AClientStructureRestriction getRestriction(ResourceLocation structure) {
        var cache = structureCache.get(structure);
        return cache.isEmpty() ? null : cache.first();
    }

    @Override
    public AClientStructureRestriction getRestriction(AClientHolder holder, ResourceLocation structure) {
        return structureCache.find(holder, structure);
    }

    @Override
    public ARestrictionType associatedType() {
        return ARestrictionTypes.STRUCTURE;
    }
}