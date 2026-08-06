package com.alessandro.astages.infrastructure.hook.collision;

import com.alessandro.astages.AStages;
import com.alessandro.astages.api.event.world.EntityChangedChunkEvent;
import com.alessandro.astages.api.nullability.NotNullParams;
import com.alessandro.astages.engine.AStructureCollisionManager;
import com.alessandro.astages.infrastructure.networking.Networking;
import com.alessandro.astages.infrastructure.networking.packet.structure.SyncRestrictedStructuresS2C;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.ChunkPos;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.player.PlayerEvent;

@NotNullParams
@EventBusSubscriber(modid = AStages.MODID)
public class StructureServerEvents {
    @SubscribeEvent
    public static void onPlayerLoggedIn(PlayerEvent.PlayerLoggedInEvent event) {
        sendAABBsToPlayer(event.getEntity(), event.getEntity().chunkPosition());
    }

    @SubscribeEvent
    public static void onPlayerChangedChunk(EntityChangedChunkEvent event) {
        sendAABBsToPlayer(event.getEntity(), event.getNewChunkPos());
    }

    @SuppressWarnings("all")
    private static void sendAABBsToPlayer(Entity player, ChunkPos chunkPos) {
        if (player instanceof ServerPlayer serverPlayer && serverPlayer.connection != null) {
            var level = serverPlayer.serverLevel();
            AStructureCollisionManager.INSTANCE.buildServerCacheForChunk(level, chunkPos);

            var boxesToCollide = AStructureCollisionManager.INSTANCE.getCacheForChunk(level.dimension(), chunkPos);
            Networking.sendToPlayer(serverPlayer, new SyncRestrictedStructuresS2C(level.dimension(), chunkPos, boxesToCollide));
        }
    }
}