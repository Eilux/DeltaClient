package dev.bodner.jack.deltaclient.gui.hud;

import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawableHelper;
import net.minecraft.client.render.item.ItemRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Identifier;

public class Armor extends DrawableHelper {
    private final MinecraftClient client = MinecraftClient.getInstance();
    private final ItemRenderer itemRenderer = client.getItemRenderer();
    private int scaledWidth = this.client.getWindow().getScaledWidth();
    private int scaledHeight = this.client.getWindow().getScaledHeight();

    public Armor(){}

    public void update() {
        scaledWidth = this.client.getWindow().getScaledWidth();
        scaledHeight = this.client.getWindow().getScaledHeight();
    }

    public void draw(MatrixStack matrixStack) {
        update();

        int x = this.scaledWidth - 18;
        int y = this.scaledHeight - 18;

        ItemStack headSlot = this.client.player.getInventory().getArmorStack(3);
        ItemStack chestSlot = this.client.player.getInventory().getArmorStack(2);
        ItemStack legSlot = this.client.player.getInventory().getArmorStack(1);
        ItemStack bootSlot = this.client.player.getInventory().getArmorStack(0);

        if (headSlot.isEmpty()) {
//            matrixStack.push();
//            this.client.getTextureManager().bindTexture(new Identifier("textures/item/empty_armor_slot_helmet.png"));
            RenderSystem.setShaderTexture(0, new Identifier("textures/item/empty_armor_slot_helmet.png"));
            drawTexture(matrixStack, x, y - 54, 0, 0, 16, 16, 16, 16);
//            matrixStack.pop();
        }
        if (chestSlot.isEmpty()) {
//            matrixStack.push();
            RenderSystem.setShaderTexture(0, new Identifier("textures/item/empty_armor_slot_chestplate.png"));
            drawTexture(matrixStack, x, y - 36, 0, 0, 16, 16, 16, 16);
        }
        if (legSlot.isEmpty()) {
//            matrixStack.push();
            RenderSystem.setShaderTexture(0, new Identifier("textures/item/empty_armor_slot_leggings.png"));
            drawTexture(matrixStack, x, y - 18, 0, 0, 16, 16, 16, 16);
        }
        if (bootSlot.isEmpty()) {
//            matrixStack.push();
            RenderSystem.setShaderTexture(0, new Identifier("textures/item/empty_armor_slot_boots.png"));
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

//        this.itemRenderer.reload(this.client.getResourceManager());
    }
}
