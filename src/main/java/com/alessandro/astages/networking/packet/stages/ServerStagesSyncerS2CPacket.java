package com.alessandro.astages.networking.packet.stages;

import com.alessandro.astages.api.ALoader;
import com.alessandro.astages.api.AResourceLocation;
import com.alessandro.astages.api.ASetUtils;
import com.alessandro.astages.api.AStagesClientUtils;
import com.alessandro.astages.api.constant.AOperation;
import com.alessandro.astages.api.holder.AClientHolder;
import com.alessandro.astages.event.custom.ClientSynchronizeServerStagesEvent;
import com.alessandro.astages.networking.AStagesPacket;
import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.neoforged.neoforge.network.handling.IPayloadContext;

import java.util.HashSet;
import java.util.Set;

@MethodsReturnNonnullByDefault
public record ServerStagesSyncerS2CPacket(Set<String> stages, AOperation operation) implements AStagesPacket {
    public static final Type<ServerStagesSyncerS2CPacket> TYPE = new Type<>(AResourceLocation.fromNamespaceAndPath("server_stages_syncer_s2c_packet"));

    public static final StreamCodec<RegistryFriendlyByteBuf, ServerStagesSyncerS2CPacket> STREAM_CODEC = StreamCodec.composite(
        ByteBufCodecs.STRING_UTF8.apply(ByteBufCodecs.collection(HashSet::new)), ServerStagesSyncerS2CPacket::stages,
        ByteBufCodecs.idMapper(AOperation.BY_ID, AOperation::getId), ServerStagesSyncerS2CPacket::operation,
        ServerStagesSyncerS2CPacket::new
    );

    @Override
    public void run(IPayloadContext context) {
        switch (operation) {
            case ADD -> AStagesClientUtils.addStage(AClientHolder.server(), ASetUtils.getOnlyElement(stages));
            case ADD_ALL -> AStagesClientUtils.addStages(AClientHolder.server(), stages);
            case REMOVE -> AStagesClientUtils.removeStage(AClientHolder.server(), ASetUtils.getOnlyElement(stages));
            case REMOVE_ALL -> AStagesClientUtils.removeStages(AClientHolder.server(), stages);
            case LOGIN -> AStagesClientUtils.setStages(AClientHolder.server(), stages);
        }

        ALoader.EVENT_BUS.post(new ClientSynchronizeServerStagesEvent(stages, operation));
    }

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}
