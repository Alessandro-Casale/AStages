package com.alessandro.astages.infrastructure.networking.packet.ore;

import com.alessandro.astages.api.AResourceLocation;
import com.alessandro.astages.api.nullability.NotNullParams;
import com.alessandro.astages.engine.AClientRestrictionManager;
import com.alessandro.astages.engine.client.restriction.AClientOreRestriction;
import com.alessandro.astages.engine.server.restriction.AOreRestriction;
import com.alessandro.astages.api.wrapper.OreWrapper;
import com.alessandro.astages.infrastructure.networking.AStagesPacket;
import com.alessandro.astages.engine.store.Attributes;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import org.jetbrains.annotations.NotNull;

@NotNullParams
public record SyncOreS2C(String id, String stage, BlockState original, BlockState replacement, boolean stageAllBlockStates) implements AStagesPacket {
    public static final Type<SyncOreS2C> TYPE = new Type<>(AResourceLocation.fromNamespaceAndPath("sync_ore_s2c"));

    public static final StreamCodec<RegistryFriendlyByteBuf, SyncOreS2C> STREAM_CODEC = StreamCodec.composite(
        ByteBufCodecs.STRING_UTF8, SyncOreS2C::id,
        ByteBufCodecs.STRING_UTF8, SyncOreS2C::stage,
        ByteBufCodecs.fromCodec(BlockState.CODEC), SyncOreS2C::original,
        ByteBufCodecs.fromCodec(BlockState.CODEC), SyncOreS2C::replacement,
        ByteBufCodecs.BOOL, SyncOreS2C::stageAllBlockStates,
        SyncOreS2C::new
    );

    public SyncOreS2C(AOreRestriction restriction) {
        this(restriction.getId(), restriction.getStage(), restriction.getOriginal(), restriction.getReplacement(), restriction.get(Attributes.MATCH_ALL_BLOCK_STATES));
    }

    @Override
    public void run(IPayloadContext context) {
        var restriction = new AClientOreRestriction(id, stage)
                .restrict(new OreWrapper(original, replacement))
                .set(Attributes.MATCH_ALL_BLOCK_STATES, stageAllBlockStates);

        AClientRestrictionManager.ORE_INSTANCE.addRestriction(restriction);
    }

    @Override
    public @NotNull Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}
