package com.ned.shinyfinder;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;

public class ShinyFinderHudOverlay {
    public static void renderGameOverlayEvent(DrawContext graphics) {
        // Only show if scanner is enabled and we're in a world
        if (!ShinyFinderClientCore.isScannerEnabled() || MinecraftClient.getInstance().world == null || MinecraftClient.getInstance().player == null) {
            return;
        }

        MinecraftClient mc = MinecraftClient.getInstance();
        int windowWidth = mc.getWindow().getScaledWidth();
        int windowHeight = mc.getWindow().getScaledHeight();

        // Position in bottom-right corner like XRay (when not in debug mode)
        int x = windowWidth - 10;
        int y = windowHeight - 10;
        
        // Position our indicator above XRay (20 pixels above)
        y = y - 20;

        // Draw circle exactly like XRay - copy the exact approach
        // XRay uses: graphics.blit(RenderPipelines.GUI_TEXTURED, CIRCLE, x, y, 0f, 0f, 5, 5, 5, 5, 0xFF00FF00);
        // We'll draw a simple circle manually to match
        drawSimpleCircle(graphics, x, y, 0xFF00FF00);

        // Draw text exactly like XRay - using drawText with shadow
        String text = "Shiny Finder Active";
        int width = mc.textRenderer.getWidth(text);
        int textX = x - width - 5; // Position to the left of circle
        int textY = y - 2; // Slightly above circle
        graphics.drawText(mc.textRenderer, text, textX, textY, 0xFFFFFF, true); // WHITE text like XRay!
    }
    
    private static void drawSimpleCircle(DrawContext graphics, int x, int y, int color) {
        // Draw a simple 5x5 circle that looks like XRay's
        // Top row: 3 pixels
        graphics.fill(x + 1, y + 0, x + 4, y + 1, color);
        // Middle rows: 5 pixels each
        graphics.fill(x + 0, y + 1, x + 5, y + 2, color);
        graphics.fill(x + 0, y + 2, x + 5, y + 3, color);
        graphics.fill(x + 0, y + 3, x + 5, y + 4, color);
        // Bottom row: 3 pixels
        graphics.fill(x + 1, y + 4, x + 4, y + 5, color);
    }
}
