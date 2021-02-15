package rip.helium.module.modules.player;

import rip.helium.event.EventTarget;
import rip.helium.event.events.impl.player.UpdateEvent;
import rip.helium.module.Module;
import rip.helium.module.modules.combat.KillAura;
import rip.helium.setting.Setting;
import rip.helium.utils.render.ColorUtils;

public class GoodHack extends Module {
	
	private Setting increment;
	private float serverYaw = 0;
	
	public GoodHack(int bind, String name, String displayName, Category category) {
		super(bind, name, displayName, category);
		this.setColor(ColorUtils.generateColor());
		
		this.increment = new Setting("Increment", this, 25f, 0.1f, 360f, false);
		
		mc.hackedClient.getSettingManager().addSetting(this.increment);
	}
	
	@EventTarget
	public void onUpdate(UpdateEvent event) {
		if(event.isPre()) {
			if(mc.hackedClient.getModuleManager().getModule("KillAura").getState() && ((KillAura)mc.hackedClient.getModuleManager().getModule("KillAura")).getCurrentTarget() != null)
				return;
			
			serverYaw += (float)increment.getValDouble();
			event.setYaw(serverYaw);
			event.setPitch(180f);
		}
	}

}
