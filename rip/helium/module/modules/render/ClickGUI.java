package rip.helium.module.modules.render;

import rip.helium.gui.click.ClickGUIScreen;
import rip.helium.module.Module;
import rip.helium.utils.render.ColorUtils;

public class ClickGUI extends Module {
	
	public ClickGUI(int bind, String name, String displayName, Category category) {
		super(bind, name, displayName, category);
		this.setColor(ColorUtils.generateColor());
		this.setHidden(true);
	}
	
	@Override
	public void onEnable() {
		super.onEnable();
		mc.displayGuiScreen(mc.hackedClient.getGUI());
		this.setState(false);
	}
	
}
