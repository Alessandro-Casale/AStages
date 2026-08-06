package com.alessandro.astages.engine.server.manager;

import com.alessandro.astages.api.cache.server.ResourceLocationCache;
import com.alessandro.astages.api.develop.UnderDevelopment;
import com.alessandro.astages.api.feature.ClientSynchronizable;
import com.alessandro.astages.api.holder.AHolder;
import com.alessandro.astages.api.manager.AManager;
import com.alessandro.astages.api.nullability.NotNullParams;
import com.alessandro.astages.api.store.ARestrictionType;
import com.alessandro.astages.engine.server.restriction.AStructureRestriction;
import com.alessandro.astages.engine.store.ARestrictionTypes;
import com.alessandro.astages.infrastructure.networking.Networking;
import com.alessandro.astages.infrastructure.networking.packet.reload.RequestRestrictionDeleteS2C;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;

@NotNullParams
public class AStructureManager extends AManager<AStructureRestriction, ResourceLocation, ResourceLocation> implements ClientSynchronizable {
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
        AStructureCollisionManager.onReloadStarted();
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
    public void removeRestriction(String id) {
        super.removeRestriction(id);
        Networking.sendTo(null, new RequestRestrictionDeleteS2C(id, associatedType()));
    }

    @Override
    public void synchronizeWithClient(ServerPlayer player) {
        getRegistry()
            .forEach(restriction -> Networking.sendTo(player, new SyncStructureS2C(restriction)));
    }

    @Override
    public ARestrictionType associatedType() {
        return ARestrictionTypes.STRUCTURE;
    }
}