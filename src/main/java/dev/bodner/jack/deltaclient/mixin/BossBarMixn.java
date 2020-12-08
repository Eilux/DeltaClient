package dev.bodner.jack.deltaclient.mixin;

import com.google.common.collect.Maps;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.hud.BossBarHud;
import net.minecraft.client.gui.hud.ClientBossBar;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.boss.BossBar;
import net.minecraft.text.StringVisitable;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;

import java.util.Iterator;
import java.util.Map;
import java.util.UUID;

@Mixin(BossBarHud.class)
public class BossBarMixn {

    @Shadow
    private void renderBossBar(MatrixStack matrices, int x, int y, BossBar bossBar){}


    @Shadow @Final private static Identifier BARS_TEXTURE = new Identifier("textures/gui/bars.png");
    @Shadow @Final private MinecraftClient client;
    @Shadow @Final private Map<UUID, ClientBossBar> bossBars = Maps.newLinkedHashMap();


    /**
     * @author Eilux
     * @reason overlap with compass
     */
    @Overwrite
    public void render(MatrixStack matrices) {
        if (!this.bossBars.isEmpty()) {
            int i = this.client.getWindow().getScaledWidth();
            int j = 12 + 25;
            Iterator var4 = this.bossBars.values().iterator();

            while(var4.hasNext()) {
                ClientBossBar clientBossBar = (ClientBossBar)var4.next();
                int k = i / 2 - 91;
                RenderSystem.color4f(1.0F, 1.0F, 1.0F, 1.0F);
                this.client.getTextureManager().bindTexture(BARS_TEXTURE);
                this.renderBossBar(matrices, k, j, clientBossBar);
                Text text = clientBossBar.getName();
                int m = this.client.textRenderer.getWidth((StringVisitable)text);
                int n = i / 2 - m / 2;
                int o = j - 9;
                this.client.textRenderer.drawWithShadow(matrices, text, (float)n, (float)o, 16777215);
                this.client.textRenderer.getClass();
                j += 10 + 9;
                if (j >= this.client.getWindow().getScaledHeight() / 3) {
                    break;
                }
            }

        }
    }
}
