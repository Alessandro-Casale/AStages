package com.alessandro.astages.infrastructure.hook.renderer;

import com.alessandro.astages.AStages;
import com.alessandro.astages.api.graphic.RenderSystemUtils;
import com.alessandro.astages.api.nullability.NotNullParams;
import com.alessandro.astages.engine.collision.StructureCollisionManager;
import com.mojang.blaze3d.vertex.*;
import net.minecraft.Util;
import net.minecraft.client.Minecraft;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.RenderLevelStageEvent;
import org.joml.Matrix4f;

@NotNullParams
@EventBusSubscriber(modid = AStages.MODID, value = Dist.CLIENT)
public class ClientBorderRenderer {
    private static boolean hasAddedVertices = false;

    @SubscribeEvent
    public static void onRenderLevel(RenderLevelStageEvent event) {
        if (event.getStage() != RenderLevelStageEvent.Stage.AFTER_TRANSLUCENT_BLOCKS) { return; }

        Minecraft mc = Minecraft.getInstance();
        Player player = mc.player;
        if (player == null) return;

        var dimension = player.level().dimension();
        Vec3 camPos = event.getCamera().getPosition();
        PoseStack poseStack = event.getPoseStack();
        float time = (float) (Util.getMillis() % 3000L) / 3000.0F;

        Tesselator tesselator = Tesselator.getInstance();
        BufferBuilder bufferBuilder = null;
        boolean isSetup = false;
        hasAddedVertices = false;

        double shieldRadius = 10.0D;

        for (AABB originalBox : StructureCollisionManager.CLIENT_INSTANCE.getRestrictedAABBsForChunks(dimension, player.chunkPosition(), 1)) {
            if (!originalBox.inflate(shieldRadius).contains(player.position())) continue;

            if (!isSetup) {
                RenderSystemUtils.setupRenderSystemForTransparency();
                bufferBuilder = tesselator.begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.POSITION_TEX_COLOR);
                isSetup = true;
            }

            renderCleanShield(bufferBuilder, poseStack, originalBox.inflate(0.01D), player.position(), camPos, shieldRadius, time);
        }

