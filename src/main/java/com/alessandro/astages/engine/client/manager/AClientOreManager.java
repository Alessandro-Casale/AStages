package com.alessandro.astages.engine.client.manager;

import com.alessandro.astages.api.cache.BlockCache;
import com.alessandro.astages.api.cache.BlockStateCache;
import com.alessandro.astages.api.cache.StringCache;
import com.alessandro.astages.api.develop.NotYetImplemented;
import com.alessandro.astages.api.manager.AClientManager;
import com.alessandro.astages.api.nullability.NotNullParams;
import com.alessandro.astages.api.stage.AStageContext;
import com.alessandro.astages.api.store.ARestrictionType;
import com.alessandro.astages.api.wrapper.OreWrapper;
import com.alessandro.astages.engine.client.ClientMiscStorage;
import com.alessandro.astages.engine.client.restriction.AClientOreRestriction;
import com.alessandro.astages.engine.store.ARestrictionTypes;
import com.alessandro.astages.engine.store.Attributes;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.BlockState;

@NotNullParams
public class AClientOreManager extends AClientManager<AClientOreRestriction, OreWrapper, BlockState> {
    private final BlockStateCache<AClientOreRestriction> blockStateCache = new BlockStateCache<>(this) {
        @Override
        public void index(AClientOreRestriction restriction) {
            add(restriction.getOriginal(), restriction);
        }
    };

    private final BlockCache<AClientOreRestriction> blockCache = new BlockCache<>(this) {
        @Override
        public void index(AClientOreRestriction restriction) {
            if (restriction.isEnabled(Attributes.MATCH_ALL_BLOCK_STATES)) {
                add(restriction.getOriginal().getBlock(), restriction);
            }
        }
    };

    @NotYetImplemented
    private final StringCache<AClientOreRestriction> restrictionsByStageCache = new StringCache<>(this) {
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
    public AClientOreRestriction getRestriction(AStageContext context, BlockState state) {
        var cacheRestriction = blockStateCache.find(context, state);
        if (cacheRestriction != null) { return cacheRestriction; }

        return blockCache.find(context, state.getBlock());
    }

    public AClientOreRestriction getRestriction(AStageContext context, BlockItem item) {
        var cacheRestriction = blockStateCache.find(context, item.getBlock().defaultBlockState());
        if (cacheRestriction != null) { return cacheRestriction; }

        return blockCache.find(context, item.getBlock());
    }

    public BlockState getReplacement(AStageContext context, BlockState original) {
        var restriction = getRestriction(context, original);

        return restriction != null ? restriction.getReplacement() : original;
    }

    public Item getReplacement(AStageContext context, BlockItem item) {
        var restriction = getRestriction(context, item);

        return restriction != null ? restriction.getReplacement().getBlock().asItem() : item;
    }

    public ItemStack getReplacement(AStageContext context, ItemStack original, BlockItem check) {
        var restriction = getRestriction(context, check.getBlock().defaultBlockState());

        return restriction != null ? restriction.getReplacement().getBlock().asItem().getDefaultInstance() : original;
    }

    @Override
    public ARestrictionType associatedType() {
        return ARestrictionTypes.ORE;
    }
}