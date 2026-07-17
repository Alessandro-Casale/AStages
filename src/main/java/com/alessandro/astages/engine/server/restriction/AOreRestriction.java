package com.alessandro.astages.engine.server.restriction;

import com.alessandro.astages.api.feature.AChangeable;
import com.alessandro.astages.api.feature.AMarkable;
import com.alessandro.astages.api.nullability.NotNullParamsAndMethodsReturn;
import com.alessandro.astages.api.reload.ClientReloadPhase;
import com.alessandro.astages.api.restriction.ARestriction;
import com.alessandro.astages.api.store.Attribute;
import com.alessandro.astages.api.store.container.AttributeStore;
import com.alessandro.astages.api.wrapper.OreWrapper;
import com.alessandro.astages.engine.ARestrictionManager;
import com.alessandro.astages.engine.store.Attributes;
import com.alessandro.astages.infrastructure.networking.Networking;
import com.alessandro.astages.infrastructure.networking.packet.ore.SyncOreS2C;
import com.alessandro.astages.infrastructure.networking.packet.reload.RequestReloadS2C;
import net.minecraft.world.level.block.state.BlockState;

@NotNullParamsAndMethodsReturn
public class AOreRestriction extends ARestriction<AOreRestriction, OreWrapper, BlockState> implements AChangeable, AMarkable {
    private BlockState original;
    private BlockState replacement;

    public AOreRestriction(String id, String stage) {
        super(id, stage);
    }

    @Override
    public AttributeStore allowedAttributes() {
        var defaultAttributes = AttributeStore.builder()
            .addAttribute(Attributes.AFFECTS_PLAYER_ACTIONS)
            .addAttribute(Attributes.STAGE_ALL_BLOCK_STATES);

        return AttributeStore.compose()
            .withSuper(super.allowedAttributes())
            .withSelf(defaultAttributes)
            .withPlugin(ARestrictionManager.ATTACHED_ATTRIBUTES, AOreRestriction.class)
            .build();
    }

    @Override
    public AOreRestriction restrict(OreWrapper wrapper) {
        this.original = wrapper.original();
        this.replacement = wrapper.replacement();

        return this;
    }

    @Override
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

    @Override
    public <T> AOreRestriction set(Attribute<T> attribute, T value) {
        var toReturn = super.set(attribute, value);

        if (attribute == Attributes.AFFECTS_PLAYER_ACTIONS || attribute == Attributes.STAGE_ALL_BLOCK_STATES) {
            setChanged();
        }

        return toReturn;
    }

    @Override
    public void setChanged() {
        ARestrictionManager.ORE_INSTANCE.recalculateCaches(this);
    }

    @Override
    public void markAsDirty() {
        Networking.sendToAllPlayers(new SyncOreS2C(this));
        Networking.sendToAllPlayers(new RequestReloadS2C(ClientReloadPhase.ORE_RESTRICTION_MARKED_AS_DIRTY));
    }

    public AOreRestriction matchAllBlockStates() {
        return set(Attributes.STAGE_ALL_BLOCK_STATES, true);
    }

    public AOreRestriction affectPlayerActions() {
        return set(Attributes.AFFECTS_PLAYER_ACTIONS, true);
    }

    @Deprecated(forRemoval = true, since = "3.0.0")
    public AOreRestriction setStageAllBlockStates(boolean value) {
        set(Attributes.STAGE_ALL_BLOCK_STATES, value);
        return this;
    }

    @Deprecated(forRemoval = true, since = "3.0.0")
    public AOreRestriction setAffectsPlayerActions(boolean value) {
        set(Attributes.AFFECTS_PLAYER_ACTIONS, value);
        return this;
    }
}