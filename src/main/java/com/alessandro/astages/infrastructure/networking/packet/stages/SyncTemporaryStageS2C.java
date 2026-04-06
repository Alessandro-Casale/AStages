package com.alessandro.astages.infrastructure.networking.packet.stages;

import com.alessandro.astages.api.AResourceLocation;
import com.alessandro.astages.api.nullability.NotNullMethodsReturn;
import com.alessandro.astages.api.stage.ClientTemporaryStage;
import com.alessandro.astages.engine.AClientStageManager;
import com.alessandro.astages.engine.store.StageAttributes;
import com.alessandro.astages.infrastructure.networking.AStagesPacket;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.network.handling.IPayloadContext;

@NotNullMethodsReturn
public record SyncTemporaryStageS2C(String stageKey, ItemStack stack) implements AStagesPacket {
    public static final Type<SyncTemporaryStageS2C> TYPE = new Type<>(AResourceLocation.fromNamespaceAndPath("sync_temporary_stage_s2c"));

    public static final StreamCodec<RegistryFriendlyByteBuf, SyncTemporaryStageS2C> STREAM_CODEC = StreamCodec.composite(
        ByteBufCodecs.STRING_UTF8, SyncTemporaryStageS2C::stageKey,
        ItemStack.OPTIONAL_STREAM_CODEC, SyncTemporaryStageS2C::stack,
        SyncTemporaryStageS2C::new
    );

    @Override
    public void run(IPayloadContext context) {
        AClientStageManager.TEMPORARY_INSTANCE.addStage(new ClientTemporaryStage(stageKey).set(StageAttributes.ICON, stack));
    }

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}
