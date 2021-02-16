package rip.helium.module.modules.render;

import java.util.ArrayList;

import rip.helium.module.Module;
import rip.helium.setting.Setting;
import rip.helium.utils.render.ColorUtils;

public class ClickGUI extends Module {
	
	public Setting mode;
	
	public ClickGUI(int bind, String name, String displayName, Category category) {
		super(bind, name, displayName, category);
		this.setColor(ColorUtils.generateColor());
		this.setHidden(true);
		
		ArrayList<String> modes = new ArrayList<String>();
		modes.add("Michael");
		modes.add("Slick");
		
		this.mode = new Setting("Click GUI Mode", this, "Slick", modes);
		
		mc.hackedClient.getSettingManager().addSetting(this.mode);
	}
	
	@Override
	public void onEnable() {
		super.onEnable();
		mc.displayGuiScreen(mc.hackedClient.getGUI());
		this.setState(false);
	}
	
}
