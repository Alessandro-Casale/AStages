package com.alessandro.astages.infrastructure.networking.packet.stages;

import com.alessandro.astages.api.AResourceLocation;
import com.alessandro.astages.api.constant.AStageSource;
import com.alessandro.astages.api.holder.AClientHolder;
import com.alessandro.astages.api.nullability.NotNullParamsAndMethodsReturn;
import com.alessandro.astages.api.nullability.Nullable;
import com.alessandro.astages.api.util.AStagesClientUtils;
import com.alessandro.astages.infrastructure.networking.AStagesPacket;
import com.alessandro.astages.infrastructure.networking.Networking;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.neoforged.neoforge.network.handling.IPayloadContext;

import java.util.UUID;

@NotNullParamsAndMethodsReturn
public record RequestClientStagesS2C(AStageSource requester, AStageSource askedFor, @Nullable UUID requesterUUID, @Nullable UUID playerUUID) implements AStagesPacket {
    public static final Type<RequestClientStagesS2C> TYPE = new Type<>(AResourceLocation.fromNamespaceAndPath("request_client_stages_s2c"));

    public static final StreamCodec<FriendlyByteBuf, RequestClientStagesS2C> STREAM_CODEC = StreamCodec.of(
        RequestClientStagesS2C::encode,
        RequestClientStagesS2C::decode
    );

    private static void encode(FriendlyByteBuf buf, RequestClientStagesS2C packet) {
        buf.writeEnum(packet.requester);
        buf.writeEnum(packet.askedFor);
        if (packet.requester == AStageSource.PLAYER) { buf.writeUUID(packet.requesterUUID); }
        if (packet.askedFor == AStageSource.PLAYER) { buf.writeUUID(packet.playerUUID); }
    }

    private static RequestClientStagesS2C decode(FriendlyByteBuf buf) {
        var requester = buf.readEnum(AStageSource.class);
        var askedFor = buf.readEnum(AStageSource.class);
        var requesterUUID = requester == AStageSource.PLAYER ? buf.readUUID() : null;
        var playerUUID = askedFor == AStageSource.PLAYER ? buf.readUUID() : null;

        return new RequestClientStagesS2C(requester, askedFor, requesterUUID, playerUUID);
    }

    @Override
    public void run(IPayloadContext context) {
        var stages = askedFor == AStageSource.PLAYER ?
            AStagesClientUtils.getStages(AClientHolder.player()) :
            AStagesClientUtils.getStages(AClientHolder.server());

        Networking.sendToServer(new ReplyClientStagesC2S(requester, askedFor, requesterUUID, playerUUID, stages));
    }

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}
