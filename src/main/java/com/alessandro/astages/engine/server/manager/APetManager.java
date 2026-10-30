package com.alessandro.astages.engine.server.manager;

import com.alessandro.astages.api.cache.EntityTypeCache;
import com.alessandro.astages.api.manager.AManager;
import com.alessandro.astages.api.nullability.NotNullParams;
import com.alessandro.astages.api.stage.AStageContext;
import com.alessandro.astages.api.store.ARestrictionType;
import com.alessandro.astages.engine.server.restriction.APetRestriction;
import com.alessandro.astages.engine.store.ARestrictionTypes;
import net.minecraft.world.entity.EntityType;

@NotNullParams
public class APetManager extends AManager<APetRestriction, EntityType<?>, EntityType<?>> {
    private final EntityTypeCache<APetRestriction> petCache = new EntityTypeCache<>(this) {
        @Override
        public void index(APetRestriction restriction) {
            for (var pet : restriction.getPets()) {
                add(pet, restriction);
            }
        }
    };

    public APetManager() {
        registerCaches(petCache);
    }

    @Override
    public APetRestriction getRestriction(AStageContext context, EntityType<?> type) {
        return  petCache.find(context, type);
    }

    @Override
    public ARestrictionType associatedType() {
        return ARestrictionTypes.PET;
    }
}