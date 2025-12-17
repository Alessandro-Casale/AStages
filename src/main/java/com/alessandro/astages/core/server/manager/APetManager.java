package com.alessandro.astages.core.server.manager;

import com.alessandro.astages.api.ARestrictionUtils;
import com.alessandro.astages.api.base.OrderedMultiMap;
import com.alessandro.astages.api.constant.AStageType;
import com.alessandro.astages.api.holder.AHolder;
import com.alessandro.astages.api.nullability.NotNullParams;
import com.alessandro.astages.core.server.restriction.APetRestriction;
import com.alessandro.astages.store.ARestrictionType;
import com.alessandro.astages.store.ARestrictionTypes;
import com.alessandro.astages.store.server.AManager;
import net.minecraft.world.entity.EntityType;

@NotNullParams
public class APetManager extends AManager<APetRestriction, EntityType<?>, EntityType<?>> {
    public final OrderedMultiMap<EntityType<?>, APetRestriction> CACHE = OrderedMultiMap.create();

    @Override
    public void reloadBeforeScripts() {
        super.reloadBeforeScripts();
        CACHE.clear();
    }

    @Override
    public void addRestriction(APetRestriction restriction) {
        super.addRestriction(restriction);

        for (var type : restriction.getPets()) {
            CACHE.put(type, restriction);
        }
    }

    @Override
    public APetRestriction getRestriction(AHolder holder, EntityType<?> type) {
        if (holder.isServerActive()) {
            var serverRestriction = ARestrictionUtils.getRestrictionFromCache(holder, AStageType.SERVER, CACHE, type);
            if (serverRestriction == null) { return null; }
        }

        if (holder.isPlayerActive()) {
            return ARestrictionUtils.getRestrictionFromCache(holder, AStageType.PLAYER, CACHE, type);
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
        return ARestrictionTypes.PET;
    }
}
