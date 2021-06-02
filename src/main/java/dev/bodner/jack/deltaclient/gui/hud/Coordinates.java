package dev.bodner.jack.deltaclient.gui.hud;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.DrawableHelper;
import net.minecraft.client.util.math.MatrixStack;

public class Coordinates extends DrawableHelper {
    private final MinecraftClient client = MinecraftClient.getInstance();

    public Coordinates(){}

    public void draw(MatrixStack matrices) {
        int x = 6;
        int y = 6;
        double xPos = this.client.player.getX();
        double yPos = this.client.player.getY();
        double zPos = this.client.player.getZ();
        TextRenderer textRenderer = this.client.textRenderer;
        String display = String.format("%.1f / %.1f / %.1f", xPos, yPos, zPos);
        textRenderer.drawWithShadow(matrices, display, x, y, 0xffffffff);
    }

}
