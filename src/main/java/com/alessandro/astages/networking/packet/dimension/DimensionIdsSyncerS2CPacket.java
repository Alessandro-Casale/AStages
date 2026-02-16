package com.alessandro.astages.networking.packet.dimension;

import com.alessandro.astages.api.AResourceLocation;
import com.alessandro.astages.api.nullability.NotNullMethodsReturn;
import com.alessandro.astages.core.AClientRestrictionManager;
import com.alessandro.astages.networking.AStagesPacket;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.neoforged.neoforge.network.handling.IPayloadContext;

import java.util.List;

@NotNullMethodsReturn
public record DimensionIdsSyncerS2CPacket(List<String> ids) implements AStagesPacket {
    public static final Type<DimensionIdsSyncerS2CPacket> TYPE = new Type<>(AResourceLocation.fromNamespaceAndPath("dimension_ids_syncer_s2c_packet"));

    public static final StreamCodec<RegistryFriendlyByteBuf, DimensionIdsSyncerS2CPacket> STREAM_CODEC = StreamCodec.composite(
        ByteBufCodecs.STRING_UTF8.apply(ByteBufCodecs.list()), DimensionIdsSyncerS2CPacket::ids,
        DimensionIdsSyncerS2CPacket::new
    );

    @Override
    public void run(IPayloadContext context) {
        AClientRestrictionManager.DIMENSION_IDS.clear();
        AClientRestrictionManager.DIMENSION_IDS.addAll(ids);
    }

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}
