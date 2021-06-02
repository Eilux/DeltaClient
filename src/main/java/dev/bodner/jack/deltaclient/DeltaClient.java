package dev.bodner.jack.deltaclient;

import dev.bodner.jack.deltaclient.gui.hud.Armor;
import dev.bodner.jack.deltaclient.gui.hud.Compass;
import dev.bodner.jack.deltaclient.gui.hud.Coordinates;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.ModInitializer;

public class DeltaClient implements ClientModInitializer, ModInitializer {
    public static Compass compass;
    public static Armor armor;
    public static Coordinates coordinates;

    @Override
    public void onInitialize() {

    }

    @Override
    public void onInitializeClient() {
    }

}
