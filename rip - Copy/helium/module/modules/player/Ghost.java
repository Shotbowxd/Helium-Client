package rip.helium.module.modules.player;

import rip.helium.module.Module;
import rip.helium.utils.render.ColorUtils;

public class Ghost extends Module {

	public Ghost(int bind, String name, Category category) {
		super(bind, name, category);
		this.setColor(ColorUtils.generateColor());
	}

}
