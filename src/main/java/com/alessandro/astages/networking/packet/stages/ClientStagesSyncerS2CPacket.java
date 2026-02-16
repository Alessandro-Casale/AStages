package com.alessandro.astages.networking.packet.stages;

import com.alessandro.astages.api.ALoader;
import com.alessandro.astages.api.AResourceLocation;
import com.alessandro.astages.api.ASetUtils;
import com.alessandro.astages.api.AStagesClientUtils;
import com.alessandro.astages.api.constant.AOperation;
import com.alessandro.astages.api.holder.AClientHolder;
import com.alessandro.astages.api.nullability.NotNullMethodsReturn;
import com.alessandro.astages.event.custom.ClientSynchronizeStagesEvent;
import com.alessandro.astages.networking.AStagesPacket;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.neoforged.neoforge.network.handling.IPayloadContext;

import java.util.HashSet;
import java.util.Set;

@NotNullMethodsReturn
public record ClientStagesSyncerS2CPacket(Set<String> stages, AOperation operation) implements AStagesPacket {
    public static final Type<ClientStagesSyncerS2CPacket> TYPE = new Type<>(AResourceLocation.fromNamespaceAndPath("client_stages_syncer_s2c_packet"));

    public static final StreamCodec<RegistryFriendlyByteBuf, ClientStagesSyncerS2CPacket> STREAM_CODEC = StreamCodec.composite(
        ByteBufCodecs.STRING_UTF8.apply(ByteBufCodecs.collection(HashSet::new)), ClientStagesSyncerS2CPacket::stages,
        ByteBufCodecs.idMapper(AOperation.BY_ID, AOperation::getId), ClientStagesSyncerS2CPacket::operation,
        ClientStagesSyncerS2CPacket::new
    );

    @Override
    public void run(IPayloadContext context) {
        switch (operation) {
            case ADD -> AStagesClientUtils.addStage(AClientHolder.player(), ASetUtils.getOnlyElement(stages));
            case ADD_ALL -> AStagesClientUtils.addStages(AClientHolder.player(), stages);
            case REMOVE -> AStagesClientUtils.removeStage(AClientHolder.player(), ASetUtils.getOnlyElement(stages));
            case REMOVE_ALL -> AStagesClientUtils.removeStages(AClientHolder.player(), stages);
            case LOGIN -> AStagesClientUtils.setStages(AClientHolder.player(), stages);
        }

        ALoader.EVENT_BUS.post(new ClientSynchronizeStagesEvent(stages, operation));
    }

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}
