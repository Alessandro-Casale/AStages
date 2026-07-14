package com.alessandro.astages.infrastructure.networking.packet.simple;

import com.alessandro.astages.api.AResourceLocation;
import com.alessandro.astages.api.constant.ASyncOperation;
import com.alessandro.astages.api.network.ACodecs;
import com.alessandro.astages.api.nullability.NotNullMethodsReturn;
import com.alessandro.astages.engine.client.ClientMiscStorage;
import com.alessandro.astages.infrastructure.networking.AStagesPacket;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.neoforged.neoforge.network.handling.IPayloadContext;

import java.util.Collection;
import java.util.HashSet;

@NotNullMethodsReturn
public record SyncSimpleIdsS2C(Collection<String> ids, ASyncOperation operation) implements AStagesPacket {
    public static final Type<SyncSimpleIdsS2C> TYPE = new Type<>(AResourceLocation.fromNamespaceAndPath("sync_simple_ids_s2c"));

    public static final StreamCodec<RegistryFriendlyByteBuf, SyncSimpleIdsS2C> STREAM_CODEC = StreamCodec.composite(
        ByteBufCodecs.STRING_UTF8.apply(ByteBufCodecs.collection(HashSet::new)), SyncSimpleIdsS2C::ids,
        ACodecs.enumByName(ASyncOperation.class), SyncSimpleIdsS2C::operation,
        SyncSimpleIdsS2C::new
    );

    @Override
    public void run(IPayloadContext context) {
        switch (operation) {
            case ADD -> ClientMiscStorage.SIMPLE_IDS.addAll(ids);
            case REMOVE -> ids.forEach(ClientMiscStorage.SIMPLE_IDS::remove);
        }
    }

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}
