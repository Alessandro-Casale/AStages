package com.alessandro.astages.api.graphic;

import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.renderer.GameRenderer;

public class RenderSystemUtils {
    public static void setupRenderSystemForTransparency() {
        RenderSystem.enableBlend();
        RenderSystem.defaultBlendFunc();
        RenderSystem.enableDepthTest();
        RenderSystem.depthMask(false);
        RenderSystem.enableCull(); // Avoid overlapping front and back faces
        RenderSystem.setShader(GameRenderer::getPositionTexColorShader);
        RenderSystem.setShaderTexture(0, Textures.FORCEFIELD);
    }

    public static void cleanupRenderSystemForTransparency() {
        RenderSystem.disableCull();
        RenderSystem.depthMask(true);
        RenderSystem.disableBlend();
    }
}
