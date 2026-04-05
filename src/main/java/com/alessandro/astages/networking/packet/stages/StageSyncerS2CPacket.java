package com.alessandro.astages.networking.packet.stages;

import com.alessandro.astages.api.AResourceLocation;
import com.alessandro.astages.api.nullability.NotNullMethodsReturn;
import com.alessandro.astages.api.stage.ClientStage;
import com.alessandro.astages.core.AClientStageManager;
import com.alessandro.astages.networking.AStagesPacket;
import com.alessandro.astages.store.StageAttributes;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.network.handling.IPayloadContext;

@NotNullMethodsReturn
public record StageSyncerS2CPacket(String stageKey, ItemStack stack) implements AStagesPacket {
    public static final Type<StageSyncerS2CPacket> TYPE = new Type<>(AResourceLocation.fromNamespaceAndPath("stage_syncer_s2c_packet"));

    public static final StreamCodec<RegistryFriendlyByteBuf, StageSyncerS2CPacket> STREAM_CODEC = StreamCodec.composite(
        ByteBufCodecs.STRING_UTF8, StageSyncerS2CPacket::stageKey,
        ItemStack.OPTIONAL_STREAM_CODEC, StageSyncerS2CPacket::stack,
        StageSyncerS2CPacket::new
    );

    @Override
    public void run(IPayloadContext context) {
        AClientStageManager.PERMANENT_INSTANCE.addStage(new ClientStage(stageKey).set(StageAttributes.ICON, stack));
    }

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}
