package com.alessandro.astages.infrastructure.networking.packet.dimension;

import com.alessandro.astages.api.AResourceLocation;
import com.alessandro.astages.api.nullability.NotNullMethodsReturn;
import com.alessandro.astages.engine.client.ClientMiscStorage;
import com.alessandro.astages.infrastructure.networking.AStagesPacket;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.neoforged.neoforge.network.handling.IPayloadContext;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@NotNullMethodsReturn
public record SyncDimensionIdsS2C(Set<String> ids) implements AStagesPacket {
    public static final Type<SyncDimensionIdsS2C> TYPE = new Type<>(AResourceLocation.fromNamespaceAndPath("sync_dimension_ids_s2c"));

    public static final StreamCodec<RegistryFriendlyByteBuf, SyncDimensionIdsS2C> STREAM_CODEC = StreamCodec.composite(
        ByteBufCodecs.STRING_UTF8.apply(ByteBufCodecs.collection(HashSet::new)), SyncDimensionIdsS2C::ids,
        SyncDimensionIdsS2C::new
    );

    @Override
    public void run(IPayloadContext context) {
        ClientMiscStorage.DIMENSION_IDS.clear();
        ClientMiscStorage.DIMENSION_IDS.addAll(ids);
    }

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}
