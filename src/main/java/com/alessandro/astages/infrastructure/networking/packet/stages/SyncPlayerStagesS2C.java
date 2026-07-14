package com.alessandro.astages.infrastructure.networking.packet.stages;

import com.alessandro.astages.api.ALoader;
import com.alessandro.astages.api.AResourceLocation;
import com.alessandro.astages.api.constant.AOperation;
import com.alessandro.astages.api.event.sync.ClientSynchronizeStagesEvent;
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
public record SyncPlayerStagesS2C(Set<String> stages, AOperation operation) implements AStagesPacket {
    public static final Type<SyncPlayerStagesS2C> TYPE = new Type<>(AResourceLocation.fromNamespaceAndPath("sync_player_stages_s2c"));

    public static final StreamCodec<RegistryFriendlyByteBuf, SyncPlayerStagesS2C> STREAM_CODEC = StreamCodec.composite(
        ByteBufCodecs.STRING_UTF8.apply(ByteBufCodecs.collection(HashSet::new)), SyncPlayerStagesS2C::stages,
        ACodecs.enumByName(AOperation.class), SyncPlayerStagesS2C::operation,
        SyncPlayerStagesS2C::new
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
