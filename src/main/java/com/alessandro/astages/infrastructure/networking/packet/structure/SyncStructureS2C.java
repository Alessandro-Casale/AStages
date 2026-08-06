package com.alessandro.astages.infrastructure.networking.packet.structure;

import com.alessandro.astages.api.AResourceLocation;
import com.alessandro.astages.api.nullability.NotNullParams;
import com.alessandro.astages.engine.AClientRestrictionManager;
import com.alessandro.astages.engine.client.restriction.AClientStructureRestriction;
import com.alessandro.astages.engine.server.restriction.AStructureRestriction;
import com.alessandro.astages.engine.store.Attributes;
import com.alessandro.astages.infrastructure.networking.AStagesPacket;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.network.handling.IPayloadContext;

import java.util.HashSet;
import java.util.Set;

@NotNullParams
public record SyncStructureS2C(String id, String stage, Set<ResourceLocation> structures, boolean enter) implements AStagesPacket {
    public static final Type<SyncStructureS2C> TYPE = new Type<>(AResourceLocation.fromNamespaceAndPath("sync_structure_s2c"));

    public static final StreamCodec<RegistryFriendlyByteBuf, SyncStructureS2C> STREAM_CODEC = StreamCodec.composite(
        ByteBufCodecs.STRING_UTF8, SyncStructureS2C::id,
        ByteBufCodecs.STRING_UTF8, SyncStructureS2C::stage,
        ResourceLocation.STREAM_CODEC.apply(ByteBufCodecs.collection(HashSet::new)), SyncStructureS2C::structures,
        ByteBufCodecs.BOOL, SyncStructureS2C::enter,
        SyncStructureS2C::new
    );

    public SyncStructureS2C(AStructureRestriction restriction) {
        this(restriction.getId(), restriction.getStage(), restriction.getStructures(), restriction.get(Attributes.ENTERING));
    }

    @Override
    public void run(IPayloadContext context) {
        var restriction = new AClientStructureRestriction(id, stage)
            .set(Attributes.ENTERING, enter);

        for (var structure : structures) {
            restriction.restrict(structure);
        }

        AClientRestrictionManager.STRUCTURE_INSTANCE.addRestriction(restriction);
    }

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}