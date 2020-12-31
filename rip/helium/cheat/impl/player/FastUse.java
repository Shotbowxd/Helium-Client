package rip.helium.cheat.impl.player;

import me.hippo.systems.lwjeb.annotation.Collect;
import net.minecraft.network.play.client.C03PacketPlayer;
import rip.helium.cheat.Cheat;
import rip.helium.cheat.CheatCategory;
import rip.helium.event.minecraft.PlayerUpdateEvent;

public class FastUse extends Cheat {

    public FastUse() {
        super("FastUse", "Fast item use", CheatCategory.PLAYER);
    }

    @Collect
    public void playerUpdateEvent(PlayerUpdateEvent event) {
        if (mc.thePlayer.isEating() && mc.thePlayer.onGround) {
            for (int i = 0; i < 20; i++) {
                mc.getNetHandler().addToSendQueueNoEvent(new C03PacketPlayer(false));
            }
            mc.thePlayer.stopUsingItem();
        }
    }

}
