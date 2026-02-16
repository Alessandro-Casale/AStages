package com.alessandro.astages.networking.packet.simple;

import com.alessandro.astages.api.AResourceLocation;
import com.alessandro.astages.api.constant.ASyncOperation;
import com.alessandro.astages.api.nullability.NotNullMethodsReturn;
import com.alessandro.astages.core.AClientRestrictionManager;
import com.alessandro.astages.networking.AStagesPacket;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.neoforged.neoforge.network.handling.IPayloadContext;

import java.util.Collection;
import java.util.HashSet;

@NotNullMethodsReturn
public record SimpleIdsSyncerS2CPacket(Collection<String> ids, ASyncOperation operation) implements AStagesPacket {
    public static final Type<SimpleIdsSyncerS2CPacket> TYPE = new Type<>(AResourceLocation.fromNamespaceAndPath("simple_stages_syncer_s2c_packet"));

    public static final StreamCodec<RegistryFriendlyByteBuf, SimpleIdsSyncerS2CPacket> STREAM_CODEC = StreamCodec.composite(
            ByteBufCodecs.STRING_UTF8.apply(ByteBufCodecs.collection(HashSet::new)), SimpleIdsSyncerS2CPacket::ids,
            ByteBufCodecs.idMapper(ASyncOperation.BY_ID, ASyncOperation::getId), SimpleIdsSyncerS2CPacket::operation,
            SimpleIdsSyncerS2CPacket::new
    );

    @Override
    public void run(IPayloadContext context) {
        switch (operation) {
            case ADD -> AClientRestrictionManager.SIMPLE_IDS.addAll(ids);
            case REMOVE -> ids.forEach(AClientRestrictionManager.SIMPLE_IDS::remove);
        }
    }

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}
