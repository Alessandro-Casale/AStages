package com.alessandro.astages.infrastructure.networking.packet.stages;

import com.alessandro.astages.api.ALoader;
import com.alessandro.astages.api.AResourceLocation;
import com.alessandro.astages.api.constant.AOperation;
import com.alessandro.astages.api.event.sync.ClientSynchronizeServerStagesEvent;
import com.alessandro.astages.api.holder.AClientHolder;
import com.alessandro.astages.api.network.ACodecs;
import com.alessandro.astages.api.nullability.NotNullMethodsReturn;
import com.alessandro.astages.api.util.ASetUtils;
import com.alessandro.astages.api.util.AStagesClientUtils;
import com.alessandro.astages.infrastructure.networking.AStagesPacket;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.neoforged.neoforge.network.handling.IPayloadContext;

import java.util.HashSet;
import java.util.Set;

@NotNullMethodsReturn
public record SyncServerStagesS2C(Set<String> stages, AOperation operation) implements AStagesPacket {
    public static final Type<SyncServerStagesS2C> TYPE = new Type<>(AResourceLocation.fromNamespaceAndPath("sync_server_stages_s2c"));

    public static final StreamCodec<RegistryFriendlyByteBuf, SyncServerStagesS2C> STREAM_CODEC = StreamCodec.composite(
        ByteBufCodecs.STRING_UTF8.apply(ByteBufCodecs.collection(HashSet::new)), SyncServerStagesS2C::stages,
        ACodecs.enumByName(AOperation.class), SyncServerStagesS2C::operation,
        SyncServerStagesS2C::new
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
