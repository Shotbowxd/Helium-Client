package rip.helium.module.modules.render;

import java.awt.Color;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.monster.EntityGolem;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.entity.monster.EntitySlime;
import net.minecraft.entity.passive.EntityAnimal;
import net.minecraft.entity.passive.EntityPig;
import net.minecraft.entity.passive.EntityVillager;
import net.minecraft.entity.player.EntityPlayer;
import rip.helium.event.EventTarget;
import rip.helium.event.events.impl.render.Render3DEvent;
import rip.helium.module.Module;
import rip.helium.module.modules.combat.Targeting;
import rip.helium.utils.render.ColorUtils;
import rip.helium.utils.render.Render3DUtils;

public class LivingESP extends Module {

	public LivingESP(int bind, String name, String displayName, Category category) {
		super(bind, name, displayName, category);
		this.setColor(ColorUtils.generateColor());
		this.setHidden(true);
	}
	
	private boolean checkValidity(EntityLivingBase entity) {
		if (entity != mc.thePlayer && (entity instanceof EntityPlayer && ((Targeting)mc.hackedClient.getModuleManager().getModule("Targeting")).players.getValBoolean())
                || (entity instanceof EntityMob && ((Targeting)mc.hackedClient.getModuleManager().getModule("Targeting")).monsters.getValBoolean())
                || (entity instanceof EntitySlime && ((Targeting)mc.hackedClient.getModuleManager().getModule("Targeting")).monsters.getValBoolean())
                || (entity instanceof EntityAnimal && ((Targeting)mc.hackedClient.getModuleManager().getModule("Targeting")).animals.getValBoolean())
                || (entity instanceof EntityPig && ((Targeting)mc.hackedClient.getModuleManager().getModule("Targeting")).animals.getValBoolean())
                || (entity instanceof EntityVillager && ((Targeting)mc.hackedClient.getModuleManager().getModule("Targeting")).villagers.getValBoolean())
                || (entity instanceof EntityGolem && ((Targeting)mc.hackedClient.getModuleManager().getModule("Targeting")).golems.getValBoolean())) {
			if(entity.getName().equalsIgnoreCase("Freecam")) return false; else return true;
		}
	    return false;
	}
	
	@EventTarget
	public void onRender3D(Render3DEvent event) {
		for (Entity e: mc.theWorld.loadedEntityList) {
		    if (e instanceof EntityLivingBase) {
		    	if (e != mc.thePlayer) {
		    		EntityLivingBase entity = (EntityLivingBase)e;
		    		if (checkValidity(entity)) {
		    			float red = 1.0f;
		    			float green = 1.0f;
		    			float blue = 1.0f;
		    			float alpha = 1.0f;
		    			if (entity instanceof EntityPlayer && mc.hackedClient.getFriendManager().isFriend(entity.getName())) {
	    					red = 85 / 255;
	    					green = 85 / 255;
	    					blue = 255 / 255;
	    				} else {
	    					final Color col = new Color(Color.HSBtoRGB((float)(Math.max(0.0, Math.min(mc.thePlayer.getDistanceSqToEntity(entity), 2500.0f) / 2500.0f) / 3.0), 1.0f, 1.0f) | 0xFF000000);
	    			        red = col.getRed() / 255.0f; green = col.getGreen() / 255.0f; blue = col.getBlue() / 255.0f;
	    				}
	    			  	Render3DUtils.drawEsp(entity, event.getPartialTicks(), red, green, blue, alpha);
		    		} 
		    	} 
		    }
		}	
	}
	
}
