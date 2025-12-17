package com.alessandro.astages.core.server.manager;

import com.alessandro.astages.api.ARestrictionUtils;
import com.alessandro.astages.api.base.OrderedMultiMap;
import com.alessandro.astages.api.holder.AHolder;
import com.alessandro.astages.api.nullability.NotNullParams;
import com.alessandro.astages.api.nullability.Nullable;
import com.alessandro.astages.core.ARestrictionManager;
import com.alessandro.astages.core.server.restriction.AOreRestriction;
import com.alessandro.astages.core.wrapper.OreWrapper;
import com.alessandro.astages.networking.ANetworking;
import com.alessandro.astages.networking.packet.ore.OreSyncerS2CPacket;
import com.alessandro.astages.networking.packet.reload.RequestReloadS2CPacket;
import com.alessandro.astages.networking.packet.reload.RequestRestrictionDeleteS2CPacket;
import com.alessandro.astages.store.ARestrictionType;
import com.alessandro.astages.store.ARestrictionTypes;
import com.alessandro.astages.store.Attributes;
import com.alessandro.astages.api.feature.ClientSynchronizable;
import com.alessandro.astages.store.server.AManager;
import com.alessandro.astages.util.ReloadType;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;

@NotNullParams
public class AOreManager extends AManager<AOreRestriction, OreWrapper, BlockState> implements ClientSynchronizable {
    private final OrderedMultiMap<BlockState, AOreRestriction> CACHE = OrderedMultiMap.create();
    private final OrderedMultiMap<Block, AOreRestriction> BLOCK_CACHE = OrderedMultiMap.create();
    public final OrderedMultiMap<BlockState, AOreRestriction> AFFECTS_PLAYER_CACHE = OrderedMultiMap.create();

    @Override
    public void reloadBeforeScripts() {
        super.reloadBeforeScripts();
        CACHE.clear();
        BLOCK_CACHE.clear();
        AFFECTS_PLAYER_CACHE.clear();
    }

    @Override
    public void addRestriction(AOreRestriction restriction) {
        super.addRestriction(restriction);

        ARestrictionManager.ORE_STAGES.add(restriction.getStage());
        CACHE.put(restriction.getOriginal(), restriction);

        if (restriction.isEnabled(Attributes.STAGE_ALL_BLOCK_STATES)) {
            BLOCK_CACHE.put(restriction.getOriginal().getBlock(), restriction);
        }
    }

    @Override
    public AOreRestriction getRestriction(AHolder holder, BlockState state) {
        var cacheRestriction = ARestrictionUtils.getRestrictionFromCache(holder, CACHE, state);
        if (cacheRestriction != null) { return cacheRestriction; }

        return ARestrictionUtils.getRestrictionFromCache(holder, BLOCK_CACHE, state.getBlock());
    }

    public BlockState getReplacement(AHolder holder, BlockState original) {
        var restriction = ARestrictionManager.ORE_INSTANCE.getRestriction(holder, original);

        return restriction != null ? restriction.getReplacement() : original;
    }

    public BlockState getReplacementForPlayerActions(AHolder holder, BlockState original) {
        var restriction = ARestrictionUtils.getRestrictionFromCache(holder, AFFECTS_PLAYER_CACHE, original);

        return restriction != null ? restriction.getReplacement() : original;
    }

    public void recalculatePlayerActions(AOreRestriction restriction) {
        BLOCK_CACHE.removeValues(r -> r.getId().equals(restriction.getId()));
        AFFECTS_PLAYER_CACHE.removeValues(r -> r.getId().equals(restriction.getId()));

        if (restriction.isEnabled(Attributes.STAGE_ALL_BLOCK_STATES)) {
            BLOCK_CACHE.put(restriction.getOriginal().getBlock(), restriction);
        }

        if (restriction.isEnabled(Attributes.AFFECTS_PLAYER_ACTIONS)) {
            AFFECTS_PLAYER_CACHE.put(restriction.getOriginal(), restriction);
        }
    }

    @Override
    public void removeRestriction(String id) {
        super.removeRestriction(id);
        CACHE.removeValues(restriction -> restriction.getId().equals(id));
        BLOCK_CACHE.removeValues(restriction -> restriction.getId().equals(id));
        AFFECTS_PLAYER_CACHE.removeValues(restriction -> restriction.getId().equals(id));

        ANetworking.sendTo(null, new RequestRestrictionDeleteS2CPacket(id, associatedType()));
    }

    @Override
    public void synchronizeWithClient(@Nullable ServerPlayer player) {
        getRestrictions().forEach(restriction -> ANetworking.sendTo(player, new OreSyncerS2CPacket(restriction)));
        ANetworking.sendTo(player, new RequestReloadS2CPacket(ReloadType.ORE));
    }

    @Override
    public ARestrictionType associatedType() {
        return ARestrictionTypes.ORE;
    }
}
