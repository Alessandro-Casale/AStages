package com.alessandro.astages.api.base;

import com.alessandro.astages.api.misc.Twin;
import com.alessandro.astages.api.nullability.NotNullParams;
import com.alessandro.astages.api.nullability.Nullable;
import it.unimi.dsi.fastutil.longs.*;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import it.unimi.dsi.fastutil.objects.ObjectList;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.Level;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@NotNullParams
public class StructureCollisionCache<T> {
    private final Map<ResourceKey<Level>, LongSet> scannedChunks = new ConcurrentHashMap<>();
    private final Map<ResourceKey<Level>, Long2ObjectMap<ObjectList<Twin<String, T>>>> dimensionRelatedCaches = new ConcurrentHashMap<>();

    private LongSet getScannedChunksForDimension(ResourceKey<Level> dimension) {
        return scannedChunks.computeIfAbsent(dimension, k ->
            LongSets.synchronize(new LongOpenHashSet())
        );
    }

    private Long2ObjectMap<ObjectList<Twin<String, T>>> getCacheForDimension(ResourceKey<Level> dimension) {
        return dimensionRelatedCaches.computeIfAbsent(dimension, k ->
            Long2ObjectMaps.synchronize(new Long2ObjectOpenHashMap<>())
        );
    }

    public void addEntry(ResourceKey<Level> dimension, long chunkPos, String restrictionId, T start, boolean ignoreCachedChunks) {
        var dimCache = getCacheForDimension(dimension);
        Twin<String, T> newEntry = new Twin<>(restrictionId, start);

        if (!ignoreCachedChunks && isChunkCached(dimension, chunkPos)) {
            return;
        }

        dimCache.compute(chunkPos, (key, list) -> {
            if (list == null) {
                list = new ObjectArrayList<>(2);
            }

            if (!list.contains(newEntry)) {
                list.add(newEntry);
            }

            return list;
        });
    }

    public List<Twin<String, T>> getTwinsFor(ResourceKey<Level> dimension, long pos) {
        var cache = dimensionRelatedCaches.get(dimension);

        if (cache != null) {
            var result = cache.get(pos);
            if (result != null) { return result; }
        }

        return Collections.emptyList();
    }

    public @Nullable Long2ObjectMap<ObjectList<Twin<String, T>>> getInternalCacheForDimension(ResourceKey<Level> dimension) {
        return dimensionRelatedCaches.get(dimension);
    }

    public void setScannedChunk(ResourceKey<Level> dimension, ChunkPos pos) {
        getScannedChunksForDimension(dimension).add(pos.toLong());
    }

    public boolean isChunkCached(ResourceKey<Level> dimension, ChunkPos pos) {
        return isChunkCached(dimension, pos.toLong());
    }

    public boolean isChunkCached(ResourceKey<Level> dimension, long pos) {
        var cache = scannedChunks.get(dimension);
        return cache != null && cache.contains(pos);
    }

    public boolean isValueAlreadyCached(ResourceKey<Level> dimension, long pos, T value) {
        for (var twin : getTwinsFor(dimension, pos)) {
            if (twin.value().equals(value)) { return true; }
        }

        return false;
    }

    public void clearCache() {
        scannedChunks.clear();
        dimensionRelatedCaches.clear();
    }
}