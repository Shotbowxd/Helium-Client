package rip.helium.module.modules.combat;

import net.minecraft.client.Minecraft;
import net.minecraft.network.play.client.C03PacketPlayer;
import rip.helium.event.EventTarget;
import rip.helium.event.events.impl.player.PlayerJumpEvent;
import rip.helium.module.Module;
import rip.helium.utils.render.ColorUtils;

public class Criticals extends Module {

	public Criticals(int bind, String name, Category category) {
		super(bind, name, category);
		this.setColor(ColorUtils.generateColor());
	}
	
	@EventTarget
	public void onPlayerJump(PlayerJumpEvent event) {
		Minecraft.getMinecraft().thePlayer.sendQueue.addToSendQueue(new C03PacketPlayer.C06PacketPlayerPosLook(mc.thePlayer.posX, mc.thePlayer.posY, mc.thePlayer.posZ, mc.thePlayer.rotationYaw, mc.thePlayer.rotationPitch, true));
	}

}
