package com.alessandro.astages.infrastructure.capability;

import com.alessandro.astages.api.nullability.NotNullParamsAndMethodsReturn;
import com.mojang.serialization.Codec;
import it.unimi.dsi.fastutil.longs.Long2ObjectOpenHashMap;
import net.minecraft.core.UUIDUtil;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@NotNullParamsAndMethodsReturn
public record BlockOwner(Long2ObjectOpenHashMap<UUID> blockMap) {
    public BlockOwner() {
        this(new Long2ObjectOpenHashMap<>());
    }

    public static final Codec<BlockOwner> CODEC = Codec.unboundedMap(Codec.STRING, UUIDUtil.CODEC)
        .xmap(
            stringMap -> {
                Long2ObjectOpenHashMap<UUID> longMap = new Long2ObjectOpenHashMap<>();
                stringMap.forEach((k, v) -> longMap.put(Long.parseLong(k), v));
                return new BlockOwner(longMap);
            },
            data -> {
                Map<String, UUID> stringMap = new HashMap<>();
                data.blockMap.long2ObjectEntrySet().fastForEach(entry ->
                    stringMap.put(String.valueOf(entry.getLongKey()), entry.getValue())
                );
                return stringMap;
            }
        );
}
