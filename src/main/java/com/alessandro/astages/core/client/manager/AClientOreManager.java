package com.alessandro.astages.core.client.manager;

import com.alessandro.astages.api.ARestrictionClientUtils;
import com.alessandro.astages.api.holder.AClientHolder;
import com.alessandro.astages.core.AClientRestrictionManager;
import com.alessandro.astages.core.client.restriction.AClientOreRestriction;
import com.alessandro.astages.core.wrapper.OreWrapper;
import com.alessandro.astages.store.ARestrictionType;
import com.alessandro.astages.store.ARestrictionTypes;
import com.alessandro.astages.store.Attributes;
import com.alessandro.astages.store.client.AClientManager;
import com.alessandro.astages.api.base.OrderedMultiMap;
import com.alessandro.astages.api.nullability.NotNullParams;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;

@NotNullParams
public class AClientOreManager extends AClientManager<AClientOreRestriction, OreWrapper, BlockState> {
    private final OrderedMultiMap<BlockState, AClientOreRestriction> CACHE = OrderedMultiMap.create();
    private final OrderedMultiMap<Block, AClientOreRestriction> BLOCK_CACHE = OrderedMultiMap.create();
    private final OrderedMultiMap<String, AClientOreRestriction> RESTRICTIONS_BY_STAGE = OrderedMultiMap.create();

    public OrderedMultiMap<String, AClientOreRestriction> getRestrictionsByStage() {
        return RESTRICTIONS_BY_STAGE;
    }

    @Override
    public void reloadBeforeScripts() {
        super.reloadBeforeScripts();
        CACHE.clear();
        BLOCK_CACHE.clear();
        RESTRICTIONS_BY_STAGE.clear();
    }

    @Override
    public void addRestriction(AClientOreRestriction restriction) {
        super.addRestriction(restriction);

        AClientRestrictionManager.ORE_STAGES.add(restriction.getStage());
        CACHE.put(restriction.getOriginal(), restriction);
        RESTRICTIONS_BY_STAGE.put(restriction.getStage(), restriction);

        if (restriction.isEnabled(Attributes.STAGE_ALL_BLOCK_STATES)) {
            BLOCK_CACHE.put(restriction.getOriginal().getBlock(), restriction);
        }
    }

    @Override
    public AClientOreRestriction getRestriction(AClientHolder holder, BlockState state) {
        var cacheRestriction = ARestrictionClientUtils.getRestrictionFromCache(holder, CACHE, state);
        if (cacheRestriction != null) { return cacheRestriction; }

        return ARestrictionClientUtils.getRestrictionFromCache(holder, BLOCK_CACHE, state.getBlock());
    }

    public AClientOreRestriction getRestriction(AClientHolder holder, BlockItem item) {
        var cacheRestriction = ARestrictionClientUtils.getRestrictionFromCache(holder, CACHE, item.getBlock().defaultBlockState());
        if (cacheRestriction != null) { return cacheRestriction; }

        return ARestrictionClientUtils.getRestrictionFromCache(holder, BLOCK_CACHE, item.getBlock());
    }

    public BlockState getReplacement(AClientHolder holder, BlockState original) {
        var restriction = AClientRestrictionManager.ORE_INSTANCE.getRestriction(holder, original);

        return restriction != null ? restriction.getReplacement() : original;
    }

    public Item getReplacement(AClientHolder holder, BlockItem item) {
        var restriction = AClientRestrictionManager.ORE_INSTANCE.getRestriction(holder, item);

        return restriction != null ? restriction.getReplacement().getBlock().asItem() : item;
    }

    @Override
    public void removeRestriction(String id) {
        super.removeRestriction(id);
        CACHE.removeValues(restriction -> restriction.getId().equals(id));
        BLOCK_CACHE.removeValues(restriction -> restriction.getId().equals(id));
        RESTRICTIONS_BY_STAGE.removeValues(restriction -> restriction.getId().equals(id));
    }

    @Override
    public ARestrictionType associatedType() {
        return ARestrictionTypes.ORE;
    }
}
