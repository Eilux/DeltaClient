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
import net.minecraft.client.render.model.json.ModelTransformation;
import net.minecraft.client.texture.Sprite;
import net.minecraft.client.texture.StatusEffectSpriteManager;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.data.dev.NbtProvider;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffectUtil;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.IntArrayTag;
import net.minecraft.nbt.NbtHelper;
import net.minecraft.text.Text;
import net.minecraft.text.TextColor;
import net.minecraft.util.ChatUtil;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.MathHelper;
import org.lwjgl.system.CallbackI;
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

import static net.minecraft.client.gui.DrawableHelper.drawSprite;

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

//        int fireTicks = this.client.player.getFireTicks();
//        CompoundTag tag = this.client.player.toTag(new CompoundTag());
//        textRenderer.drawWithShadow(matrices,String.valueOf(tag.get("Fire")),this.scaledWidth/2F,6,0xffffffff);

        if (!this.client.options.debugEnabled) {
            int baseX = this.scaledWidth - 6;
            int baseY = 6;
            int offsetX = 0;
            int offsetY = 0;

//            if(fireTicks>0){
//                this.client.getTextureManager().bindTexture(new Identifier("deltaclient","textures/mob_effect/burning.png"));
//                String burningString = ChatUtil.ticksToString(fireTicks);
//                drawTexture(matrices, baseX - offsetX - 24, baseY + offsetY, 0, 0, 18, 18);
//                textRenderer.drawWithShadow(matrices, burningString, baseX - offsetX - 24, baseY + offsetY + 20, 0xffffffff);
//                offsetX += 30;
//            }

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
            renderCompass(matrices);
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

    public void renderCompass(MatrixStack matrices) {
        TextRenderer textRenderer = this.client.textRenderer;
        int yaw = ((int) this.client.player.getHeadYaw() % 360);
        if (yaw < 0){
            yaw = 360+yaw;
        }
//        textRenderer.drawWithShadow(matrices,String.valueOf(this.client.player.getHeadYaw()),scaledWidth/2F,30,0xFFFFFFFF);
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
//        for (int j = 0; (width / 2) + j >= 0; j--) {
//            if ((yaw + j) % 10 == 0) {
//                drawVerticalLine(matrices, (scaledWidth / 2) + j, 2, 12, 0x66dedede);
//            }
//            switch (yaw + j) {
//                case 360:
//                case 0:
//                    textRenderer.draw(matrices, "s", scaledWidth / 2F + j - 3, 3, 0xFFFFFFFF);
//                    break;
//                case 180:
//                    textRenderer.draw(matrices, "n", scaledWidth / 2F + j - 3, 3, 0xFFFFFFFF);
//                    break;
//                case 270:
//                    textRenderer.draw(matrices, "e", scaledWidth / 2F + j - 3, 3, 0xFFFFFFFF);
//                    break;
//                case 90:
//                    textRenderer.draw(matrices, "w", scaledWidth / 2F + j - 3, 3, 0xFFFFFFFF);
//                    break;
//            }
//        }

        drawHorizontalLine(matrices, ((int) (scaledWidth * (1.5 / 4F))) + 1, ((int) (scaledWidth * (2.5 / 4F))) - 1, 2, 0xFFFFFFFF);
        drawHorizontalLine(matrices, ((int) (scaledWidth * (1.5 / 4F))) + 1, ((int) (scaledWidth * (2.5 / 4F))) - 1, 12, 0xFFFFFFFF);
        drawVerticalLine(matrices, ((int) (scaledWidth * (1.5 / 4F))), 2, 12, 0xFFFFFFFF);
        drawVerticalLine(matrices, ((int) (scaledWidth * (2.5 / 4F))), 2, 12, 0xFFFFFFFF);
    }
}
