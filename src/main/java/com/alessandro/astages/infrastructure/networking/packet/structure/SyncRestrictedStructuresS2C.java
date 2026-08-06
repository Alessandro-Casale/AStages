package com.alessandro.astages.infrastructure.networking.packet.structure;

import com.alessandro.astages.api.AResourceLocation;
import com.alessandro.astages.api.misc.Twin;
import com.alessandro.astages.api.network.ACodecs;
import com.alessandro.astages.api.nullability.NotNullMethodsReturn;
import com.alessandro.astages.infrastructure.networking.AStagesPacket;
import io.netty.buffer.ByteBuf;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.levelgen.structure.BoundingBox;
import net.neoforged.neoforge.network.handling.IPayloadContext;

import java.util.List;

@NotNullMethodsReturn
public record SyncRestrictedStructuresS2C(ResourceKey<Level> dimension, ChunkPos chunkPos, List<Twin<String, BoundingBox>> boxes) implements AStagesPacket {
    public static final Type<SyncRestrictedStructuresS2C> TYPE = new Type<>(AResourceLocation.fromNamespaceAndPath("sync_restricted_structures_s2c"));

    public static final StreamCodec<ByteBuf, SyncRestrictedStructuresS2C> STREAM_CODEC = StreamCodec.composite(
        ResourceKey.streamCodec(Registries.DIMENSION), SyncRestrictedStructuresS2C::dimension,
        ACodecs.CHUNK_POS_CODEC, SyncRestrictedStructuresS2C::chunkPos,
        Twin.codec(ByteBufCodecs.STRING_UTF8, ACodecs.BOUNDING_BOX_CODEC).apply(ByteBufCodecs.list()), SyncRestrictedStructuresS2C::boxes,
        SyncRestrictedStructuresS2C::new
    );

    @Override
    public void run(IPayloadContext context) {
        StructureCollisionManager.CLIENT_INSTANCE.populateClientCacheForChunk(dimension, chunkPos, boxes);
    }

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}