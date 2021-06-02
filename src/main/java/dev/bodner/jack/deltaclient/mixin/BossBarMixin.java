package dev.bodner.jack.deltaclient.mixin;

import dev.bodner.jack.deltaclient.DeltaClient;
import net.minecraft.client.gui.hud.BossBarHud;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

@Mixin(BossBarHud.class)
public class BossBarMixin {

    @ModifyVariable(method = "render", at = @At(value = "INVOKE_ASSIGN", target = "Ljava/util/Collection;iterator()Ljava/util/Iterator;"), ordinal = 1)
    private int mixin(int j){
        int var = j;
        if (DeltaClient.compass != null){
            var += DeltaClient.compass.getHeight();
        }
        return var;
    }
}
