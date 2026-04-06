package com.alessandro.astages.engine.client.manager;

import com.alessandro.astages.api.cache.client.BlockClientCache;
import com.alessandro.astages.api.cache.client.BlockStateClientCache;
import com.alessandro.astages.api.cache.client.StringClientCache;
import com.alessandro.astages.api.develop.NotYetImplemented;
import com.alessandro.astages.api.holder.AClientHolder;
import com.alessandro.astages.api.manager.AClientManager;
import com.alessandro.astages.api.nullability.NotNullParams;
import com.alessandro.astages.api.store.ARestrictionType;
import com.alessandro.astages.api.wrapper.OreWrapper;
import com.alessandro.astages.engine.client.ClientMiscStorage;
import com.alessandro.astages.engine.client.restriction.AClientOreRestriction;
import com.alessandro.astages.engine.store.ARestrictionTypes;
import com.alessandro.astages.engine.store.Attributes;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.state.BlockState;

@NotNullParams
public class AClientOreManager extends AClientManager<AClientOreRestriction, OreWrapper, BlockState> {
    private final BlockStateClientCache<AClientOreRestriction> blockStateCache = new BlockStateClientCache<>() {
        @Override
        public void index(AClientOreRestriction restriction) {
            add(restriction.getOriginal(), restriction);
        }
    };

    private final BlockClientCache<AClientOreRestriction> blockCache = new BlockClientCache<>() {
        @Override
        public void index(AClientOreRestriction restriction) {
            if (restriction.isEnabled(Attributes.STAGE_ALL_BLOCK_STATES)) {
                add(restriction.getOriginal().getBlock(), restriction);
            }
        }
    };

    @NotYetImplemented
    private final StringClientCache<AClientOreRestriction> restrictionsByStageCache = new StringClientCache<>() {
        @Override
        public void index(AClientOreRestriction restriction) {
            add(restriction.getStage(), restriction);
        }
    };

    public AClientOreManager() {
        registerCaches(blockStateCache, blockCache, restrictionsByStageCache);
    }

    @Override
    public void addRestriction(AClientOreRestriction restriction) {
        super.addRestriction(restriction);
        ClientMiscStorage.ORE_STAGES.add(restriction.getStage());
    }

    @Override
    public AClientOreRestriction getRestriction(AClientHolder holder, BlockState state) {
        var cacheRestriction = blockStateCache.find(holder, state);
        if (cacheRestriction != null) { return cacheRestriction; }

        return blockCache.find(holder, state.getBlock());
    }

    public AClientOreRestriction getRestriction(AClientHolder holder, BlockItem item) {
        var cacheRestriction = blockStateCache.find(holder, item.getBlock().defaultBlockState());
        if (cacheRestriction != null) { return cacheRestriction; }

        return blockCache.find(holder, item.getBlock());
    }

    public BlockState getReplacement(AClientHolder holder, BlockState original) {
        var restriction = getRestriction(holder, original);

        return restriction != null ? restriction.getReplacement() : original;
    }

    public Item getReplacement(AClientHolder holder, BlockItem item) {
        var restriction = getRestriction(holder, item);

        return restriction != null ? restriction.getReplacement().getBlock().asItem() : item;
    }

    @Override
    public ARestrictionType associatedType() {
        return ARestrictionTypes.ORE;
    }
}