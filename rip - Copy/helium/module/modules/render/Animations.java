package rip.helium.module.modules.render;

import java.util.ArrayList;

import rip.helium.module.Module;
import rip.helium.setting.Setting;
import rip.helium.utils.render.ColorUtils;

public class Animations extends Module {

	public Setting mode;
	
	public Animations(int bind, String name, Category category) {
		super(bind, name, category);
		this.setColor(ColorUtils.generateColor());
		this.setHidden(true);
		this.setState(true);
		
		ArrayList<String> modes = new ArrayList<String>();
		modes.add("1.7");
		modes.add("97k");
		modes.add("Sensation");
		modes.add("Matt");
		modes.add("Slide");
		modes.add("Sigma");
		modes.add("Helium");
		modes.add("Poke");
		modes.add("Dortware");
		modes.add("Swang");
		modes.add("oHare");
		modes.add("Exhibition");
		modes.add("Spin");
		modes.add("Astro");
		modes.add("In");
		modes.add("Remix");
		modes.add("Shotbowxd");
		
		this.mode = new Setting("Mode", this, "Helium", modes);
		
		mc.hackedClient.getSettingManager().addSetting(this.mode);
	}

}
