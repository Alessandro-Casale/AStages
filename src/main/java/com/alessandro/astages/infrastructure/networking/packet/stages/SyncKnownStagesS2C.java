package com.alessandro.astages.infrastructure.networking.packet.stages;

import com.alessandro.astages.api.AResourceLocation;
import com.alessandro.astages.api.constant.ASyncOperation;
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
public record SyncKnownStagesS2C(Collection<String> stages, ASyncOperation operation) implements AStagesPacket {
    public static final CustomPacketPayload.Type<SyncKnownStagesS2C> TYPE = new CustomPacketPayload.Type<>(AResourceLocation.fromNamespaceAndPath("sync_known_stages_s2c"));

    public static final StreamCodec<RegistryFriendlyByteBuf, SyncKnownStagesS2C> STREAM_CODEC = StreamCodec.composite(
            ByteBufCodecs.STRING_UTF8.apply(ByteBufCodecs.collection(HashSet::new)), SyncKnownStagesS2C::stages,
            ByteBufCodecs.idMapper(ASyncOperation.BY_ID, ASyncOperation::getId), SyncKnownStagesS2C::operation,
            SyncKnownStagesS2C::new
    );

    @Override
    public void run(IPayloadContext context) {
        switch (operation) {
            case ADD -> ClientMiscStorage.ALL_STAGES.addAll(stages);
            case REMOVE -> ClientMiscStorage.ALL_STAGES.removeAll(stages);
        }
    }

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}