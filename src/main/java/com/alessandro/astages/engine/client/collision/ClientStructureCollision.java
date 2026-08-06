package com.alessandro.astages.engine.client.collision;

import com.alessandro.astages.api.base.StructureCollisionCache;
import com.alessandro.astages.api.holder.AClientHolder;
import com.alessandro.astages.api.misc.Twin;
import com.alessandro.astages.api.nullability.NotNullParams;
import com.alessandro.astages.api.util.AStagesClientUtils;
import com.alessandro.astages.engine.AClientRestrictionManager;
import com.alessandro.astages.engine.store.Attributes;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.levelgen.structure.BoundingBox;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;

import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@NotNullParams
public class ClientStructureCollision {
    private final StructureCollisionCache<AABB> AABB_CACHE = new StructureCollisionCache<>();
    private final StructureCollisionCache<VoxelShape> SHAPE_CACHE = new StructureCollisionCache<>();

    public void populateClientCacheForChunk(ResourceKey<Level> dimension, ChunkPos chunkPos, List<Twin<String, BoundingBox>> twins) {
        external: for (var twin : twins) {
            var bb = twin.value();

            var minX = bb.minX() >> 4;
            var minZ = bb.minZ() >> 4;
            var maxX = bb.maxX() >> 4;
            var maxZ = bb.maxZ() >> 4;

            for (int x = minX; x <= maxX; x++) {
                for (int z = minZ; z <= maxZ; z++) {
                    var pos = ChunkPos.asLong(x, z);
                    var aabb = AABB.of(twin.value());

                    if (AABB_CACHE.isValueAlreadyCached(dimension, pos, aabb)) {
                        continue external;
                    }

                    AABB_CACHE.addEntry(dimension, pos, twin.id(), aabb, false);
                    SHAPE_CACHE.addEntry(dimension, pos, twin.id(), Shapes.create(aabb), false);
                }
            }
        }

        AABB_CACHE.setScannedChunk(dimension, chunkPos);
        SHAPE_CACHE.setScannedChunk(dimension, chunkPos);
    }

    public Set<AABB> getRestrictedAABBsForChunks(ResourceKey<Level> dimension, ChunkPos center, int range) {
        var cache = AABB_CACHE.getInternalCacheForDimension(dimension);
        if (cache == null) { return Collections.emptySet(); }

        var aabbs = new HashSet<AABB>();

        for (int x = -range; x <= range; x++) {
            for (int z = -range; z <= range; z++) {
                var chunkPos = ChunkPos.asLong(center.x + x, center.z + z);
                var twins = cache.get(chunkPos);
                if (twins == null) { continue; }

                for (var twin : twins) {
                    var restrictionId = twin.id();
                    var restriction = AClientRestrictionManager.STRUCTURE_INSTANCE.getRestriction(restrictionId);
                    if (restriction != null && restriction.isDisabled(Attributes.ENTERING) && !AStagesClientUtils.hasStage(AClientHolder.serverAndPlayer(), restriction.getStage())) {
                        aabbs.add(twin.value());
                    }
                }
            }
        }

        return aabbs;
    }

    public Set<VoxelShape> getRestrictedShapesForChunks(ResourceKey<Level> dimension, ChunkPos center, int range) {
        var cache = SHAPE_CACHE.getInternalCacheForDimension(dimension);
        if (cache == null) { return Collections.emptySet(); }

        var shapes = new HashSet<VoxelShape>();

        for (int x = -range; x <= range; x++) {
            for (int z = -range; z <= range; z++) {
                var chunkPos = ChunkPos.asLong(center.x + x, center.z + z);
                var twins = cache.get(chunkPos);
                if (twins == null) { continue; }

                for (var twin : twins) {
                    var restrictionId = twin.id();
                    var restriction = AClientRestrictionManager.STRUCTURE_INSTANCE.getRestriction(restrictionId);
                    if (restriction != null && restriction.isDisabled(Attributes.ENTERING) && !AStagesClientUtils.hasStage(AClientHolder.serverAndPlayer(), restriction.getStage())) {
                        shapes.add(twin.value());
                    }
                }
            }
        }

        return shapes;
    }

    public void clearCache() {
        AABB_CACHE.clearCache();
        SHAPE_CACHE.clearCache();
    }
}
