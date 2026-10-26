package com.alessandro.astages.infrastructure.networking.packet.structure;

import com.alessandro.astages.api.AResourceLocation;
import com.alessandro.astages.api.network.data.CustomRestrictionData;
import com.alessandro.astages.api.network.data.RestrictionData;
import com.alessandro.astages.api.nullability.NotNullParamsAndMethodsReturn;
import com.alessandro.astages.engine.AClientRestrictionManager;
import com.alessandro.astages.engine.client.restriction.AClientStructureRestriction;
import com.alessandro.astages.engine.server.restriction.AStructureRestriction;
import com.alessandro.astages.engine.store.Attributes;
import com.alessandro.astages.infrastructure.networking.ARestrictionPacket;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.levelgen.structure.Structure;
import net.neoforged.neoforge.network.handling.IPayloadContext;

import java.util.HashSet;
import java.util.Set;

@NotNullParamsAndMethodsReturn
public record SyncStructureS2C(RestrictionData data, Set<ResourceKey<Structure>> structures) implements ARestrictionPacket {
    public static final Type<SyncStructureS2C> TYPE = new Type<>(AResourceLocation.fromNamespaceAndPath("sync_structure_s2c"));

    public static final StreamCodec<RegistryFriendlyByteBuf, SyncStructureS2C> STREAM_CODEC = StreamCodec.composite(
        RestrictionData.STREAM_CODEC, SyncStructureS2C::data,
        ResourceKey.streamCodec(Registries.STRUCTURE).apply(ByteBufCodecs.collection(HashSet::new)), SyncStructureS2C::structures,
        SyncStructureS2C::new
    );

    public SyncStructureS2C(AStructureRestriction restriction) {
        this(CustomRestrictionData.structure(restriction), restriction.getStructures());
    }

    @Override
    public void run(IPayloadContext context) {
        var restriction = new AClientStructureRestriction(id(), stage(), priority())
            .setFromData(data, Attributes.ENTERING);

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