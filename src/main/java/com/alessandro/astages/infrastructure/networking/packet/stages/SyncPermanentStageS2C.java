package com.alessandro.astages.infrastructure.networking.packet.stages;

import com.alessandro.astages.api.AResourceLocation;
import com.alessandro.astages.api.nullability.NotNullMethodsReturn;
import com.alessandro.astages.api.stage.ClientStage;
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
public record SyncPermanentStageS2C(String stageKey, ItemStack stack) implements AStagesPacket {
    public static final Type<SyncPermanentStageS2C> TYPE = new Type<>(AResourceLocation.fromNamespaceAndPath("sync_permanent_stage_s2c"));

    public static final StreamCodec<RegistryFriendlyByteBuf, SyncPermanentStageS2C> STREAM_CODEC = StreamCodec.composite(
        ByteBufCodecs.STRING_UTF8, SyncPermanentStageS2C::stageKey,
        ItemStack.OPTIONAL_STREAM_CODEC, SyncPermanentStageS2C::stack,
        SyncPermanentStageS2C::new
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
