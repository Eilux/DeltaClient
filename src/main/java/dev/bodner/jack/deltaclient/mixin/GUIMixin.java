package dev.bodner.jack.deltaclient.mixin;

import com.google.common.collect.Lists;
import com.google.common.collect.Ordering;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.DrawableHelper;
import net.minecraft.client.gui.hud.InGameHud;
import net.minecraft.client.gui.screen.ingame.HandledScreen;
import net.minecraft.client.render.item.ItemRenderer;
import net.minecraft.client.texture.Sprite;
import net.minecraft.client.texture.StatusEffectSpriteManager;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffectUtil;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Identifier;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;

@Mixin(InGameHud.class)
public abstract class GUIMixin extends DrawableHelper {

    @Shadow
    @Final
    private MinecraftClient client;
    @Shadow
    private int scaledWidth;

    @Shadow
    private int scaledHeight;

    @Shadow
    @Final
    private ItemRenderer itemRenderer;

    @Shadow
    protected abstract PlayerEntity getCameraPlayer();

    @Shadow
    private int ticks;

    /**
     * @author Eilux
     * @reason asdfjkl
     */
    @Overwrite
    public void renderStatusEffectOverlay(MatrixStack matrices) {
        TextRenderer textRenderer = this.client.textRenderer;
        Collection<StatusEffectInstance> statusCollection = this.client.player.getStatusEffects();

        if (!this.client.options.debugEnabled) {
            int baseX = this.scaledWidth - 6;
            int baseY = 6;
            int offsetX = 0;
            int offsetY = 0;

            if (!statusCollection.isEmpty()) {
                RenderSystem.enableBlend();
                RenderSystem.color4f(1.0F, 1.0F, 1.0F, 1.0F);

                this.client.getTextureManager().bindTexture(HandledScreen.BACKGROUND_TEXTURE);
                StatusEffectSpriteManager statusEffectSpriteManager = this.client.getStatusEffectSpriteManager();
                List<Runnable> list = Lists.newArrayListWithExpectedSize(statusCollection.size());
                Iterator<StatusEffectInstance> ordered = Ordering.natural().reverse().sortedCopy(statusCollection).iterator();


                for (StatusEffectInstance instance : Ordering.natural().reverse().sortedCopy(statusCollection)) {
                    if (instance.shouldShowIcon()) {

                        Sprite sprite = statusEffectSpriteManager.getSprite(instance.getEffectType());

                        String string2 = StatusEffectUtil.durationToString(instance, 1.0F);

                        int finalOffsetX = offsetX;
                        int finalOffsetY = offsetY;
                        list.add(() -> {
                            this.client.getTextureManager().bindTexture(sprite.getAtlas().getId());
                            drawSprite(matrices, baseX - finalOffsetX - 24, baseY + finalOffsetY, this.getZOffset(), 18, 18, sprite);
                            textRenderer.drawWithShadow(matrices, string2, baseX - finalOffsetX - 24, baseY + finalOffsetY + 20, 0xffffffff);
                        });

                        offsetX = offsetX + 30;
                        if (offsetX >= this.scaledWidth * (1 / 4F)) {
                            offsetX = 0;
                            offsetY = offsetY + 36;
                        }
                    }
                }
                list.forEach(Runnable::run);
            }
        }
    }

    @Inject(method = "render", at = @At("TAIL"))
    public void render(MatrixStack matrices, float tickDelta, CallbackInfo ci) {
        if (!this.client.options.debugEnabled) {
            renderCords(matrices);
            renderNewCompass(matrices);
        }
        renderArmor(matrices);
    }

    public void renderCords(MatrixStack matrices) {
        int x = 6;
        int y = 6;
        double xPos = this.client.player.getX();
        double yPos = this.client.player.getY();
        double zPos = this.client.player.getZ();
        TextRenderer textRenderer = this.client.textRenderer;
        String display = String.format("%.1f / %.1f / %.1f", xPos, yPos, zPos);
        textRenderer.drawWithShadow(matrices, display, x, y, 0xffffffff);
    }

