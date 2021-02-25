package rip.helium.module.modules.player;

import net.minecraft.client.Minecraft;
import net.minecraft.network.play.client.C03PacketPlayer;
import rip.helium.event.EventTarget;
import rip.helium.event.events.impl.network.PacketSendEvent;
import rip.helium.module.Module;
import rip.helium.utils.render.ColorUtils;

public class NoFall extends Module {

	public NoFall(int bind, String name, String displayName, Category category) {
		super(bind, name, displayName, category);
		this.setState(true);
		this.setColor(ColorUtils.generateColor());
	}
	
	@EventTarget
	public void onPacketSend(PacketSendEvent event) {
        if (event.getPacket() instanceof C03PacketPlayer) {
            final C03PacketPlayer packet = (C03PacketPlayer) event.getPacket();
            if (mc.thePlayer.fallDistance > 0 || mc.playerController.isHittingBlock) {
                packet.onGround = true;
            }
        }
	}
	
}
