package com.alessandro.astages.networking.packet.mob;

import com.alessandro.astages.api.AResourceLocation;
import com.alessandro.astages.core.AClientRestrictionManager;
import com.alessandro.astages.core.client.restriction.AClientMobRestriction;
import com.alessandro.astages.core.server.restriction.AMobRestriction;
import com.alessandro.astages.networking.AStagesPacket;
import com.alessandro.astages.store.Attributes;
import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.ComponentSerialization;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.world.entity.EntityType;
import net.neoforged.neoforge.network.handling.IPayloadContext;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.List;

@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public record MobSyncerS2CPacket(String id, String stage, List<EntityType<?>> types, Component jadeMobMessage) implements AStagesPacket {
    public static final Type<MobSyncerS2CPacket> TYPE = new Type<>(AResourceLocation.fromNamespaceAndPath("mob_syncer_s2c_packet"));

    public static final StreamCodec<RegistryFriendlyByteBuf, MobSyncerS2CPacket> STREAM_CODEC = StreamCodec.composite(
        ByteBufCodecs.STRING_UTF8, MobSyncerS2CPacket::id,
        ByteBufCodecs.STRING_UTF8, MobSyncerS2CPacket::stage,
        ByteBufCodecs.registry(Registries.ENTITY_TYPE).apply(ByteBufCodecs.list()), MobSyncerS2CPacket::types,
        ByteBufCodecs.fromCodec(ComponentSerialization.CODEC), MobSyncerS2CPacket::jadeMobMessage,
        MobSyncerS2CPacket::new
    );

    public MobSyncerS2CPacket(AMobRestriction restriction) {
        this(restriction.getId(), restriction.getStage(), restriction.getMobs(), restriction.get(Attributes.Mob.JADE_MOB_MESSAGE).get());
    }

    @Override
    public void run(IPayloadContext context) {
        var restriction = new AClientMobRestriction(id, stage)
                .set(Attributes.Mob.JADE_MOB_MESSAGE, () -> jadeMobMessage);

        for (var type : types) {
            restriction.restrict(type);
        }

        AClientRestrictionManager.MOB_INSTANCE.addRestriction(restriction);
    }

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}
