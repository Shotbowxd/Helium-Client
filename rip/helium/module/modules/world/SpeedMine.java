package rip.helium.module.modules.world;

import rip.helium.module.Module;
import rip.helium.utils.render.ColorUtils;

public class SpeedMine extends Module {
	
	public SpeedMine(int bind, String name, String displayName, Category category) {
		super(bind, name, displayName, category);
		this.setColor(ColorUtils.generateColor());
	}

}
