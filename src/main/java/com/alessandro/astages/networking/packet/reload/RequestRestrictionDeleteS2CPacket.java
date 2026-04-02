package com.alessandro.astages.networking.packet.reload;

import com.alessandro.astages.api.AResourceLocation;
import com.alessandro.astages.core.AClientRestrictionManager;
import com.alessandro.astages.networking.AStagesPacket;
import com.alessandro.astages.registry.AStagesRegistries;
import com.alessandro.astages.store.ARestrictionType;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import org.jetbrains.annotations.NotNull;

public record RequestRestrictionDeleteS2CPacket(String id, ARestrictionType restrictionType) implements AStagesPacket {
    public static final Type<RequestRestrictionDeleteS2CPacket> TYPE = new Type<>(AResourceLocation.fromNamespaceAndPath("request_restriction_delete_s2c_packet"));

    public static final StreamCodec<RegistryFriendlyByteBuf, RequestRestrictionDeleteS2CPacket> STREAM_CODEC = StreamCodec.composite(
            ByteBufCodecs.STRING_UTF8, RequestRestrictionDeleteS2CPacket::id,
            ByteBufCodecs.registry(AStagesRegistries.RESTRICTION_TYPES.key()), RequestRestrictionDeleteS2CPacket::restrictionType,
            RequestRestrictionDeleteS2CPacket::new
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
