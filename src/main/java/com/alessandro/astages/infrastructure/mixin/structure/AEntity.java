package com.alessandro.astages.infrastructure.mixin.structure;

import com.alessandro.astages.api.ALoader;
import com.alessandro.astages.api.event.world.EntityChangedChunkEvent;
import com.alessandro.astages.engine.AClientStructureCollisionManager;
import com.alessandro.astages.engine.AStructureCollisionManager;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.ArrayList;
import java.util.List;

@Mixin(Entity.class)
public abstract class AEntity {
    @Shadow public abstract ChunkPos chunkPosition();

    @Shadow public abstract BlockPos blockPosition();

    @Inject(method = "collide", at = @At("HEAD"), cancellable = true)
    public void astages$collide(Vec3 vec, CallbackInfoReturnable<Vec3> cir) {
        var entity = (Entity) (Object) this;

        if (!(entity instanceof Player player) || player.isSpectator()) {
            return;
        }

        var level = player.level();
        var restrictedShapes = level instanceof ServerLevel serverLevel
            ? AStructureCollisionManager.INSTANCE.getRestrictedShapesForChunk(serverLevel, chunkPosition(), player)
            : AClientStructureCollisionManager.INSTANCE.getRestrictedShapesForChunks(level.dimension(), chunkPosition(), 1);

        if (restrictedShapes.isEmpty()) {
            return;
        }

        var aabb = player.getBoundingBox();
        List<VoxelShape> collisions = new ArrayList<>(level.getEntityCollisions(entity, aabb.expandTowards(vec)));
        collisions.addAll(restrictedShapes);

        Vec3 result = vec.lengthSqr() == 0.0 ? vec : Entity.collideBoundingBox(entity, vec, aabb, level, collisions);
        cir.setReturnValue(result);
    }

    @Inject(method = "setPosRaw", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/ChunkPos;<init>(Lnet/minecraft/core/BlockPos;)V"))
    public void astages$onPlayerChangedChunk(double x, double y, double z, CallbackInfo ci) {
        ALoader.EVENT_BUS.post(new EntityChangedChunkEvent((Entity) (Object) this, new ChunkPos(blockPosition())));
    }
}