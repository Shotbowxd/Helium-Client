package rip.helium.module.modules.combat;

import rip.helium.module.Module;
import rip.helium.setting.Setting;

public class Targeting extends Module {

	public Setting players;
	public Setting monsters;
	public Setting animals;
	public Setting villagers;
	public Setting golems;
	
	public Targeting(int bind, String name, Category category) {
		super(bind, name, category);
		this.setHidden(true);
		
		this.players = new Setting("Players", this, true);
		this.monsters = new Setting("Monsters", this, false);
		this.animals = new Setting("Animals", this, false);
		this.villagers = new Setting("Villagers", this, false);
		this.golems = new Setting("Golems", this, false);
		
		mc.hackedClient.getSettingManager().addSetting(this.players);
		mc.hackedClient.getSettingManager().addSetting(this.monsters);
		mc.hackedClient.getSettingManager().addSetting(this.animals);
		mc.hackedClient.getSettingManager().addSetting(this.villagers);
		mc.hackedClient.getSettingManager().addSetting(this.golems);
	}
	
	@Override
	public void onEnable() {
		super.onEnable();
		this.toggle();
	}

}
