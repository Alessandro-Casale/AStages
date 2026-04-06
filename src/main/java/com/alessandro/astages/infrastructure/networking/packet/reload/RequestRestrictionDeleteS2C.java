package com.alessandro.astages.infrastructure.networking.packet.reload;

import com.alessandro.astages.api.AResourceLocation;
import com.alessandro.astages.api.store.ARestrictionType;
import com.alessandro.astages.engine.AClientRestrictionManager;
import com.alessandro.astages.infrastructure.networking.AStagesPacket;
import com.alessandro.astages.infrastructure.registry.AStagesRegistries;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import org.jetbrains.annotations.NotNull;

public record RequestRestrictionDeleteS2C(String id, ARestrictionType restrictionType) implements AStagesPacket {
    public static final Type<RequestRestrictionDeleteS2C> TYPE = new Type<>(AResourceLocation.fromNamespaceAndPath("request_restriction_delete_s2c"));

    public static final StreamCodec<RegistryFriendlyByteBuf, RequestRestrictionDeleteS2C> STREAM_CODEC = StreamCodec.composite(
            ByteBufCodecs.STRING_UTF8, RequestRestrictionDeleteS2C::id,
            ByteBufCodecs.registry(AStagesRegistries.RESTRICTION_TYPES.key()), RequestRestrictionDeleteS2C::restrictionType,
            RequestRestrictionDeleteS2C::new
    );

    @Override
    public void run(IPayloadContext context) {
        AClientRestrictionManager.removeRestriction(id, restrictionType);
    }

    @Override
    public @NotNull Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}
