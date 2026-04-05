package com.alessandro.astages.core.server.restriction;

import com.alessandro.astages.api.feature.AChangeable;
import com.alessandro.astages.api.feature.AMarkable;
import com.alessandro.astages.api.nullability.NotNullParamsAndMethodsReturn;
import com.alessandro.astages.core.ARestrictionManager;
import com.alessandro.astages.core.wrapper.OreWrapper;
import com.alessandro.astages.networking.ANetworking;
import com.alessandro.astages.networking.packet.ore.OreSyncerS2CPacket;
import com.alessandro.astages.networking.packet.reload.RequestReloadS2CPacket;
import com.alessandro.astages.store.Attribute;
import com.alessandro.astages.store.AttributeStore;
import com.alessandro.astages.store.Attributes;
import com.alessandro.astages.store.server.ARestriction;
import com.alessandro.astages.util.ReloadType;
import net.minecraft.world.level.block.state.BlockState;

@NotNullParamsAndMethodsReturn
public class AOreRestriction extends ARestriction<AOreRestriction, OreWrapper, BlockState> implements AChangeable, AMarkable {
    private BlockState original;
    private BlockState replacement;

    public AOreRestriction(String id, String stage) {
        super(id, stage);
    }

    @SuppressWarnings("unused")
    public static AOreRestriction newBuilder() {
        return new AOreRestriction("null", "null");
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
        if (toReturn.isConfig()) { return toReturn; }

        if (attribute == Attributes.AFFECTS_PLAYER_ACTIONS || attribute == Attributes.STAGE_ALL_BLOCK_STATES) {
            setChanged();
        }

        return toReturn;
    }

    @Override
    public void setChanged() {
        ARestrictionManager.ORE_INSTANCE.recalculatePlayerActions(this);
    }

    @Override
    public void markAsDirty() {
        ANetworking.sendToAllPlayers(new OreSyncerS2CPacket(this));
        ANetworking.sendToAllPlayers(new RequestReloadS2CPacket(ReloadType.ORE));
    }

    @SuppressWarnings("unused")
    public AOreRestriction setStageAllBlockStates(boolean value) {
        set(Attributes.STAGE_ALL_BLOCK_STATES, value);
        return this;
    }

    @SuppressWarnings("unused")
    public AOreRestriction setAffectsPlayerActions(boolean value) {
        set(Attributes.AFFECTS_PLAYER_ACTIONS, value);
        return this;
    }
}
