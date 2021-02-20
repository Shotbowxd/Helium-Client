package rip.helium.module.modules.render;

import rip.helium.module.Module;
import rip.helium.utils.render.ColorUtils;

public class CustomViewmodel extends Module {

	public CustomViewmodel(int bind, String name, String displayName, Category category) {
		super(bind, name, displayName, category);
		this.setColor(ColorUtils.generateColor());
		this.setHidden(true);
	}

}
