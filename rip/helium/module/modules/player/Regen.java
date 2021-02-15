package rip.helium.module.modules.player;

import net.minecraft.network.play.client.C03PacketPlayer;
import net.minecraft.util.AxisAlignedBB;
import rip.helium.event.EventTarget;
import rip.helium.event.events.impl.player.UpdateEvent;
import rip.helium.module.Module;
import rip.helium.setting.Setting;
import rip.helium.utils.render.ColorUtils;

public class Regen extends Module {

	private Setting health;
	
	public Regen(int bind, String name, Category category) {
		super(bind, name, category);
		this.setColor(ColorUtils.generateColor());
		
		this.health = new Setting("Health", this, 15.0, 0.0, 20.0, true);
		
		mc.hackedClient.getSettingManager().addSetting(this.health);
	}
	
	@EventTarget
	public void onUpdatePre(UpdateEvent event) {
		if(mc.thePlayer.getHealth() <= this.health.getValDouble() && mc.thePlayer.onGround) {
			mc.getNetHandler().addToSendQueue(new C03PacketPlayer(true));
		}
	}
	
}
