package com.alessandro.astages.engine.server.manager;

import com.alessandro.astages.api.cache.server.ResourceLocationCache;
import com.alessandro.astages.api.develop.UnderDevelopment;
import com.alessandro.astages.api.holder.AHolder;
import com.alessandro.astages.api.manager.AManager;
import com.alessandro.astages.api.nullability.NotNullParams;
import com.alessandro.astages.api.store.ARestrictionType;
import com.alessandro.astages.engine.collision.StructureCollisionManager;
import com.alessandro.astages.engine.server.restriction.AStructureRestriction;
import com.alessandro.astages.engine.store.ARestrictionTypes;
import net.minecraft.resources.ResourceLocation;

@NotNullParams
public class AStructureManager extends AManager<AStructureRestriction, ResourceLocation, ResourceLocation> {
    public final ResourceLocationCache<AStructureRestriction> structureCache = new ResourceLocationCache<>() {
        @Override
        public void index(AStructureRestriction restriction) {
            for (var structure : restriction.getStructures()) {
                add(structure, restriction);
            }
        }
    };

    public AStructureManager() {
        registerCaches(structureCache);
    }

    @Override
    public void onReloadStarted() {
        super.onReloadStarted();
        StructureCollisionManager.onReloadStarted();
    }

    @UnderDevelopment("Check correct priority!")
    public AStructureRestriction getRestriction(ResourceLocation structure) {
        var cache = structureCache.get(structure);
        return cache.isEmpty() ? null : cache.first();
    }

    @Override
    public AStructureRestriction getRestriction(AHolder holder, ResourceLocation structure) {
        return structureCache.find(holder, structure);
    }

    @Override
    public ARestrictionType associatedType() {
        return ARestrictionTypes.STRUCTURE;
    }
}