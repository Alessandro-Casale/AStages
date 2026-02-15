package com.alessandro.astages.networking.packet.stages;

import com.alessandro.astages.api.AResourceLocation;
import com.alessandro.astages.api.AStagesClientUtils;
import com.alessandro.astages.api.constant.AStageSource;
import com.alessandro.astages.api.holder.AClientHolder;
import com.alessandro.astages.api.nullability.NotNullParamsAndMethodsReturn;
import com.alessandro.astages.api.nullability.Nullable;
import com.alessandro.astages.networking.ANetworking;
import com.alessandro.astages.networking.AStagesPacket;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.neoforged.neoforge.network.handling.IPayloadContext;

import java.util.UUID;

@NotNullParamsAndMethodsReturn
public record RequestClientStagesS2CPacket(AStageSource requester, AStageSource askedFor, @Nullable UUID requesterUUID, @Nullable UUID playerUUID) implements AStagesPacket {
    public static final Type<RequestClientStagesS2CPacket> TYPE = new Type<>(AResourceLocation.fromNamespaceAndPath("request_client_stages_s2c_packet"));

    public static final StreamCodec<FriendlyByteBuf, RequestClientStagesS2CPacket> STREAM_CODEC = StreamCodec.of(
        RequestClientStagesS2CPacket::encode,
        RequestClientStagesS2CPacket::decode
    );

    private static void encode(FriendlyByteBuf buf, RequestClientStagesS2CPacket packet) {
        buf.writeEnum(packet.requester);
        buf.writeEnum(packet.askedFor);
        if (packet.requester == AStageSource.PLAYER) { buf.writeUUID(packet.requesterUUID); }
        if (packet.askedFor == AStageSource.PLAYER) { buf.writeUUID(packet.playerUUID); }
    }

    private static RequestClientStagesS2CPacket decode(FriendlyByteBuf buf) {
        var requester = buf.readEnum(AStageSource.class);
        var askedFor = buf.readEnum(AStageSource.class);
        var requesterUUID = requester == AStageSource.PLAYER ? buf.readUUID() : null;
        var playerUUID = askedFor == AStageSource.PLAYER ? buf.readUUID() : null;

        return new RequestClientStagesS2CPacket(requester, askedFor, requesterUUID, playerUUID);
    }

    @Override
    public void run(IPayloadContext context) {
        var stages = askedFor == AStageSource.PLAYER ?
            AStagesClientUtils.getStages(AClientHolder.player()) :
            AStagesClientUtils.getStages(AClientHolder.server());

        ANetworking.sendToServer(new ClientStagesC2SPacket(requester, askedFor, requesterUUID, playerUUID, stages));
    }

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}
