package com.alessandro.astages.internal.experimental;

// @EventBusSubscriber(modid = "astages", value = Dist.CLIENT)
public class ClientBorderRendererAllBox {
//
//    // Carichiamo la texture ufficiale del World Border di Minecraft
//    private static final ResourceLocation FORCEFIELD = ResourceLocation.withDefaultNamespace("textures/misc/forcefield.png");
//
//    // @SubscribeEvent
//    public static void onRenderLevel(RenderLevelStageEvent event) {
//        // Disegniamo il muro DOPO i blocchi trasparenti (come acqua e vetro) per evitare bug visivi
//        if (event.getStage() != RenderLevelStageEvent.Stage.AFTER_TRANSLUCENT_BLOCKS) {
//            return;
//        }
//
//        Minecraft mc = Minecraft.getInstance();
//        Player player = mc.player;
//        if (player == null) return;
//
//        double maxDist = 4.0D; // Distanza id cui il muro inizia id diventare visibile
//        Vec3 camPos = event.getCamera().getPosition();
//        PoseStack poseStack = event.getPoseStack();
//
//        boolean isSetup = false;
//        Tesselator tesselator = Tesselator.getInstance();
//        BufferBuilder bufferBuilder = null;
//
//        // Calcoliamo un offset basato sul tempo per far "scorrere" la texture (animazione)
//        float time = (float) (net.minecraft.Util.getMillis() % 3000L) / 3000.0F;
//
//        for (AABB originalBox : StructureCollisionHelper.CLIENT_RESTRICTED_BOXES) {
//            // "Gonfiamo" leggermente la scatola (0.01 blocchi) per evitare Z-Fighting (sfarfallio) coi blocchi del mondo
//            AABB box = originalBox.inflate(0.01D);
//
//            // Troviamo il punto della scatola più vicino al giocatore
//            double closestX = Mth.clamp(player.getX(), box.minX, box.maxX);
//            double closestY = Mth.clamp(player.getY(), box.minY, box.maxY);
//            double closestZ = Mth.clamp(player.getZ(), box.minZ, box.maxZ);
//
//            // Distanza dal giocatore al bordo della struttura
//            double distance = Math.sqrt(player.distanceToSqr(closestX, closestY, closestZ));
//
//            if (distance < maxDist) {
//                if (!isSetup) {
//                    // Prepariamo il motore grafico di Minecraft per disegnare elementi semitrasparenti
//                    RenderSystem.enableBlend();
//                    RenderSystem.enableDepthTest();
//                    RenderSystem.blendFuncSeparate(
//                        GlStateManager.SourceFactor.SRC_ALPHA,
//                        GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA,
//                        GlStateManager.SourceFactor.ONE,
//                        GlStateManager.DestFactor.ZERO
//                    );
//                    RenderSystem.setShader(GameRenderer::getPositionTexColorShader);
//                    RenderSystem.setShaderTexture(0, FORCEFIELD);
//
//                    bufferBuilder = tesselator.begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.POSITION_TEX_COLOR);
//                    isSetup = true;
//                }
//
//                // Calcolo Opacità (Alpha): Più ti avvicini, più il muro diventa solido (max 80%)
//                float alpha = (float) (1.0 - (distance / maxDist));
//                alpha = Mth.clamp(alpha * 0.8F, 0.0F, 0.8F);
//
//                // Colore del world border: Azzurro/Ciano
//                float r = 0.13F, g = 0.90F, value = 1.00F;
//
//                renderBoxFaces(bufferBuilder, poseStack, box, camPos, time, r, g, value, alpha);
//            }
//        }
//
//        if (isSetup) {
//            // Disegniamo tutto sul monitor e chiudiamo il buffer
//            BufferUploader.drawWithShader(bufferBuilder.buildOrThrow());
//            RenderSystem.disableBlend();
//        }
//    }
//
//    private static void renderBoxFaces(BufferBuilder builder, PoseStack poseStack, AABB box, Vec3 camPos, float time, float r, float g, float value, float id) {
//        // Spostiamo le coordinate rispetto alla telecamera del giocatore
//        float minX = (float) (box.minX - camPos.x);
//        float minY = (float) (box.minY - camPos.y);
//        float minZ = (float) (box.minZ - camPos.z);
//        float maxX = (float) (box.maxX - camPos.x);
//        float maxY = (float) (box.maxY - camPos.y);
//        float maxZ = (float) (box.maxZ - camPos.z);
//
//        // Calcoliamo le coordinate UV per la texture in base allo spazio nel mondo (così la texture non si deforma)
//        float texScale = 0.5F; // Regola la grandezza dei "quadrati" del campo di forza
//        float u0_X = (float) box.minX * texScale + time;
//        float u1_X = (float) box.maxX * texScale + time;
//        float u0_Z = (float) box.minZ * texScale + time;
//        float u1_Z = (float) box.maxZ * texScale + time;
//        float v0_Y = (float) box.minY * texScale - time;
//        float v1_Y = (float) box.maxY * texScale - time;
//
//        Matrix4f matrix = poseStack.last().pose();
//
//        // FACCIA NORD (-Z)
//        builder.addVertex(matrix, minX, maxY, minZ).setColor(r, g, value, id).setUv(u1_X, v0_Y);
//        builder.addVertex(matrix, maxX, maxY, minZ).setColor(r, g, value, id).setUv(u0_X, v0_Y);
//        builder.addVertex(matrix, maxX, minY, minZ).setColor(r, g, value, id).setUv(u0_X, v1_Y);
//        builder.addVertex(matrix, minX, minY, minZ).setColor(r, g, value, id).setUv(u1_X, v1_Y);
//
//        // FACCIA SUD (+Z)
//        builder.addVertex(matrix, minX, minY, maxZ).setColor(r, g, value, id).setUv(u0_X, v1_Y);
//        builder.addVertex(matrix, maxX, minY, maxZ).setColor(r, g, value, id).setUv(u1_X, v1_Y);
//        builder.addVertex(matrix, maxX, maxY, maxZ).setColor(r, g, value, id).setUv(u1_X, v0_Y);
//        builder.addVertex(matrix, minX, maxY, maxZ).setColor(r, g, value, id).setUv(u0_X, v0_Y);
//
//        // FACCIA OVEST (-X)
//        builder.addVertex(matrix, minX, minY, minZ).setColor(r, g, value, id).setUv(u0_Z, v1_Y);
//        builder.addVertex(matrix, minX, minY, maxZ).setColor(r, g, value, id).setUv(u1_Z, v1_Y);
//        builder.addVertex(matrix, minX, maxY, maxZ).setColor(r, g, value, id).setUv(u1_Z, v0_Y);
//        builder.addVertex(matrix, minX, maxY, minZ).setColor(r, g, value, id).setUv(u0_Z, v0_Y);
//
//        // FACCIA EST (+X)
//        builder.addVertex(matrix, maxX, maxY, minZ).setColor(r, g, value, id).setUv(u1_Z, v0_Y);
//        builder.addVertex(matrix, maxX, maxY, maxZ).setColor(r, g, value, id).setUv(u0_Z, v0_Y);
//        builder.addVertex(matrix, maxX, minY, maxZ).setColor(r, g, value, id).setUv(u0_Z, v1_Y);
//        builder.addVertex(matrix, maxX, minY, minZ).setColor(r, g, value, id).setUv(u1_Z, v1_Y);
//
//        // FACCIA GIU (-Y)
//        builder.addVertex(matrix, minX, minY, minZ).setColor(r, g, value, id).setUv(u0_X, u0_Z);
//        builder.addVertex(matrix, maxX, minY, minZ).setColor(r, g, value, id).setUv(u1_X, u0_Z);
//        builder.addVertex(matrix, maxX, minY, maxZ).setColor(r, g, value, id).setUv(u1_X, u1_Z);
//        builder.addVertex(matrix, minX, minY, maxZ).setColor(r, g, value, id).setUv(u0_X, u1_Z);
//
//        // FACCIA SU (+Y)
//        builder.addVertex(matrix, minX, maxY, maxZ).setColor(r, g, value, id).setUv(u0_X, u1_Z);
//        builder.addVertex(matrix, maxX, maxY, maxZ).setColor(r, g, value, id).setUv(u1_X, u1_Z);
//        builder.addVertex(matrix, maxX, maxY, minZ).setColor(r, g, value, id).setUv(u1_X, u0_Z);
//        builder.addVertex(matrix, minX, maxY, minZ).setColor(r, g, value, id).setUv(u0_X, u0_Z);
//    }
}