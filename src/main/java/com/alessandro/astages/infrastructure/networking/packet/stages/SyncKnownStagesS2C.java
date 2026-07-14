package com.alessandro.astages.infrastructure.networking.packet.stages;

import com.alessandro.astages.api.AResourceLocation;
import com.alessandro.astages.api.constant.AStageSource;
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
public record SyncKnownStagesS2C(Collection<String> stages, ASyncOperation operation, AStageSource source) implements AStagesPacket {
    public static final CustomPacketPayload.Type<SyncKnownStagesS2C> TYPE = new CustomPacketPayload.Type<>(AResourceLocation.fromNamespaceAndPath("sync_known_stages_s2c"));

    public static final StreamCodec<RegistryFriendlyByteBuf, SyncKnownStagesS2C> STREAM_CODEC = StreamCodec.composite(
        ByteBufCodecs.STRING_UTF8.apply(ByteBufCodecs.collection(HashSet::new)), SyncKnownStagesS2C::stages,
        ACodecs.enumByName(ASyncOperation.class), SyncKnownStagesS2C::operation,
        ACodecs.enumByName(AStageSource.class), SyncKnownStagesS2C::source,
        SyncKnownStagesS2C::new
    );

    @Override
    public void run(IPayloadContext context) {
        switch (operation) {
            case ADD -> {
                if (source == AStageSource.PLAYER) { ClientMiscStorage.STAGES_ONLY_FOR_PLAYER.addAll(stages); }
                if (source == AStageSource.SERVER) { ClientMiscStorage.STAGES_ONLY_FOR_SERVER.addAll(stages); }
                if (source == AStageSource.BOTH) { ClientMiscStorage.ALL_STAGES.addAll(stages); }
            }
            case REMOVE -> {
                if (source == AStageSource.PLAYER) { ClientMiscStorage.STAGES_ONLY_FOR_PLAYER.removeAll(stages); }
                if (source == AStageSource.SERVER) { ClientMiscStorage.STAGES_ONLY_FOR_SERVER.removeAll(stages); }
                if (source == AStageSource.BOTH) { ClientMiscStorage.ALL_STAGES.removeAll(stages); }
            }
        }
    }

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}