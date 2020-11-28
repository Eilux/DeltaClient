package dev.bodner.jack.deltaclient.client;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.options.KeyBinding;
import net.minecraft.client.util.InputUtil;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ArmorItem;
import net.minecraft.item.ItemStack;
import net.minecraft.network.packet.c2s.play.UpdateSelectedSlotC2SPacket;
import org.lwjgl.glfw.GLFW;

@Environment(EnvType.CLIENT)
public class DeltaclientClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
//        KeyBinding binding1 = KeyBindingHelper.registerKeyBinding(new KeyBinding("key.deltaclient.armor_swap", InputUtil.Type.KEYSYM, GLFW.GLFW_KEY_G, "key.category.first.test"));
//
//        ClientTickEvents.END_CLIENT_TICK.register(client -> {
//            if (binding1.wasPressed()){
//                PlayerEntity player = MinecraftClient.getInstance().player;
//                ItemStack itemStack = player.getMainHandStack();
//
//                if(itemStack.getItem().getClass().equals(ArmorItem.class)){
//                    ItemStack inSlot = player.getEquippedStack(((ArmorItem)itemStack.getItem()).getSlotType());
//                    player.equipStack(((ArmorItem)itemStack.getItem()).getSlotType(),itemStack);
//                    MinecraftClient.getInstance().getNetworkHandler().sendPacket(new UpdateSelectedSlotC2SPacket());
//
//                    player.equip(player.inventory.selectedSlot,inSlot);
//                    MinecraftClient.getInstance().getNetworkHandler().sendPacket(new UpdateSelectedSlotC2SPacket(player.inventory.selectedSlot));
//                }
//            }
//        });
    }
}
