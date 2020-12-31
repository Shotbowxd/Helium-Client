package rip.helium.cheat.impl.movement;

import me.hippo.systems.lwjeb.annotation.Collect;
import net.minecraft.network.play.client.C0BPacketEntityAction;
import net.minecraft.util.MovementInput;
import rip.helium.cheat.Cheat;
import rip.helium.cheat.CheatCategory;
import rip.helium.event.minecraft.PlayerUpdateEvent;

public class Sneak extends Cheat {

    public Sneak() {
        super("Sneak", "Makes you sneak", CheatCategory.MOVEMENT);
    }

    @Collect
    public void motionEvent(PlayerUpdateEvent e) {
        final boolean sneaking = mc.thePlayer.isSneaking();
        boolean moving = MovementInput.moveForward != 0.0f;
        final boolean strafing = MovementInput.moveStrafe != 0.0f;
        moving = (moving || strafing);
        if (!moving || sneaking) {
            if (e.isPre()) {
                sneaker();
            }
        } else {
            this.sneaker();
            if (e.isPre()) {
                this.unsneaker();
            }
        }
    }


    public void sneaker() {
        mc.thePlayer.sendQueue.addToSendQueueNoEvent(new C0BPacketEntityAction(mc.thePlayer, C0BPacketEntityAction.Action.START_SNEAKING));
    }

    public void unsneaker() {
        mc.thePlayer.sendQueue.addToSendQueueNoEvent(new C0BPacketEntityAction(mc.thePlayer, C0BPacketEntityAction.Action.STOP_SNEAKING));
    }

}
