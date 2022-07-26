package dev.bodner.jack.deltaclient;

import dev.bodner.jack.deltaclient.gui.hud.Armor;
import dev.bodner.jack.deltaclient.gui.hud.Compass;
import dev.bodner.jack.deltaclient.gui.hud.Coordinates;
import net.fabricmc.api.ClientModInitializer;

public class DeltaClient implements ClientModInitializer {
    public static Compass compass;
    public static Armor armor;
    public static Coordinates coordinates;

    @Override
    public void onInitializeClient() {
    }

}
