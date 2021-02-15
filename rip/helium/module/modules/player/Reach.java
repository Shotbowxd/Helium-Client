package rip.helium.module.modules.player;

import rip.helium.event.EventTarget;
import rip.helium.event.events.impl.player.ReachEvent;
import rip.helium.module.Module;
import rip.helium.setting.Setting;
import rip.helium.utils.render.ColorUtils;

public class Reach extends Module {

	private Setting reach;
	
	public Reach(int bind, String name, Category category) {
		super(bind, name, category);
		this.setColor(ColorUtils.generateColor());
		this.setHidden(true);
		
		this.reach = new Setting("Distance", this, 4.5f, 0.1f, 10.0f, false);
		
		mc.hackedClient.getSettingManager().addSetting(this.reach);
	}
	
	@EventTarget
	public void onSetReach(ReachEvent event) {
		event.setReach((float)this.reach.getValDouble());
	}

}
