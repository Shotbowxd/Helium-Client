package rip.helium.module.modules.render;

import rip.helium.event.EventTarget;
import rip.helium.event.events.impl.player.UpdateEvent;
import rip.helium.module.Module;
import rip.helium.utils.client.Timer;
import rip.helium.utils.render.ColorUtils;

public class Brightness extends Module {
	
	public Brightness(int bind, String name, Category category) {
		super(bind, name, category);
		this.setColor(ColorUtils.generateColor());
		this.setHidden(true);
	}
	
	@EventTarget
	public void onUpdate(UpdateEvent event) {
		mc.gameSettings.gammaSetting = 10000;
	}
	
	@Override
	public void onDisable() {
		mc.gameSettings.gammaSetting = 1;
		super.onDisable();
	}
	
}
