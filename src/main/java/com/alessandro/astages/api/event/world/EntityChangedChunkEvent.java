package com.alessandro.astages.api.event.world;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.ChunkPos;
import net.neoforged.neoforge.event.entity.EntityEvent;

public class EntityChangedChunkEvent extends EntityEvent {
    private final ChunkPos newChunkPos;

    public EntityChangedChunkEvent(Entity entity, ChunkPos newChunkPos) {
        super(entity);
        this.newChunkPos = newChunkPos;
    }

    public ChunkPos getNewChunkPos() {
        return newChunkPos;
    }
}
