package com.alessandro.astages.internal.experimental;
//
//import com.mojang.blaze3d.systems.RenderSystem;
//import com.mojang.blaze3d.vertex.*;
//import net.minecraft.client.Minecraft;
//import net.minecraft.client.renderer.GameRenderer;
//import net.minecraft.resources.ResourceLocation;
//import net.minecraft.world.entity.player.Player;
//import net.minecraft.world.phys.AABB;
//import net.minecraft.world.phys.Vec3;
//import net.neoforged.api.distmarker.Dist;
//import net.neoforged.bus.api.SubscribeEvent;
//import net.neoforged.fml.common.EventBusSubscriber;
//import net.neoforged.neoforge.client.event.RenderLevelStageEvent;
//import org.joml.Matrix4f;
//
//@EventBusSubscriber(modid = "astages", value = Dist.CLIENT)
public class ClientBorderRendererOld {
//
//    private static final ResourceLocation FORCEFIELD = ResourceLocation.withDefaultNamespace("textures/misc/forcefield.png");
//    private static boolean hasAddedVertices = false; // Sicurezza anti-crash
//
//    @SubscribeEvent
//    public static void onRenderLevel(RenderLevelStageEvent event) {
//        if (event.getStage() != RenderLevelStageEvent.Stage.AFTER_TRANSLUCENT_BLOCKS) return;
//
//        Minecraft mc = Minecraft.getInstance();
//        Player player = mc.player;
//        if (player == null) return;
//
//        Vec3 camPos = event.getCamera().getPosition();
//        PoseStack poseStack = event.getPoseStack();
//        float time = (float) (net.minecraft.Util.getMillis() % 3000L) / 3000.0F;
//
//        Tesselator tesselator = Tesselator.getInstance();
//        BufferBuilder bufferBuilder = null;
//        boolean isSetup = false;
//        hasAddedVertices = false;
//
//        double shieldRadius = 8.0D; // Raggio dell'effetto "scudo"
//
//        for (AABB originalBox : StructureCollisionHelper.CLIENT_RESTRICTED_BOXES) {
//            AABB box = originalBox.inflate(0.01D);
//
//            // Se il player è troppo lontano dalla box, saltiamo tutto
//            if (!box.inflate(shieldRadius).contains(player.position())) continue;
//
//            if (!isSetup) {
//                RenderSystem.enableBlend();
//                RenderSystem.enableDepthTest();
//                RenderSystem.depthMask(false); // Impedisce al muro di coprire altri effetti trasparenti
//                RenderSystem.setShader(GameRenderer::getPositionTexColorShader);
//                RenderSystem.setShaderTexture(0, FORCEFIELD);
//                bufferBuilder = tesselator.begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.POSITION_TEX_COLOR);
//                isSetup = true;
//            }
//
//            renderFullShield(bufferBuilder, poseStack, box, player.position(), camPos, shieldRadius, time);
//        }
//
//        // CRITICO: Costruiamo il buffer solo se abbiamo effettivamente aggiunto dei quadratini
//        if (isSetup && hasAddedVertices) {
//            BufferUploader.drawWithShader(bufferBuilder.buildOrThrow());
//            RenderSystem.depthMask(true);
//            RenderSystem.disableBlend();
//        }
//    }
//
//    private static void renderFullShield(BufferBuilder builder, PoseStack poseStack, AABB box, Vec3 playerPos, Vec3 camPos, double radius, float time) {
//        Matrix4f matrix = poseStack.last().pose();
//        float r = 0.13F, g = 0.90F, value = 1.00F;
//        double step = 0.5D; // Dimensione quadratini della griglia
//
//        // Renderizziamo le 6 facce
//        // Asse X (Lati Est/Ovest)
//        drawFace(builder, matrix, camPos, playerPos, radius, box.minZ, box.maxZ, box.minY, box.maxY, (float)box.minX, false, time, r, g, value, step);
//        drawFace(builder, matrix, camPos, playerPos, radius, box.minZ, box.maxZ, box.minY, box.maxY, (float)box.maxX, false, time, r, g, value, step);
//
//        // Asse Z (Lati Nord/Sud)
//        drawFace(builder, matrix, camPos, playerPos, radius, box.minX, box.maxX, box.minY, box.maxY, (float)box.minZ, true, time, r, g, value, step);
//        drawFace(builder, matrix, camPos, playerPos, radius, box.minX, box.maxX, box.minY, box.maxY, (float)box.maxZ, true, time, r, g, value, step);
//
//        // Asse Y (Sopra/Sotto)
//        drawHorizontalFace(builder, matrix, camPos, playerPos, radius, box.minX, box.maxX, box.minZ, box.maxZ, (float)box.minY, time, r, g, value, step);
//        drawHorizontalFace(builder, matrix, camPos, playerPos, radius, box.minX, box.maxX, box.minZ, box.maxZ, (float)box.maxY, time, r, g, value, step);
//    }
//
//    private static void drawFace(BufferBuilder builder, Matrix4f matrix, Vec3 camPos, Vec3 playerPos, double radius, double sA, double eA, double sB, double eB, float fixed, boolean isZFixed, float time, float r, float g, float value, double step) {
//        for (double id = sA; id < eA; id += step) {
//            for (double b1 = sB; b1 < eB; b1 += step) {
//                double endA = Math.min(id + step, eA);
//                double endB = Math.min(b1 + step, eB);
//
//                double cX = isZFixed ? (id + endA) / 2 : fixed;
//                double cY = (b1 + endB) / 2;
//                double cZ = isZFixed ? fixed : (id + endA) / 2;
//
//                double dist = playerPos.distanceTo(new Vec3(cX, cY, cZ));
//                if (dist < radius) {
//                    float alpha = (float) (1.0 - (dist / radius)) * 0.7F;
//                    addQuad(builder, matrix, camPos, id, endA, b1, endB, fixed, isZFixed, time, r, g, value, alpha);
//                    hasAddedVertices = true;
//                }
//            }
//        }
//    }
//
//    private static void drawHorizontalFace(BufferBuilder builder, Matrix4f matrix, Vec3 camPos, Vec3 playerPos, double radius, double sX, double eX, double sZ, double eZ, float fixedY, float time, float r, float g, float value, double step) {
//        for (double x = sX; x < eX; x += step) {
//            for (double z = sZ; z < eZ; z += step) {
//                double endX = Math.min(x + step, eX);
//                double endZ = Math.min(z + step, eZ);
//                double dist = playerPos.distanceTo(new Vec3((x + endX) / 2, fixedY, (z + endZ) / 2));
//
//                if (dist < radius) {
//                    float alpha = (float) (1.0 - (dist / radius)) * 0.7F;
//                    float x0 = (float) (x - camPos.x), x1 = (float) (endX - camPos.x);
//                    float y = (float) (fixedY - camPos.y);
//                    float z0 = (float) (z - camPos.z), z1 = (float) (endZ - camPos.z);
//                    float u0 = (float)x * 0.5f + time, u1 = (float)endX * 0.5f + time;
//                    float v0 = (float)z * 0.5f - time, v1 = (float)endZ * 0.5f - time;
//
//                    builder.addVertex(matrix, x0, y, z0).setColor(r, g, value, alpha).setUv(u0, v0);
//                    builder.addVertex(matrix, x0, y, z1).setColor(r, g, value, alpha).setUv(u0, v1);
//                    builder.addVertex(matrix, x1, y, z1).setColor(r, g, value, alpha).setUv(u1, v1);
//                    builder.addVertex(matrix, x1, y, z0).setColor(r, g, value, alpha).setUv(u1, v0);
//                    hasAddedVertices = true;
//                }
//            }
//        }
//    }
//
//    private static void addQuad(BufferBuilder builder, Matrix4f matrix, Vec3 camPos, double id, double endA, double value, double endB, float fixed, boolean isZFixed, float time, float r, float g, float blue, float alpha) {
//        float x0 = (float) (isZFixed ? id : fixed) - (float)camPos.x;
//        float x1 = (float) (isZFixed ? endA : fixed) - (float)camPos.x;
//        float y0 = (float) value - (float)camPos.y;
//        float y1 = (float) endB - (float)camPos.y;
//        float z0 = (float) (isZFixed ? fixed : id) - (float)camPos.z;
//        float z1 = (float) (isZFixed ? fixed : endA) - (float)camPos.z;
//        float u0 = (float)id * 0.5f + time, u1 = (float)endA * 0.5f + time;
//        float v0 = (float)value * 0.5f - time, v1 = (float)endB * 0.5f - time;
//
//        builder.addVertex(matrix, x0, y1, z0).setColor(r, g, blue, alpha).setUv(u1, v0);
//        builder.addVertex(matrix, x1, y1, z1).setColor(r, g, blue, alpha).setUv(u0, v0);
//        builder.addVertex(matrix, x1, y0, z1).setColor(r, g, blue, alpha).setUv(u0, v1);
//        builder.addVertex(matrix, x0, y0, z0).setColor(r, g, blue, alpha).setUv(u1, v1);
//    }
}