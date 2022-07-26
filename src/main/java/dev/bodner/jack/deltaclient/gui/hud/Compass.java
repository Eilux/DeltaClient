package dev.bodner.jack.deltaclient.gui.hud;

import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.DrawableHelper;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;

public class Compass extends DrawableHelper {
    private final MinecraftClient client = MinecraftClient.getInstance();
    private int scaledWidth = this.client.getWindow().getScaledWidth();
    private int scaledHeight = this.client.getWindow().getScaledHeight();

    private int start;
    private int end;
    private int width;
    private int yaw;
    private boolean text;

    private int testValue = findDegree(0,0);

    public Compass(){

        text = true;

        start = ((int) (scaledWidth * (1.5 / 4F)));
        end = ((int)(scaledWidth * (2.5 / 4F)));
        width = end-start;

        if (width>180){
            int trim = Math.abs(180-width)/2;
            start = start+trim;
            end = end-trim;
            width = end-start;
        }
    }

    public void update(){
        scaledWidth = this.client.getWindow().getScaledWidth();
        scaledHeight = this.client.getWindow().getScaledHeight();

        yaw = ((int) client.player.getHeadYaw() % 360);
        if (yaw < 0){
            yaw = 360+yaw;
        }

        start = ((int) (scaledWidth * (1.5 / 4F)));
        end = ((int)(scaledWidth * (2.5 / 4F)));
        width = end-start;

        if (width>180){
            int trim = Math.abs(180-width)/2;
            start = start+trim;
            end = end-trim;
            width = end-start;
        }

        //
        testValue = findDegree(0,0);
    }

    public void draw(MatrixStack matrices, int verticalOffset){
        this.update();
        RenderSystem.setShaderTexture(0,new Identifier("deltaclient","textures/gui/compass.png"));


        fill(matrices, start-1, 3+verticalOffset, end+1, 12+verticalOffset, 0x668c8c8c);
        for (int i = (width / -2)-1; i <= width / 2; i++) {
            int shiftedYaw = (yaw + i)%360;
            if (shiftedYaw < 0){
                shiftedYaw = shiftedYaw + 360;
            }

            if ((shiftedYaw) % 10 == 0) {
                drawVerticalLine(matrices, (scaledWidth / 2) + i, 2+verticalOffset, 13+verticalOffset, 0x66dedede);
            }
            //TEST
//            if (shiftedYaw == testValue){
//                drawVerticalLine(matrices, (scaledWidth / 2) + i, 2+verticalOffset, 13+verticalOffset, 0xFF3434eb);
//            }
            //TEST
            switch (shiftedYaw) {
                case 0:
                    drawTexture(matrices, scaledWidth/2 + i -2, 5+verticalOffset, 6, 13, 5, 7);
                    break;
                case 180:
                    drawTexture(matrices, scaledWidth/2 + i -2, 5+verticalOffset, 0, 13, 5, 7);
                    break;
                case 270:
                    drawTexture(matrices, scaledWidth/2 + i -2, 5+verticalOffset, 12, 13, 5, 7);
                    break;
                case 90:
                    drawTexture(matrices, scaledWidth/2 + i -2, 5+verticalOffset, 18, 13, 5, 7);
                    break;
            }
        }

        for(int i = start; i<=end; i++){
            drawTexture(matrices,i,2+verticalOffset, 3, 0, 1, 13);
        }
        drawTexture(matrices, start-3, 2+verticalOffset, 0, 0, 3, 13);
        drawTexture(matrices, end, 2+verticalOffset, 108, 0, 3, 13);
        drawTexture(matrices, (scaledWidth/2)-1, 2+verticalOffset, 54, 0, 3, 13);

        if (text){
            this.drawText(matrices,verticalOffset);
        }
    }

