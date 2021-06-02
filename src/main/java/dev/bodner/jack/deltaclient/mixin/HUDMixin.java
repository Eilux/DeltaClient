package dev.bodner.jack.deltaclient.mixin;

import com.google.common.collect.Lists;
import com.google.common.collect.Ordering;
import com.mojang.blaze3d.systems.RenderSystem;
import dev.bodner.jack.deltaclient.gui.hud.Armor;
import dev.bodner.jack.deltaclient.gui.hud.Compass;
import dev.bodner.jack.deltaclient.gui.hud.Coordinates;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.DrawableHelper;
import net.minecraft.client.gui.hud.InGameHud;
import net.minecraft.client.texture.Sprite;
import net.minecraft.client.texture.StatusEffectSpriteManager;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffectUtil;
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

import static dev.bodner.jack.deltaclient.DeltaClient.*;

@Mixin(InGameHud.class)
public abstract class HUDMixin extends DrawableHelper {

    @Shadow
    @Final
    private MinecraftClient client;
    @Shadow
    private int scaledWidth;

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
                RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);

//                this.client.getTextureManager().bindTexture(HandledScreen.BACKGROUND_TEXTURE);
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
                            RenderSystem.setShaderTexture(0,sprite.getAtlas().getId());
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

    @Inject(
            method = "render",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/hud/PlayerListHud;tick(Z)V")
    )
    public void render(MatrixStack matrices, float tickDelta, CallbackInfo ci) {
        if (coordinates == null){
            coordinates = new Coordinates();
        }
        if (compass == null){
            compass = new Compass();
        }
        if (armor == null){
            armor = new Armor();
        }
        if (!this.client.options.debugEnabled) {
            coordinates.draw(matrices);
            compass.draw(matrices,0);
        }
        armor.draw(matrices);
    }
}
