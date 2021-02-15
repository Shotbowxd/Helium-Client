package rip.helium.module.modules.render;

import java.util.ArrayList;

import net.minecraft.client.entity.AbstractClientPlayer;
import net.minecraft.client.renderer.texture.SimpleTexture;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ResourceLocation;
import rip.helium.event.EventTarget;
import rip.helium.event.events.impl.player.UpdateEvent;
import rip.helium.module.Module;
import rip.helium.setting.Setting;
import rip.helium.utils.render.ColorUtils;

public class Capes extends Module {

	public Setting mode;
	
	public Capes(int bind, String name, Category category) {
		super(bind, name, category);
		this.setColor(ColorUtils.generateColor());
		this.setHidden(true);
		this.setState(true);
		
		ArrayList<String> modes = new ArrayList<String>();
		modes.add("Silver Phoenix #1");
		modes.add("Silver Phoenix #2");
		modes.add("Silver Phoenix #3");
		modes.add("Helium");
		modes.add("Kansio");
		modes.add("Jeffrey");
		
		this.mode = new Setting("Mode", this, "SilverPhoenix #1", modes);
		
		mc.hackedClient.getSettingManager().addSetting(this.mode);
	}
	
	@Override
	public void onDisable() {
		super.onDisable();
		for(Entity e: mc.theWorld.loadedEntityList) {
			if(e instanceof EntityPlayer) {
				AbstractClientPlayer acp = (AbstractClientPlayer) e;
				String cape = "";
				switch(this.mode.getValString()) {
				case "Silver Phoenix #1":
					cape = "dotexe/cape12.png";
					break;
				case "Silver Phoenix #2":
					cape = "dotexe/cape2.png";
					break;
				case "Silver Phoenix #3":
					cape = "dotexe/cape8.png";
					break;
				case "Helium":
					cape = "dotexe/heliumcape1.png";
					break;
				case "Kansio":
					cape = "dotexe/kansiocape1.png";
					break;
				case "Jeffrey":
					cape = "dotexe/jeffrey.png";
					break;
				}
				ResourceLocation rl = new ResourceLocation(cape);
				SimpleTexture st = new SimpleTexture(rl);
				if(acp.getLocationCape() != null) {
					acp.setLocationOfCape(null);
					mc.getTextureManager().deleteTexture(rl);
				}
			}
		}
	}
	
	@EventTarget
	public void onUpdatePre(UpdateEvent event) {
		for(Entity e: mc.theWorld.loadedEntityList) {
			if(e instanceof EntityPlayer) {
				AbstractClientPlayer acp = (AbstractClientPlayer) e;
				String cape = "";
				switch(this.mode.getValString()) {
				case "Silver Phoenix #1":
					cape = "dotexe/cape12.png";
					break;
				case "Silver Phoenix #2":
					cape = "dotexe/cape2.png";
					break;
				case "Silver Phoenix #3":
					cape = "dotexe/cape8.png";
					break;
				case "Helium":
					cape = "dotexe/heliumcape1.png";
					break;
				case "Kansio":
					cape = "dotexe/kansiocape1.png";
					break;
				case "Jeffrey":
					cape = "dotexe/jeffrey.png";
					break;
				}
				ResourceLocation rl = new ResourceLocation(cape);
				SimpleTexture st = new SimpleTexture(rl);
				if(mc.hackedClient.getFriendManager().isFriend(e.getName())) {
					if(acp.getLocationCape() == null || acp.getLocationCape().getResourcePath() != cape) {
						acp.setLocationOfCape(null);
						mc.getTextureManager().deleteTexture(rl);
						acp.setLocationOfCape(rl);
						mc.getTextureManager().loadTexture(rl, st);
					}
				}
				if(!mc.hackedClient.getFriendManager().isFriend(e.getName())) {
					if(acp.getLocationCape() != null) {
						acp.setLocationOfCape(null);
						mc.getTextureManager().deleteTexture(rl);
					}
				}
			}
		}
	}
	
}
