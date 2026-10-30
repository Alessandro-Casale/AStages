package com.alessandro.astages.engine.client.manager;

import com.alessandro.astages.api.cache.ResourceKeyCache;
import com.alessandro.astages.api.develop.UnderDevelopment;
import com.alessandro.astages.api.manager.AClientManager;
import com.alessandro.astages.api.nullability.NotNullParams;
import com.alessandro.astages.api.stage.AStageContext;
import com.alessandro.astages.api.store.ARestrictionType;
import com.alessandro.astages.engine.AClientStructureCollisionManager;
import com.alessandro.astages.engine.client.restriction.AClientStructureRestriction;
import com.alessandro.astages.engine.store.ARestrictionTypes;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.levelgen.structure.Structure;

@NotNullParams
public class AClientStructureManager extends AClientManager<AClientStructureRestriction, ResourceKey<Structure>, ResourceKey<Structure>> {
    private final ResourceKeyCache<AClientStructureRestriction, Structure> structureCache = new ResourceKeyCache<>(this) {
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
    public AClientStructureRestriction getRestriction(ResourceKey<Structure> structure) {
        var cache = structureCache.get(structure);
        return cache.isEmpty() ? null : cache.first();
    }

    @Override
    public AClientStructureRestriction getRestriction(AStageContext context, ResourceKey<Structure> structure) {
        return structureCache.find(context, structure);
    }

    @Override
    public ARestrictionType associatedType() {
        return ARestrictionTypes.STRUCTURE;
    }
}