package com.alessandro.astages.infrastructure.mixin.structure;

import com.alessandro.astages.api.ALoader;
import com.alessandro.astages.api.event.world.EntityChangedChunkEvent;
import com.alessandro.astages.engine.collision.StructureCollisionManager;
import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.ArrayList;
import java.util.List;

@Mixin(Entity.class)
public abstract class AEntity {
    @Shadow public abstract ChunkPos chunkPosition();

    @Shadow public abstract BlockPos blockPosition();

    @ModifyExpressionValue(method = "collide", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/Level;getEntityCollisions(Lnet/minecraft/world/entity/Entity;Lnet/minecraft/world/phys/AABB;)Ljava/util/List;"))
    public List<VoxelShape> astages$collide(List<VoxelShape> original) {
        var entity = (Entity) (Object) this;

        if (entity instanceof Player player && !player.isSpectator()) {
            List<VoxelShape> newCollisions = new ArrayList<>(original);
            var level = player.level();

            if (level instanceof ServerLevel serverLevel) {
                newCollisions.addAll(StructureCollisionManager.SERVER_INSTANCE.getRestrictedShapesForChunk(serverLevel, chunkPosition(), player));
            } else {
                newCollisions.addAll(StructureCollisionManager.CLIENT_INSTANCE.getRestrictedShapesForChunks(level.dimension(), chunkPosition(), 1));
            }

            return newCollisions;
        }

        return original;
    }

    @Inject(method = "setPosRaw", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/ChunkPos;<init>(Lnet/minecraft/core/BlockPos;)V"))
    public void astages$onPlayerChangedChunk(double x, double y, double z, CallbackInfo ci) {
        ALoader.EVENT_BUS.post(new EntityChangedChunkEvent((Entity) (Object) this, new ChunkPos(blockPosition())));
    }
}