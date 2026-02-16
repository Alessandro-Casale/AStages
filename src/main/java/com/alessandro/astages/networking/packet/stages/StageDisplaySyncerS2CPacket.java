package com.alessandro.astages.networking.packet.stages;

import com.alessandro.astages.api.AResourceLocation;
import com.alessandro.astages.api.nullability.NotNullMethodsReturn;
import com.alessandro.astages.api.stage.ClientStage;
import com.alessandro.astages.core.AClientStageManager;
import com.alessandro.astages.networking.AStagesPacket;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.network.handling.IPayloadContext;

@NotNullMethodsReturn
public record StageDisplaySyncerS2CPacket(String stageKey, ItemStack stack) implements AStagesPacket {
    public static final Type<StageDisplaySyncerS2CPacket> TYPE = new Type<>(AResourceLocation.fromNamespaceAndPath("stage_display_syncer_s2c_packet"));

    public static final StreamCodec<RegistryFriendlyByteBuf, StageDisplaySyncerS2CPacket> STREAM_CODEC = StreamCodec.composite(
        ByteBufCodecs.STRING_UTF8, StageDisplaySyncerS2CPacket::stageKey,
        ItemStack.STREAM_CODEC, StageDisplaySyncerS2CPacket::stack,
        StageDisplaySyncerS2CPacket::new
    );

    @Override
    public void run(IPayloadContext context) {
        AClientStageManager.GENERIC_INSTANCE.addStageInternal(stageKey, new ClientStage(stageKey, stack));
    }

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}
