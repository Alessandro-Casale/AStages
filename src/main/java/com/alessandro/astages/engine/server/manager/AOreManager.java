package com.alessandro.astages.engine.server.manager;

import com.alessandro.astages.api.cache.BlockCache;
import com.alessandro.astages.api.cache.BlockStateCache;
import com.alessandro.astages.api.feature.ClientSynchronizable;
import com.alessandro.astages.api.manager.AManager;
import com.alessandro.astages.api.nullability.NotNullParams;
import com.alessandro.astages.api.nullability.Nullable;
import com.alessandro.astages.api.stage.AStageContext;
import com.alessandro.astages.api.store.ARestrictionType;
import com.alessandro.astages.api.util.AEvaluatorUtils;
import com.alessandro.astages.api.wrapper.OreWrapper;
import com.alessandro.astages.engine.server.MiscStorage;
import com.alessandro.astages.engine.server.restriction.AOreRestriction;
import com.alessandro.astages.engine.store.ARestrictionTypes;
import com.alessandro.astages.engine.store.Attributes;
import com.alessandro.astages.infrastructure.networking.Networking;
import com.alessandro.astages.infrastructure.networking.packet.ore.SyncOreS2C;
import com.alessandro.astages.infrastructure.networking.packet.reload.RequestRestrictionDeleteS2C;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.block.state.BlockState;

@NotNullParams
public class AOreManager extends AManager<AOreRestriction, OreWrapper, BlockState> implements ClientSynchronizable {
    private final BlockStateCache<AOreRestriction> blockStateCache = new BlockStateCache<>(this) {
        @Override
        public void index(AOreRestriction restriction) {
            add(restriction.getOriginal(), restriction);
        }
    };

    private final BlockCache<AOreRestriction> blockCache = new BlockCache<>(this) {
        @Override
        public void index(AOreRestriction restriction) {
            if (restriction.isEnabled(Attributes.MATCH_ALL_BLOCK_STATES)) {
                add(restriction.getOriginal().getBlock(), restriction);
            }
        }
    };

    private final BlockStateCache<AOreRestriction> affectsPlayerCache = new BlockStateCache<>(this) {
        @Override
        public void index(AOreRestriction restriction) { }
    };

    public AOreManager() {
        registerCaches(blockStateCache, blockCache, affectsPlayerCache);
    }

    @Override
    public boolean addRestriction(AOreRestriction restriction) {
        var correctlyRegistered = super.addRestriction(restriction);

        if (correctlyRegistered) {
            MiscStorage.ORE_STAGES.add(restriction.getStage());
        }

        return correctlyRegistered;
    }

    @Override
    public AOreRestriction getRestriction(AStageContext context, BlockState state) {
        return AEvaluatorUtils.evaluateRestrictionFromMultipleSources(
            AEvaluatorUtils.calculateEvaluationMode(getRegistry()), context,
            r -> r.isRestricted(state),
            () -> blockStateCache.get(state),
            () -> blockCache.get(state.getBlock())
        );
    }

    public BlockState getReplacement(AStageContext context, BlockState original) {
        var restriction = getRestriction(context, original);
        return restriction != null ? restriction.getReplacement() : original;
    }

    public BlockState getReplacementForPlayerActions(AStageContext context, BlockState original) {
        var restriction = affectsPlayerCache.find(context, original);
        return restriction != null ? restriction.getReplacement() : original;
    }

    @Override
    public void removeRestriction(String id) {
        super.removeRestriction(id);
        Networking.sendTo(null, new RequestRestrictionDeleteS2C(id, associatedType()));
    }

    @Override
    public void synchronizeWithClient(@Nullable ServerPlayer player) {
        getRegistry()
            .forEach(restriction -> Networking.sendTo(player, new SyncOreS2C(restriction)));
    }

    public void recalculateCaches(AOreRestriction restriction) {
        blockCache.remove(restriction);
        affectsPlayerCache.remove(restriction);

        if (restriction.isEnabled(Attributes.MATCH_ALL_BLOCK_STATES)) {
            blockCache.add(restriction.getOriginal().getBlock(), restriction);
        }

        if (restriction.isEnabled(Attributes.AFFECTS_PLAYER_ACTIONS)) {
            affectsPlayerCache.add(restriction.getOriginal(), restriction);
        }
    }

    @Override
    public ARestrictionType associatedType() {
        return ARestrictionTypes.ORE;
    }
}