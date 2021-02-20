package rip.helium.module.modules.player;

import net.minecraft.network.play.server.S08PacketPlayerPosLook;
import rip.helium.event.EventTarget;
import rip.helium.event.events.impl.network.PacketReceiveEvent;
import rip.helium.module.Module;
import rip.helium.utils.client.ClientUtils;
import rip.helium.utils.render.ColorUtils;

public class NoRotate extends Module {

	public NoRotate(int bind, String name, String displayName, Category category) {
		super(bind, name, displayName, category);
		this.setColor(ColorUtils.generateColor());
		this.setHidden(true);
	}
	
	@EventTarget
	public void onPacketReceive(PacketReceiveEvent event) {
		if (event.getPacket() instanceof S08PacketPlayerPosLook) {
			S08PacketPlayerPosLook packet = (S08PacketPlayerPosLook)event.getPacket();
			packet.setYaw(mc.thePlayer.rotationYaw);
            packet.setPitch(mc.thePlayer.rotationPitch);
		} 
	}

}
