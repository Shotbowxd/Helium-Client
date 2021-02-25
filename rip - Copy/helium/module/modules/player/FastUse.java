package rip.helium.module.modules.player;

import net.minecraft.network.Packet;
import net.minecraft.network.play.client.C03PacketPlayer;
import rip.helium.event.EventTarget;
import rip.helium.event.events.impl.player.UpdateEvent;
import rip.helium.module.Module;
import rip.helium.utils.client.ClientUtils;
import rip.helium.utils.render.ColorUtils;

public class FastUse extends Module {

	public FastUse(int bind, String name, String displayName, Category category) {
		super(bind, name, displayName, category);
		this.setColor(ColorUtils.generateColor());
	}
	
	@EventTarget
	public void onUpdate(UpdateEvent event) {
		if (mc.thePlayer.getItemInUseDuration() == 16 && !(mc.thePlayer.getItemInUse().getItem() instanceof net.minecraft.item.ItemBow) && !(mc.thePlayer.getItemInUse().getItem() instanceof net.minecraft.item.ItemSword))
			for (int i = 0; i < 20; i++)
				mc.thePlayer.sendQueue.addToSendQueue((Packet)new C03PacketPlayer.C04PacketPlayerPosition(mc.thePlayer.posX, mc.thePlayer.posY - 0.06D, mc.thePlayer.posZ, mc.thePlayer.onGround));  
	}

}
