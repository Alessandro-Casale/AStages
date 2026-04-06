package com.alessandro.astages.engine.client.manager;

import com.alessandro.astages.api.cache.client.EntityTypeClientCache;
import com.alessandro.astages.api.holder.AClientHolder;
import com.alessandro.astages.api.manager.AClientManager;
import com.alessandro.astages.api.nullability.NotNullParams;
import com.alessandro.astages.api.store.ARestrictionType;
import com.alessandro.astages.engine.client.restriction.AClientMobRestriction;
import com.alessandro.astages.engine.store.ARestrictionTypes;
import net.minecraft.world.entity.EntityType;

@NotNullParams
public class AClientMobManager extends AClientManager<AClientMobRestriction, EntityType<?>, EntityType<?>> {
    private final EntityTypeClientCache<AClientMobRestriction> mobCache = new EntityTypeClientCache<>() {
        @Override
        public void index(AClientMobRestriction restriction) {
            for (var mob : restriction.getMobs()) {
                add(mob, restriction);
            }
        }
    };

    public AClientMobManager() {
        registerCaches(mobCache);
    }

    @Override
    public AClientMobRestriction getRestriction(AClientHolder holder, EntityType<?> type) {
        return mobCache.find(holder, type);
    }

    @Override
    public ARestrictionType associatedType() {
        return ARestrictionTypes.MOB;
    }
}