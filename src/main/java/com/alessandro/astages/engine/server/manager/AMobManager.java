package com.alessandro.astages.engine.server.manager;

import com.alessandro.astages.api.cache.server.EntityTypeCache;
import com.alessandro.astages.api.feature.ClientSynchronizable;
import com.alessandro.astages.api.holder.AHolder;
import com.alessandro.astages.api.manager.AManager;
import com.alessandro.astages.api.nullability.NotNullParams;
import com.alessandro.astages.api.nullability.Nullable;
import com.alessandro.astages.api.store.ARestrictionType;
import com.alessandro.astages.engine.server.restriction.AMobRestriction;
import com.alessandro.astages.engine.store.ARestrictionTypes;
import com.alessandro.astages.infrastructure.networking.Networking;
import com.alessandro.astages.infrastructure.networking.packet.mob.SyncMobS2C;
import com.alessandro.astages.infrastructure.networking.packet.reload.RequestRestrictionDeleteS2C;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.EntityType;

@NotNullParams
public class AMobManager extends AManager<AMobRestriction, EntityType<?>, EntityType<?>> implements ClientSynchronizable {
    private final EntityTypeCache<AMobRestriction> mobCache = new EntityTypeCache<>() {
        @Override
        public void index(AMobRestriction restriction) {
            for (var mob : restriction.getMobs()) {
                add(mob, restriction);
            }
        }
    };

    public AMobManager() {
        registerCaches(mobCache);
    }

    @Override
    public AMobRestriction getRestriction(AHolder holder, EntityType<?> type) {
        return mobCache.find(holder, type);
    }

    @Override
    public void removeRestriction(String id) {
        super.removeRestriction(id);
        Networking.sendTo(null, new RequestRestrictionDeleteS2C(id, associatedType()));
    }

    @Override
    public void synchronizeWithClient(@Nullable ServerPlayer player) {
        getRegistry()
            .forEach(restriction -> Networking.sendTo(player, new SyncMobS2C(restriction)));
    }

    @Override
    public ARestrictionType associatedType() {
        return ARestrictionTypes.MOB;
    }
}