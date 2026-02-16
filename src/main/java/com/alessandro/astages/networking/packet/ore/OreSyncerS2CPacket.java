package com.alessandro.astages.networking.packet.ore;

import com.alessandro.astages.api.AResourceLocation;
import com.alessandro.astages.api.nullability.NotNullParams;
import com.alessandro.astages.core.AClientRestrictionManager;
import com.alessandro.astages.core.client.restriction.AClientOreRestriction;
import com.alessandro.astages.core.server.restriction.AOreRestriction;
import com.alessandro.astages.core.wrapper.OreWrapper;
import com.alessandro.astages.networking.AStagesPacket;
import com.alessandro.astages.store.Attributes;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import org.jetbrains.annotations.NotNull;

@NotNullParams
public record OreSyncerS2CPacket(String id, String stage, BlockState original, BlockState replacement, boolean stageAllBlockStates) implements AStagesPacket {
    public static final Type<OreSyncerS2CPacket> TYPE = new Type<>(AResourceLocation.fromNamespaceAndPath("ore_syncer_s2c_packet"));

    public static final StreamCodec<RegistryFriendlyByteBuf, OreSyncerS2CPacket> STREAM_CODEC = StreamCodec.composite(
        ByteBufCodecs.STRING_UTF8, OreSyncerS2CPacket::id,
        ByteBufCodecs.STRING_UTF8, OreSyncerS2CPacket::stage,
        ByteBufCodecs.fromCodec(BlockState.CODEC), OreSyncerS2CPacket::original,
        ByteBufCodecs.fromCodec(BlockState.CODEC), OreSyncerS2CPacket::replacement,
        ByteBufCodecs.BOOL, OreSyncerS2CPacket::stageAllBlockStates,
        OreSyncerS2CPacket::new
    );

    public OreSyncerS2CPacket(AOreRestriction restriction) {
        this(restriction.getId(), restriction.getStage(), restriction.getOriginal(), restriction.getReplacement(), restriction.get(Attributes.STAGE_ALL_BLOCK_STATES));
    }

    @Override
    public void run(IPayloadContext context) {
        var restriction = new AClientOreRestriction(id, stage)
                .restrict(new OreWrapper(original, replacement))
                .set(Attributes.STAGE_ALL_BLOCK_STATES, stageAllBlockStates);

        AClientRestrictionManager.ORE_INSTANCE.addRestriction(restriction);
    }

    @Override
    public @NotNull Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}
