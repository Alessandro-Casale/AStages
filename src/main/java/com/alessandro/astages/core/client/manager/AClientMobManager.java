package com.alessandro.astages.core.client.manager;

import com.alessandro.astages.api.ARestrictionClientUtils;
import com.alessandro.astages.api.holder.AClientHolder;
import com.alessandro.astages.core.client.restriction.AClientMobRestriction;
import com.alessandro.astages.store.ARestrictionType;
import com.alessandro.astages.store.ARestrictionTypes;
import com.alessandro.astages.store.client.AClientManager;
import com.alessandro.astages.api.base.OrderedMultiMap;
import com.alessandro.astages.api.nullability.NotNullParams;
import net.minecraft.world.entity.EntityType;

@NotNullParams
public class AClientMobManager extends AClientManager<AClientMobRestriction, EntityType<?>, EntityType<?>> {
    private final OrderedMultiMap<EntityType<?>, AClientMobRestriction> CACHE = OrderedMultiMap.create();

    @Override
    public void reloadBeforeScripts() {
        super.reloadBeforeScripts();
        CACHE.clear();
    }

    @Override
    public void addRestriction(AClientMobRestriction restriction) {
        super.addRestriction(restriction);

        for (var type : restriction.getMobs()) {
            CACHE.put(type, restriction);
        }
    }

    @Override
    public AClientMobRestriction getRestriction(AClientHolder holder, EntityType<?> type) {
        return ARestrictionClientUtils.getRestrictionFromCache(holder, CACHE, type);
    }

    @Override
    public void removeRestriction(String id) {
        super.removeRestriction(id);
        CACHE.removeValues(restriction -> restriction.getId().equals(id));
    }

    @Override
    public ARestrictionType associatedType() {
        return ARestrictionTypes.MOB;
    }
}
