package rip.helium.module.modules.render;

import rip.helium.module.Module;
import rip.helium.setting.Setting;
import rip.helium.utils.render.ColorUtils;

public class Colors extends Module {

	public Setting hudR;
	public Setting hudG;
	public Setting hudB;
	
	public Setting clickR;
	public Setting clickG;
	public Setting clickB;
	
	public Colors(int bind, String name, Category category) {
		super(bind, name, category);
		this.setColor(ColorUtils.generateColor());
		this.setHidden(true);
		
		this.hudR = new Setting("HUD R", this, 255, 0, 255, true);
		this.hudG = new Setting("HUD G", this, 255, 0, 255, true);
		this.hudB = new Setting("HUD B", this, 255, 0, 255, true);
		
		this.clickR = new Setting("Click R", this, 255, 0, 255, true);
		this.clickG = new Setting("Click G", this, 255, 0, 255, true);
		this.clickB = new Setting("Click B", this, 255, 0, 255, true);
		
		mc.hackedClient.getSettingManager().addSetting(this.hudR);
		mc.hackedClient.getSettingManager().addSetting(this.hudG);
		mc.hackedClient.getSettingManager().addSetting(this.hudB);
		
		mc.hackedClient.getSettingManager().addSetting(this.clickR);
		mc.hackedClient.getSettingManager().addSetting(this.clickG);
		mc.hackedClient.getSettingManager().addSetting(this.clickB);
	}
	
	@Override
	public void onEnable() {
		super.onEnable();
		this.toggle();
	}

}
