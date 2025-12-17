package com.alessandro.astages.core.client.restriction;

import com.alessandro.astages.core.wrapper.OreWrapper;
import com.alessandro.astages.store.AttributeStore;
import com.alessandro.astages.store.Attributes;
import com.alessandro.astages.store.client.AClientRestriction;
import com.alessandro.astages.api.nullability.NotNullParamsAndMethodsReturn;
import net.minecraft.world.level.block.state.BlockState;

@NotNullParamsAndMethodsReturn
public class AClientOreRestriction extends AClientRestriction<AClientOreRestriction, OreWrapper, BlockState> {
    private BlockState original;
    private BlockState replacement;

    public AClientOreRestriction(String id, String stage) {
        super(id, stage);
    }

    @Override
    public AttributeStore allowedAttributes() {
        return AttributeStore.builder()
            .addAttribute(Attributes.STAGE_ALL_BLOCK_STATES);
    }

    @Override
    public AClientOreRestriction restrict(OreWrapper wrapper) {
        this.original = wrapper.original();
        this.replacement = wrapper.replacement();

        return this;
    }

    public boolean isRestricted(BlockState original) {
        if (isEnabled(Attributes.STAGE_ALL_BLOCK_STATES)) {
            return this.original.is(original.getBlock());
        }

        return this.original.equals(original);
    }

    public BlockState getOriginal() {
        return original;
    }

    public BlockState getReplacement() {
        return replacement;
    }
}