        if (isSetup && hasAddedVertices) {
            BufferUploader.drawWithShader(bufferBuilder.buildOrThrow());
            RenderSystemUtils.cleanupRenderSystemForTransparency();
        }
    }

    private static void renderCleanShield(BufferBuilder builder, PoseStack poseStack, AABB box, Vec3 pPos, Vec3 cPos, double rad, float time) {
        Matrix4f matrix = poseStack.last().pose();
        float r = 0.13F, g = 0.90F, b = 1.00F;
        double step = 0.5D;

        // Z (North/South)
        drawFace(builder, matrix, cPos, pPos, rad, box.minX, box.maxX, box.minY, box.maxY, (float)box.minZ, true, time, r, g, b, step, false);
        drawFace(builder, matrix, cPos, pPos, rad, box.minX, box.maxX, box.minY, box.maxY, (float)box.maxZ, true, time, r, g, b, step, true);

        // X (Est/West)
        drawFace(builder, matrix, cPos, pPos, rad, box.minZ + 0.001, box.maxZ - 0.001, box.minY + 0.001, box.maxY - 0.001, (float)box.minX, false, time, r, g, b, step, true);
        drawFace(builder, matrix, cPos, pPos, rad, box.minZ + 0.001, box.maxZ - 0.001, box.minY + 0.001, box.maxY - 0.001, (float)box.maxX, false, time, r, g, b, step, false);

        // Y (Up/Down)
        drawHorizontalFace(builder, matrix, cPos, pPos, rad, box.minX + 0.001, box.maxX - 0.001, box.minZ + 0.001, box.maxZ - 0.001, (float)box.minY, time, r, g, b, step, true);
        drawHorizontalFace(builder, matrix, cPos, pPos, rad, box.minX + 0.001, box.maxX - 0.001, box.minZ + 0.001, box.maxZ - 0.001, (float)box.maxY, time, r, g, b, step, false);
    }

    private static void drawFace(BufferBuilder builder, Matrix4f matrix, Vec3 camPos, Vec3 pPos, double rad, double sA, double eA, double sB, double eB, float fixed, boolean isZ, float time, float r, float g, float b, double step, boolean reverse) {
        for (double a = sA; a < eA; a += step) {
            for (double b1 = sB; b1 < eB; b1 += step) {
                double endA = Math.min(a + step, eA);
                double endB = Math.min(b1 + step, eB);
                double cX = isZ ? (a + endA) / 2 : fixed;
                double cY = (b1 + endB) / 2;
                double cZ = isZ ? fixed : (a + endA) / 2;

                double dist = pPos.distanceTo(new Vec3(cX, cY, cZ));
                if (dist < rad) {
                    float alpha = (float) (1.0 - (dist / rad)) * 0.8F;
                    addQuad(builder, matrix, camPos, a, endA, b1, endB, fixed, isZ, time, r, g, b, alpha, reverse);
                    hasAddedVertices = true;
                }
            }
        }
    }

    private static void drawHorizontalFace(BufferBuilder builder, Matrix4f matrix, Vec3 cam, Vec3 pPos, double rad, double sX, double eX, double sZ, double eZ, float fY, float t, float r, float g, float b, double step, boolean rev) {
        for (double x = sX; x < eX; x += step) {
            for (double z = sZ; z < eZ; z += step) {
                double endX = Math.min(x + step, eX);
                double endZ = Math.min(z + step, eZ);
                double dist = pPos.distanceTo(new Vec3((x+endX)/2, fY, (z+endZ)/2));

                if (dist < rad) {
                    float alpha = (float) (1.0 - (dist / rad)) * 0.8F;
                    float x0 = (float) (x - cam.x), x1 = (float) (endX - cam.x);
                    float y = (float) (fY - cam.y);
                    float z0 = (float) (z - cam.z), z1 = (float) (endZ - cam.z);
                    float u0 = (float)x * 0.5f + t, u1 = (float)endX * 0.5f + t;
                    float v0 = (float)z * 0.5f - t, v1 = (float)endZ * 0.5f - t;

                    if (rev) {
                        builder.addVertex(matrix, x0, y, z0).setColor(r, g, b, alpha).setUv(u0, v0);
                        builder.addVertex(matrix, x1, y, z0).setColor(r, g, b, alpha).setUv(u1, v0);
                        builder.addVertex(matrix, x1, y, z1).setColor(r, g, b, alpha).setUv(u1, v1);
                        builder.addVertex(matrix, x0, y, z1).setColor(r, g, b, alpha).setUv(u0, v1);
                    } else {
                        builder.addVertex(matrix, x0, y, z0).setColor(r, g, b, alpha).setUv(u0, v0);
                        builder.addVertex(matrix, x0, y, z1).setColor(r, g, b, alpha).setUv(u0, v1);
                        builder.addVertex(matrix, x1, y, z1).setColor(r, g, b, alpha).setUv(u1, v1);
                        builder.addVertex(matrix, x1, y, z0).setColor(r, g, b, alpha).setUv(u1, v0);
                    }
                    hasAddedVertices = true;
                }
            }
        }
    }

    private static void addQuad(BufferBuilder builder, Matrix4f matrix, Vec3 cam, double a, double eA, double b, double eB, float f, boolean isZ, float t, float r, float g, float blue, float alpha, boolean rev) {
        float x0 = (float) (isZ ? a : f) - (float)cam.x;
        float x1 = (float) (isZ ? eA : f) - (float)cam.x;
        float y0 = (float) b - (float)cam.y;
        float y1 = (float) eB - (float)cam.y;
        float z0 = (float) (isZ ? f : a) - (float)cam.z;
        float z1 = (float) (isZ ? f : eA) - (float)cam.z;
        float u0 = (float) a * 0.5f + t, u1 = (float)eA * 0.5f + t;
        float v0 = (float) b * 0.5f - t, v1 = (float)eB * 0.5f - t;

        if (rev) {
            builder.addVertex(matrix, x0, y1, z0).setColor(r, g, blue, alpha).setUv(u1, v0);
            builder.addVertex(matrix, x0, y0, z0).setColor(r, g, blue, alpha).setUv(u1, v1);
            builder.addVertex(matrix, x1, y0, z1).setColor(r, g, blue, alpha).setUv(u0, v1);
            builder.addVertex(matrix, x1, y1, z1).setColor(r, g, blue, alpha).setUv(u0, v0);
        } else {
            builder.addVertex(matrix, x0, y1, z0).setColor(r, g, blue, alpha).setUv(u1, v0);
            builder.addVertex(matrix, x1, y1, z1).setColor(r, g, blue, alpha).setUv(u0, v0);
            builder.addVertex(matrix, x1, y0, z1).setColor(r, g, blue, alpha).setUv(u0, v1);
            builder.addVertex(matrix, x0, y0, z0).setColor(r, g, blue, alpha).setUv(u1, v1);
        }
    }
}