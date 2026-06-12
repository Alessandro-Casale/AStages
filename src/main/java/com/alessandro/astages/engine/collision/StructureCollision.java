package com.alessandro.astages.engine.collision;

import com.alessandro.astages.api.base.StructureCollisionCache;
import com.alessandro.astages.api.develop.UnderDevelopment;
import com.alessandro.astages.api.holder.AHolder;
import com.alessandro.astages.api.misc.Twin;
import com.alessandro.astages.api.nullability.NotNullParams;
import com.alessandro.astages.api.util.AStagesUtils;
import com.alessandro.astages.engine.ARestrictionManager;
import com.alessandro.astages.engine.store.Attributes;
import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.levelgen.structure.BoundingBox;
import net.minecraft.world.level.levelgen.structure.StructureStart;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;

import java.util.*;
import java.util.function.BiPredicate;

@NotNullParams
public class StructureCollision {
    private final StructureCollisionCache<StructureStart> CACHE = new StructureCollisionCache<>();

    public Set<VoxelShape> getRestrictedShapesForChunk(ServerLevel level, ChunkPos chunkPos, Player player) {
        // buildServerCacheForChunk(level, chunkPos);

        return getShapesForChunk(level.dimension(), chunkPos,
                (restrictionId, start) -> {
                    var restriction = ARestrictionManager.STRUCTURE_INSTANCE.getRestriction(restrictionId);
                    return restriction != null &&
                            !AStagesUtils.hasStage(AHolder.serverAndPlayer(player), restriction.getStage()) &&
                            restriction.isDisabled(Attributes.ENTERING);
                });
    }

    public Set<VoxelShape> getShapesForChunk(ResourceKey<Level> dimension, ChunkPos chunkPos, BiPredicate<String, StructureStart> filter) {
        var toReturn = new HashSet<VoxelShape>();
        var starts = getStructureStartsForChunks(dimension, chunkPos, 1, filter);

        for (var start: starts) {
            var box = start.getBoundingBox();
            toReturn.add(Shapes.create(box.minX(), box.minY(), box.minZ(), box.maxX(), box.maxY(), box.maxZ()));
        }

        return toReturn;
    }

//    public Set<AABB> getRestrictedAABBsForChunk(ServerLevel level, ChunkPos chunkPos, Player player) {
//        buildServerCacheForChunk(level, chunkPos);
//
//        return getAABBForChunk(level.dimension(), chunkPos,
//                (restrictionId, start) -> {
//                    var restriction = ARestrictionManager.STRUCTURE_INSTANCE.getRestriction(restrictionId);
//                    return restriction != null &&
//                            !AStagesUtils.hasStage(AHolder.serverAndPlayer(player), restriction.getStage()) &&
//                            restriction.isDisabled(Attributes.ENTERING);
//                });
//    }
//
//    public Set<AABB> getAABBForChunk(ResourceKey<Level> dimension, ChunkPos chunkPos, BiPredicate<String, StructureStart> filter) {
//        var toReturn = new HashSet<AABB>();
//        var starts = getStructureStartsForChunks(dimension, chunkPos, 1, filter);
//
//        for (var start: starts) {
//            toReturn.add(AABB.of(start.getBoundingBox()));
//        }
//
//        return toReturn;
//    }

    public List<StructureStart> getStructuresForBlockPos(ResourceKey<Level> dimension, BlockPos pos) {
        var cache = new ArrayList<StructureStart>();

        for (var twin : CACHE.getTwinsFor(dimension, ChunkPos.asLong(pos))) {
            var bb = twin.value().getBoundingBox();

            if (bb.isInside(pos.getX(), pos.getY(), pos.getZ())) {
                cache.add(twin.value());
            }
        }

        return cache;
    }

    public List<Twin<String, BoundingBox>> getCacheForChunk(ResourceKey<Level> dimension, ChunkPos chunkPos) {
        var cache = new ArrayList<Twin<String, BoundingBox>>();

        for (var twin : CACHE.getTwinsFor(dimension, chunkPos.toLong())) {
            cache.add(new Twin<>(twin.id(), twin.value().getBoundingBox()));
        }

        return cache;
    }

    // Remember to call buildServerCacheForChunkPos before!
    public Set<StructureStart> getStructureStartsForChunks(ResourceKey<Level> dimension, ChunkPos center, int range, BiPredicate<String, StructureStart> filter) {
        var toReturn = new HashSet<StructureStart>();

        for (int x = -range; x <= range; x++) {
            for (int z = -range; z <= range; z++) {
                var twins = CACHE.getTwinsFor(dimension, ChunkPos.asLong(center.x + x, center.z + z));

                for (var twin : twins) {
                    if (filter.test(twin.id(), twin.value())) {
                        toReturn.add(twin.value());
                    }
                }
            }
        }

        return toReturn;
    }

    @UnderDevelopment("isChunkCached doesn't work if the chunk is built from this method!" +
        "Try to use structure pieces!")
    public void buildServerCacheForChunk(ServerLevel level, ChunkPos chunkPos) {
        var dimension = level.dimension();

        if (CACHE.isChunkCached(dimension, chunkPos)) {
            return;
        }

        var registryAccess = level.registryAccess().registry(Registries.STRUCTURE).orElse(null);
        if (registryAccess == null) { return; }

        var structureManager = level.structureManager();

        structureManager.startsForStructure(chunkPos, structure -> true)
                .forEach(start -> {
                    if (start.isValid()) {
                        var structureId = registryAccess.getKeyOrNull(start.getStructure());
                        if (structureId == null) { return; }

                        var restriction = ARestrictionManager.STRUCTURE_INSTANCE.getRestriction(structureId);

                        if (restriction != null) {
                            var bb = start.getBoundingBox();

                            var minX = bb.minX() >> 4;
                            var minZ = bb.minZ() >> 4;
                            var maxX = bb.maxX() >> 4;
                            var maxZ = bb.maxZ() >> 4;

                            for (int x = minX; x <= maxX; x++) {
                                for (int z = minZ; z <= maxZ; z++) {
                                    CACHE.addEntry(dimension, ChunkPos.asLong(x, z), restriction.getId(), start, false);
                                }
                            }
                        }
                    }
                });

        CACHE.setScannedChunk(dimension, chunkPos);
    }

    public void clearCache() {
        CACHE.clearCache();
    }
}