    private void drawText(MatrixStack matrices, int verticalOffset){
        this.update();
        TextRenderer textRenderer = this.client.textRenderer;
        textRenderer.drawWithShadow(matrices, yaw +"°", scaledWidth/2F - textRenderer.getWidth(String.valueOf(yaw))/2F,(int)(13 + textRenderer.fontHeight/2F)+verticalOffset, 0xFFFFFFFF);
        //TEST
//        textRenderer.drawWithShadow(matrices, testValue +"°", scaledWidth/2F - textRenderer.getWidth(String.valueOf(testValue))/2F,(int)(23 + textRenderer.fontHeight/2F)+verticalOffset, 0xFFFFFFFF);
        //TEST
    }

    public int getStart() {
        return start;
    }

    public int getEnd() {
        return end;
    }

    public int getWidth() {
        return width;
    }

    public int getYaw() {
        return yaw;
    }

    public int getHeight(){
        int height = 12;
        if(text) {height += this.client.textRenderer.fontHeight + 2;}
        return height;
    }

    public boolean getText(){
        return text;
    }

    public void renderText(boolean value){
        text = value;
    }

    public int findDegree(double xPos, double zPos){
        double[] cordA = {this.client.player.getPos().x, this.client.player.getPos().z};
        double[] cordB = {xPos, zPos};

        double x = cordB[0] - cordA[0];
        double y = cordB[1] - cordA[1];

        double degree = Math.toDegrees(Math.atan(y/x));

        //Q II
        if (x < 0 && y > 0){
            degree += 180;
        }

        //Q III
        if (x < 0 && y < 0){
            degree += 180;
        }

        //Q IV
        if (x > 0 && y < 0){
            degree += 360;
        }

        degree -= 90;

        degree %= 360;
        if (degree < 0){
            degree += 360;
        }

//        System.out.println(degree);

        return (int)degree;
    }


    @Deprecated
    public void renderOldCompass(MatrixStack matrices) {
        TextRenderer textRenderer = this.client.textRenderer;
        int yaw = ((int) this.client.player.getHeadYaw() % 360);
        if (yaw < 0){
            yaw = 360+yaw;
        }
        int width = ((int) (scaledWidth * (2.5 / 4F))) - ((int) (scaledWidth * (1.5 / 4F)));
        fill(matrices, ((int) (scaledWidth * (1.5 / 4F))), 2, ((int) (scaledWidth * (2.5 / 4F))), 12, 0x668c8c8c);
        for (int i = width / -2; i <= width / 2; i++) {
            if ((yaw + i) % 10 == 0) {
                drawVerticalLine(matrices, (scaledWidth / 2) + i, 2, 12, 0x66dedede);
            }
            switch (yaw + i) {
                case 360:
                case 0:
                    textRenderer.draw(matrices, "s", scaledWidth / 2F + i - 3, 3, 0xFFFFFFFF);
                    break;
                case 180:
                    textRenderer.draw(matrices, "n", scaledWidth / 2F + i - 3, 3, 0xFFFFFFFF);
                    break;
                case 270:
                    textRenderer.draw(matrices, "e", scaledWidth / 2F + i - 3, 3, 0xFFFFFFFF);
                    break;
                case 90:
                    textRenderer.draw(matrices, "w", scaledWidth / 2F + i - 3, 3, 0xFFFFFFFF);
                    break;
            }
        }

        drawHorizontalLine(matrices, ((int) (scaledWidth * (1.5 / 4F))) + 1, ((int) (scaledWidth * (2.5 / 4F))) - 1, 2, 0xFFFFFFFF);
        drawHorizontalLine(matrices, ((int) (scaledWidth * (1.5 / 4F))) + 1, ((int) (scaledWidth * (2.5 / 4F))) - 1, 12, 0xFFFFFFFF);
        drawVerticalLine(matrices, ((int) (scaledWidth * (1.5 / 4F))), 2, 12, 0xFFFFFFFF);
        drawVerticalLine(matrices, ((int) (scaledWidth * (2.5 / 4F))), 2, 12, 0xFFFFFFFF);
    }
}
