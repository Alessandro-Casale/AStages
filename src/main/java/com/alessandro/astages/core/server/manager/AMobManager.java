package com.alessandro.astages.core.server.manager;

import com.alessandro.astages.api.ARestrictionUtils;
import com.alessandro.astages.api.base.OrderedMultiMap;
import com.alessandro.astages.api.constant.AStageType;
import com.alessandro.astages.api.holder.AHolder;
import com.alessandro.astages.api.nullability.NotNullParams;
import com.alessandro.astages.api.nullability.Nullable;
import com.alessandro.astages.core.server.restriction.AMobRestriction;
import com.alessandro.astages.networking.ANetworking;
import com.alessandro.astages.networking.packet.mob.MobSyncerS2CPacket;
import com.alessandro.astages.networking.packet.reload.RequestRestrictionDeleteS2CPacket;
import com.alessandro.astages.api.feature.ClientSynchronizable;
import com.alessandro.astages.store.ARestrictionType;
import com.alessandro.astages.store.ARestrictionTypes;
import com.alessandro.astages.store.server.AManager;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.EntityType;

@NotNullParams
public class AMobManager extends AManager<AMobRestriction, EntityType<?>, EntityType<?>> implements ClientSynchronizable {
    private final OrderedMultiMap<EntityType<?>, AMobRestriction> CACHE = OrderedMultiMap.create();

    @Override
    public void reloadBeforeScripts() {
        super.reloadBeforeScripts();
        CACHE.clear();
    }

    @Override
    public void addRestriction(AMobRestriction restriction) {
        super.addRestriction(restriction);

        for (var type : restriction.getMobs()) {
            CACHE.put(type, restriction);
        }
    }

    @Override
    public AMobRestriction getRestriction(AHolder holder, EntityType<?> type) {
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

        ANetworking.sendTo(null, new RequestRestrictionDeleteS2CPacket(id, associatedType()));
    }

    @Override
    public void synchronizeWithClient(@Nullable ServerPlayer player) {
        getRestrictions().forEach(restriction -> ANetworking.sendTo(player, new MobSyncerS2CPacket(restriction)));
    }

    @Override
    public ARestrictionType associatedType() {
        return ARestrictionTypes.MOB;
    }
}
