package rip.helium.module.modules.render;

import rip.helium.module.Module;
import rip.helium.utils.render.ColorUtils;

public class Night extends Module {

	public Night(int bind, String name, Category category) {
		super(bind, name, category);
		this.setColor(ColorUtils.generateColor());
		this.setHidden(true);
	}

}
