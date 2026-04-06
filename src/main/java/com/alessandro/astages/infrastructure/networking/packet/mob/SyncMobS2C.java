package com.alessandro.astages.infrastructure.networking.packet.mob;

import com.alessandro.astages.api.AResourceLocation;
import com.alessandro.astages.api.nullability.NotNullParamsAndMethodsReturn;
import com.alessandro.astages.engine.AClientRestrictionManager;
import com.alessandro.astages.engine.client.restriction.AClientMobRestriction;
import com.alessandro.astages.engine.server.restriction.AMobRestriction;
import com.alessandro.astages.infrastructure.networking.AStagesPacket;
import com.alessandro.astages.engine.store.Attributes;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.ComponentSerialization;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.world.entity.EntityType;
import net.neoforged.neoforge.network.handling.IPayloadContext;

import java.util.List;

@NotNullParamsAndMethodsReturn
public record SyncMobS2C(String id, String stage, List<EntityType<?>> types, Component jadeMobMessage) implements AStagesPacket {
    public static final Type<SyncMobS2C> TYPE = new Type<>(AResourceLocation.fromNamespaceAndPath("sync_mob_s2c"));

    public static final StreamCodec<RegistryFriendlyByteBuf, SyncMobS2C> STREAM_CODEC = StreamCodec.composite(
        ByteBufCodecs.STRING_UTF8, SyncMobS2C::id,
        ByteBufCodecs.STRING_UTF8, SyncMobS2C::stage,
        ByteBufCodecs.registry(Registries.ENTITY_TYPE).apply(ByteBufCodecs.list()), SyncMobS2C::types,
        ByteBufCodecs.fromCodec(ComponentSerialization.CODEC), SyncMobS2C::jadeMobMessage,
        SyncMobS2C::new
    );

    public SyncMobS2C(AMobRestriction restriction) {
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
