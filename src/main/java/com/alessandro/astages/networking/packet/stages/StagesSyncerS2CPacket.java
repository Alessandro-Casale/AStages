package com.alessandro.astages.networking.packet.stages;

import com.alessandro.astages.api.AResourceLocation;
import com.alessandro.astages.api.constant.ASyncOperation;
import com.alessandro.astages.core.AClientRestrictionManager;
import com.alessandro.astages.networking.AStagesPacket;
import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.neoforged.neoforge.network.handling.IPayloadContext;

import java.util.Collection;
import java.util.HashSet;

@MethodsReturnNonnullByDefault
public record StagesSyncerS2CPacket(Collection<String> stages, ASyncOperation operation) implements AStagesPacket {
    public static final CustomPacketPayload.Type<StagesSyncerS2CPacket> TYPE = new CustomPacketPayload.Type<>(AResourceLocation.fromNamespaceAndPath("stages_syncer_s2c_packet"));

    public static final StreamCodec<RegistryFriendlyByteBuf, StagesSyncerS2CPacket> STREAM_CODEC = StreamCodec.composite(
            ByteBufCodecs.STRING_UTF8.apply(ByteBufCodecs.collection(HashSet::new)), StagesSyncerS2CPacket::stages,
            ByteBufCodecs.idMapper(ASyncOperation.BY_ID, ASyncOperation::getId), StagesSyncerS2CPacket::operation,
            StagesSyncerS2CPacket::new
    );

    @Override
    public void run(IPayloadContext context) {
        switch (operation) {
            case ADD -> AClientRestrictionManager.ALL_STAGES.addAll(stages);
            case REMOVE -> AClientRestrictionManager.ALL_STAGES.removeAll(stages);
        }
    }

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}