package com.alessandro.astages.internal.legacy.renderer;

import com.alessandro.astages.api.develop.UnderDevelopment;

@Deprecated(forRemoval=true)
@UnderDevelopment
public class ARenderer {
//        private static VertexBuffer vertexBuffer;
//        public static boolean requestRefresh = true;
//
//        public static final Set<BoundingBox> LIST = new HashSet<>();
//
//        public static void addBoundingBox(BoundingBox aabb) {
//            LIST.add(aabb);
//            requestRefresh = true;
//        }
//
//        ////////////////////////////////////////////////////////////////////////////////
//        //                            Rendering Methods                               //
//        ////////////////////////////////////////////////////////////////////////////////
//        public static void renderBlocks(PoseStack matrix, Matrix4f projection) {
//            if (vertexBuffer == null || requestRefresh) {
//                requestRefresh = false;
//                vertexBuffer = new VertexBuffer(VertexBuffer.Usage.STATIC);
//
//                Tesselator tesselator = Tesselator.getInstance();
//                BufferBuilder buffer = tesselator.getBuilder();
//                buffer.begin(VertexFormat.Mode.DEBUG_LINES, DefaultVertexFormat.POSITION_COLOR);
//
//                for (var aabb : LIST) {
//                    LevelRenderer.renderLineBox(buffer, aabb.minX(), aabb.minY(), aabb.minZ(), aabb.maxX(), aabb.maxY(), aabb.maxZ(), 0f, 1f, 0f, 1f);
//                }
//
//                // LevelRenderer.renderLineBox(buffer, -2315, 70, -2205, -2311, 71, -2200, 0f, 1f, 1f, 1f);
//
//                vertexBuffer.bind();
//                vertexBuffer.upload(buffer.end());
//                VertexBuffer.unbind();
//            }
//
//            if (vertexBuffer != null) {
//                Vec3 view = Minecraft.getInstance().getEntityRenderDispatcher().camera.getPosition();
//
//                GL11.glEnable(GL11.GL_BLEND);
//                GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
//                GL11.glEnable(GL11.GL_LINE_SMOOTH);
//                GL11.glDisable(GL11.GL_DEPTH_TEST);
//
//                RenderSystem.setShader(GameRenderer::getPositionColorShader);
//
//                matrix.pushPose();
//                matrix.translate(-view.x, -view.y, -view.z);
//
//                vertexBuffer.bind();
//                vertexBuffer.drawWithShader(matrix.last().pose(), new Matrix4f(projection), Objects.requireNonNull(RenderSystem.getShader()));
//                VertexBuffer.unbind();
//                matrix.popPose();
//
//                GL11.glEnable(GL11.GL_DEPTH_TEST);
//                GL11.glDisable(GL11.GL_BLEND);
//                GL11.glDisable(GL11.GL_LINE_SMOOTH);
//            }
//        }

}
