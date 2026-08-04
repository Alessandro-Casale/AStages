package com.alessandro.astages.engine.client.restriction;

import com.alessandro.astages.api.nullability.NotNullParamsAndMethodsReturn;
import com.alessandro.astages.api.restriction.AClientRestriction;
import com.alessandro.astages.api.store.container.AttributeStore;
import com.alessandro.astages.api.wrapper.OreWrapper;
import com.alessandro.astages.engine.AClientRestrictionManager;
import com.alessandro.astages.engine.store.Attributes;
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
        var defaultAttributes = AttributeStore.builder()
            .addAttribute(Attributes.MATCH_ALL_BLOCK_STATES);

        return AttributeStore.compose()
            .withSuper(super.allowedAttributes())
            .withSelf(defaultAttributes)
            .withPlugin(AClientRestrictionManager.ATTACHED_ATTRIBUTES, AClientOreRestriction.class)
            .build();
    }

    @Override
    public AClientOreRestriction restrict(OreWrapper wrapper) {
        this.original = wrapper.original();
        this.replacement = wrapper.replacement();

        return this;
    }

    @Override
    public boolean isRestricted(BlockState original) {
        if (isEnabled(Attributes.MATCH_ALL_BLOCK_STATES)) {
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