    public void renderArmor(MatrixStack matrixStack) {
        int x = this.scaledWidth - 18;
        int y = this.scaledHeight - 18;
        ItemStack headSlot = this.client.player.inventory.getArmorStack(3);
        ItemStack chestSlot = this.client.player.inventory.getArmorStack(2);
        ItemStack legSlot = this.client.player.inventory.getArmorStack(1);
        ItemStack bootSlot = this.client.player.inventory.getArmorStack(0);

        if (headSlot.isEmpty()) {
            matrixStack.push();
            this.client.getTextureManager().bindTexture(new Identifier("textures/item/empty_armor_slot_helmet.png"));
            drawTexture(matrixStack, x, y - 54, 0, 0, 16, 16, 16, 16);
        }
        if (chestSlot.isEmpty()) {
            matrixStack.push();
            this.client.getTextureManager().bindTexture(new Identifier("textures/item/empty_armor_slot_chestplate.png"));
            drawTexture(matrixStack, x, y - 36, 0, 0, 16, 16, 16, 16);
        }
        if (legSlot.isEmpty()) {
            matrixStack.push();
            this.client.getTextureManager().bindTexture(new Identifier("textures/item/empty_armor_slot_leggings.png"));
            drawTexture(matrixStack, x, y - 18, 0, 0, 16, 16, 16, 16);
        }
        if (bootSlot.isEmpty()) {
            matrixStack.push();
            this.client.getTextureManager().bindTexture(new Identifier("textures/item/empty_armor_slot_boots.png"));
            drawTexture(matrixStack, x, y, 0, 0, 16, 16, 16, 16);
        }

        this.itemRenderer.renderInGuiWithOverrides(headSlot, x, y - 54);
        this.itemRenderer.renderInGuiWithOverrides(chestSlot, x, y - 36);
        this.itemRenderer.renderInGuiWithOverrides(legSlot, x, y - 18);
        this.itemRenderer.renderInGuiWithOverrides(bootSlot, x, y);

        this.itemRenderer.renderGuiItemOverlay(client.textRenderer, headSlot, x, y - 54);
        this.itemRenderer.renderGuiItemOverlay(client.textRenderer, chestSlot, x, y - 36);
        this.itemRenderer.renderGuiItemOverlay(client.textRenderer, legSlot, x, y - 18);
        this.itemRenderer.renderGuiItemOverlay(client.textRenderer, bootSlot, x, y);

        this.itemRenderer.apply(this.client.getResourceManager());
    }
    public void renderNewCompass(MatrixStack matrices){
        this.client.getTextureManager().bindTexture(new Identifier("deltaclient","textures/gui/compass.png"));
        TextRenderer textRenderer = this.client.textRenderer;

        int yaw = ((int) this.client.player.getHeadYaw() % 360);
        if (yaw < 0){
            yaw = 360+yaw;
        }
        int start = ((int) (scaledWidth * (1.5 / 4F)));
        int end = ((int)(scaledWidth * (2.5 / 4F)));
        int width = end-start;
        fill(matrices, start-1, 3, end+1, 12, 0x668c8c8c);
        for (int i = (width / -2)-1; i <= width / 2; i++) {
            int shiftedYaw = (yaw + i)%360;
            if (shiftedYaw < 0){
                shiftedYaw = shiftedYaw + 360;
            }

            if ((shiftedYaw) % 10 == 0) {
                drawVerticalLine(matrices, (scaledWidth / 2) + i, 2, 13, 0x66dedede);
            }
            switch (shiftedYaw) {
                case 0:
                    drawTexture(matrices, scaledWidth/2 + i -2, 5, 6, 13, 5, 7);
                    break;
                case 180:
                    drawTexture(matrices, scaledWidth/2 + i -2, 5, 0, 13, 5, 7);
                    break;
                case 270:
                    drawTexture(matrices, scaledWidth/2 + i -2, 5, 12, 13, 5, 7);
                    break;
                case 90:
                    drawTexture(matrices, scaledWidth/2 + i -2, 5, 18, 13, 5, 7);
                    break;
            }
        }

        for(int i = start; i<=end; i++){
            drawTexture(matrices,i,2, 3, 0, 1, 13);
        }
        drawTexture(matrices, start-3, 2, 0, 0, 3, 13);
        drawTexture(matrices, end, 2, 108, 0, 3, 13);
        drawTexture(matrices, (scaledWidth/2)-1, 2, 54, 0, 3, 13);

        textRenderer.drawWithShadow(matrices, yaw +"°", scaledWidth/2F - textRenderer.getWidth(String.valueOf(yaw))/2F,(int)(13 + textRenderer.fontHeight/2F), 0xFFFFFFFF);
    }

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
