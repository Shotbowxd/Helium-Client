package rip.helium.module.modules.render;

import java.awt.Color;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import org.lwjgl.opengl.GL11;

import com.github.creeper123123321.viafabric.ViaFabric;
import com.github.creeper123123321.viafabric.util.ProtocolUtils;

import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.MathHelper;
import net.minecraft.util.Vec3;
import rip.helium.event.EventTarget;
import rip.helium.event.events.impl.render.RenderHUDEvent;
import rip.helium.module.Module;
import rip.helium.setting.Setting;
import rip.helium.utils.render.ColorUtils;
import rip.helium.utils.render.Render2DUtils;

public class HUD extends Module {
	
	private Setting watermark;
	private Setting fps;
	private Setting arrayList;
	private Setting radar;
	private Setting coords;
	private Setting direction;
	private Setting speed;
	private Setting potions;
	private Setting rainbow;
	private Setting animations;
	private Setting rect;
	
	public HUD(int bind, String name, Category category) {
		super(bind, name, category);
		this.setColor(ColorUtils.generateColor());
		this.setHidden(true);
		this.setState(true);
		
		watermark = new Setting("Watermark", this, true);
		fps = new Setting("FPS", this, true);
		arrayList = new Setting("ArrayList", this, true);
		radar = new Setting("Radar", this, true);
		coords = new Setting("Coords", this, true);
		direction = new Setting("Direction", this, true);
		this.speed = new Setting("Speed", this, true);
		potions = new Setting("Potions", this, true);
		rainbow = new Setting("Rainbow", this, false);
		animations = new Setting("Animations", this, false);
		rect = new Setting("ArrayList Rectangle", this, false);
		
		mc.hackedClient.getSettingManager().addSetting(watermark);
		mc.hackedClient.getSettingManager().addSetting(fps);
		mc.hackedClient.getSettingManager().addSetting(arrayList);
		mc.hackedClient.getSettingManager().addSetting(radar);
		mc.hackedClient.getSettingManager().addSetting(coords);
		mc.hackedClient.getSettingManager().addSetting(direction);
		mc.hackedClient.getSettingManager().addSetting(speed);
		mc.hackedClient.getSettingManager().addSetting(potions);
		mc.hackedClient.getSettingManager().addSetting(rainbow);
		mc.hackedClient.getSettingManager().addSetting(animations);
		mc.hackedClient.getSettingManager().addSetting(rect);
	}
	
	@EventTarget
	public void onRenderHUD(RenderHUDEvent event) {
		GlStateManager.pushMatrix();
		int topRightY = 2;
		if(this.rect.getValBoolean()) topRightY = 0;
		int topLeftY = 2;
		int bottomLeftY = event.getHeight() - 10;
		int bottomRightY = event.getHeight() - 10;
		if(this.watermark.getValBoolean()) {
			mc.fontRendererObj.drawStringWithShadow(mc.hackedClient.getName() + " v" + mc.hackedClient.getVersion() + " \2477(rel-1.8.8, p: " + ProtocolUtils.getProtocolName(ViaFabric.clientSideVersion) + ")", 2, topLeftY, new Color((int)((Colors)mc.hackedClient.getModuleManager().getModule("Colors")).hudR.getValDouble(), (int)((Colors)mc.hackedClient.getModuleManager().getModule("Colors")).hudG.getValDouble(), (int)((Colors)mc.hackedClient.getModuleManager().getModule("Colors")).hudB.getValDouble()).getRGB());
			topLeftY += 10;
		}
		if(this.coords.getValBoolean()) {
			mc.fontRendererObj.drawStringWithShadow("XYZ: \2477" + (int)Math.floor(mc.thePlayer.posX) + " / " + (int)Math.floor(mc.thePlayer.posY) + " / " + (int)Math.floor(mc.thePlayer.posZ), 2, topLeftY, 0xffffffff);
			topLeftY += 10;
		}
		if(this.direction.getValBoolean()) {
			mc.fontRendererObj.drawStringWithShadow("Direction: \2477" + mc.thePlayer.getHorizontalFacing().getName().substring(0, 1).toUpperCase() + mc.thePlayer.getHorizontalFacing().getName().substring(1), 2, topLeftY, 0xffffffff);
			topLeftY += 10;
		}
		if(this.fps.getValBoolean()) {
			mc.fontRendererObj.drawStringWithShadow("FPS: \2477" + mc.debugFPS, 2, topLeftY, 0xffffffff);
			topLeftY += 10;
		}
		if(this.speed.getValBoolean()) {
			
			Vec3 lastPosVec = new Vec3(mc.thePlayer.prevPosX, mc.thePlayer.prevPosY, mc.thePlayer.prevPosZ);
			Vec3 currentPosVec = new Vec3(mc.thePlayer.posX, mc.thePlayer.posY, mc.thePlayer.posZ);

			// Euclidean distance between your last and current position. 
			double distanceTraveled = lastPosVec.distanceTo(currentPosVec);

			//calculate speed
			// speed = distance * 60 *60 /1000
			double speed_in_m_s = distanceTraveled*10;
			DecimalFormat df = new DecimalFormat("###.#");
			mc.fontRendererObj.drawStringWithShadow("Speed: \2477" + df.format(speed_in_m_s) + " m/S", 2, topLeftY, 0xffffffff);
			topLeftY += 10;
		}
		
		if(this.radar.getValBoolean()) {
			/*topLeftY += 16;
			List<Entity> players = new ArrayList();
			for (Object o : mc.theWorld.loadedEntityList) {
				Entity e = (Entity)o;
		          	if (e instanceof EntityPlayer && e != mc.thePlayer)
		          		players.add(e);
		    }  
		    players.sort((Comparator)new Comparator<EntityPlayer>() {
		    	@Override
		    	public int compare(EntityPlayer m1, EntityPlayer m2) {
		    		String s1 = (mc.hackedClient.getFriendManager().isFriend(m1.getName()) ? mc.hackedClient.getFriendManager().getAliasName(m1.getName()) : m1.getName()) + " " + MathHelper.floor_double(m1.getDistanceToEntity(mc.thePlayer));
		    		String s2 = (mc.hackedClient.getFriendManager().isFriend(m2.getName()) ? mc.hackedClient.getFriendManager().getAliasName(m2.getName()) : m2.getName()) + " " + MathHelper.floor_double(m2.getDistanceToEntity(mc.thePlayer));
		            return mc.fontRendererObj.getStringWidth(s2) - mc.fontRendererObj.getStringWidth(s1);
		        }
		    });
		    if(players.size() == 0 || (players.size() == 1 && players.get(0).getName().equalsIgnoreCase("Freecam"))) {
		    	mc.fontRendererObj.drawStringWithShadow("Radar: \2477None", 2, topLeftY, 0xffffffff);
				topLeftY += 10;
		    } else {
		    	mc.fontRendererObj.drawStringWithShadow("Radar:", 2, topLeftY, 0xffffffff);
				topLeftY += 10;
		    }
		    for(Entity e: players) {
		    	String prefix = "";
		    	EntityPlayer pe = (EntityPlayer) e;
				if(pe.getName().equalsIgnoreCase("Freecam")) return;
				if(mc.hackedClient.getFriendManager().isFriend(pe.getName())) {
        			prefix = "\2479";
        		}else {
        			prefix = (mc.thePlayer.getDistanceToEntity(pe) <= 64.0F && mc.thePlayer.canEntityBeSeen((Entity)pe)) ? "\247c" : "\247a";
        		}
		    	if(mc.hackedClient.getFriendManager().isFriend(e.getName())) {
		    		mc.fontRendererObj.drawStringWithShadow(prefix + mc.hackedClient.getFriendManager().getAliasName(pe.getName()) + " \2477" + MathHelper.floor_double(pe.getDistanceToEntity(mc.thePlayer)), 2, topLeftY, 0xffffffff);
				}else {
					mc.fontRendererObj.drawStringWithShadow(prefix + pe.getName() + " \2477" + MathHelper.floor_double(pe.getDistanceToEntity(mc.thePlayer)), 2, topLeftY, 0xffffffff);
				}
		    	topLeftY += 10;
		    }*/
			topLeftY += 10;
			int tick = 0;
	        tick++;
	        if (tick >= 50)
	        	tick = 0; 
	        GL11.glPushMatrix();
	        GL11.glLineWidth(2.0F);
	        Render2DUtils.drawFilledCircle(54, topLeftY + 50, 50.0D, 1610612736);
	        Render2DUtils.drawFilledCircle(54, topLeftY + 50, tick, -1);
	        List<Entity> list1 = mc.theWorld.loadedEntityList;
	        GL11.glLineWidth(1.0F);
	        GL11.glPopMatrix();
	        for (int i = 0; i < list1.size(); i++) {
	        	Entity entity = list1.get(i);
	        	double xdis = mc.thePlayer.posX - entity.posX;
	        	double zdis = mc.thePlayer.posZ - entity.posZ;
	        	double tdis = Math.sqrt(xdis * xdis + zdis * zdis);
	        	double difInAng = MathHelper.wrapAngleTo180_double(mc.thePlayer.rotationYaw - Math.atan2(zdis, xdis) * 180.0D / Math.PI);
	        	double finalX = Math.cos(Math.toRadians(difInAng)) * tdis;
	        	double finalY = -Math.sin(Math.toRadians(difInAng)) * tdis;
	        	GL11.glPushMatrix();
	        	GL11.glTranslatef(54.0F, topLeftY + 50, 0.0F);
	        	if (tdis <= 100.0D && 
	        	!(entity instanceof net.minecraft.client.entity.EntityPlayerSP)) {
	        		if (entity instanceof EntityPlayer)
	        			if (mc.hackedClient.getFriendManager().isFriend(entity.getName())) {
	        				Render2DUtils.drawFilledCircle((int)finalX / 2, (int)finalY / 2, 1.0F, (new Color(0, 255, 255)).getRGB());
	        				GL11.glScalef(0.5F, 0.5F, 0.5F);
	        				EntityPlayer p = (EntityPlayer)entity;
	        				String u = p.getName();
	        				mc.fontRendererObj.drawString(u, ((int)finalX - (mc).fontRendererObj.getStringWidth(u) / 2), ((int)finalY - 10), (new Color(0, 255, 255)).getRGB());
	        				GL11.glScalef(1.0F, 0.5F, 1.0F);
	        			} else {
	        				final Color col = new Color(Color.HSBtoRGB((float)(Math.max(0.0, Math.min(mc.thePlayer.getDistanceSqToEntity(entity), 2500.0f) / 2500.0f) / 3.0), 1.0f, 1.0f) | 0xFF000000);
	        				Render2DUtils.drawFilledCircle((int)finalX / 2, (int)finalY / 2, 1.0F, col.getRGB());
	        				GL11.glScalef(0.5F, 0.5F, 0.5F);
	        				EntityPlayer p = (EntityPlayer)entity;
	        				String u = p.getName();
	        				mc.fontRendererObj.drawString(u, ((int)finalX - (mc).fontRendererObj.getStringWidth(u) / 2), ((int)finalY - 10), col.getRGB());
	        				GL11.glScalef(1.0F, 0.5F, 1.0F);
	        			}  
	        		if (entity instanceof net.minecraft.entity.passive.EntityAnimal)
	        			Render2DUtils.drawFilledCircle((int)finalX / 2, (int)finalY / 2, 1.0F, -16711936); 
	        		if (entity instanceof net.minecraft.entity.monster.EntityMob)
	        			Render2DUtils.drawFilledCircle((int)finalX / 2, (int)finalY / 2, 1.0F, -65536); 
	        		if (entity instanceof net.minecraft.entity.monster.EntitySlime)
	        			Render2DUtils.drawFilledCircle((int)finalX / 2, (int)finalY / 2, 1.0F, -30516); 
	        		if (entity instanceof net.minecraft.entity.passive.EntityVillager)
	        			Render2DUtils.drawFilledCircle((int)finalX / 2, (int)finalY / 2, 1.0F, -7650029); 
	        		if (entity instanceof net.minecraft.entity.passive.EntityBat)
	        			Render2DUtils.drawFilledCircle((int)finalX / 2, (int)finalY / 2, 1.0F, -744352); 
	        		if (entity instanceof net.minecraft.entity.passive.EntitySquid)
	        			Render2DUtils.drawFilledCircle((int)finalX / 2, (int)finalY / 2, 1.0F, -16764007); 
	        	} 
	        	GL11.glPopMatrix();
	        }
		}
		if(this.arrayList.getValBoolean()) {
			for(Module m: mc.hackedClient.getModuleManager().getModulesForArrayList()) {
				if(this.animations.getValBoolean()) {
					if (m.getAnimationOn() > 0) m.setAnimationOn(m.getAnimationOn() - 1);
					if(m.getAnimationOff() >= 0 && m.getAnimationOff() < 150) m.setAnimationOff(m.getAnimationOff() + 1);
					if(!m.isHidden() && (m.getState() || m.getAnimationOff() < 150)) {
						String name = m.getDisplayName();
						if(m.getSuffix().length() != 0) {
							name += " \2477" + m.getSuffix();
						}
						if(this.rect.getValBoolean()) {
							Render2DUtils.drawRect(event.getWidth() - mc.fontRendererObj.getStringWidth(name) - 4 + (m.getState() ? m.getAnimationOn() : m.getAnimationOff()), topRightY, event.getWidth(), topRightY + 10, 0x80000000);
							Render2DUtils.drawRect(event.getWidth() - mc.fontRendererObj.getStringWidth(name) - 6 + (m.getState() ? m.getAnimationOn() : m.getAnimationOff()), topRightY, event.getWidth() - mc.fontRendererObj.getStringWidth(name) - 4 + (m.getState() ? m.getAnimationOn() : m.getAnimationOff()), topRightY + 10, rainbow.getValBoolean() ? ColorUtils.setRainbow(35000000L * topRightY, 1f).getRGB() : m.getColor());
						}
						mc.fontRendererObj.drawStringWithShadow(name, event.getWidth()- mc.fontRendererObj.getStringWidth(name) - 2 + (m.getState() ? m.getAnimationOn() : m.getAnimationOff()), topRightY, rainbow.getValBoolean() ? ColorUtils.setRainbow(35000000L * topRightY, 1f).getRGB() : m.getColor());
						topRightY += 10;
					}
				} else {
					if(!m.isHidden() && (m.getState())) {
						String name = m.getDisplayName();
						if(m.getSuffix().length() != 0) {
							name += " \2477" + m.getSuffix();
						}
						if(this.rect.getValBoolean()) {
							Render2DUtils.drawRect(event.getWidth() - mc.fontRendererObj.getStringWidth(name) - 4, topRightY, event.getWidth(), topRightY + 10, 0x80000000);
							Render2DUtils.drawRect(event.getWidth() - mc.fontRendererObj.getStringWidth(name) - 6, topRightY, event.getWidth() - mc.fontRendererObj.getStringWidth(name) - 4, topRightY + 10, rainbow.getValBoolean() ? ColorUtils.setRainbow(35000000L * topRightY, 1f).getRGB() : m.getColor());
						}
						mc.fontRendererObj.drawStringWithShadow(name, event.getWidth()- mc.fontRendererObj.getStringWidth(name) - 2, topRightY, rainbow.getValBoolean() ? ColorUtils.setRainbow(35000000L * topRightY, 1f).getRGB() : m.getColor());
						topRightY += 10;
					}
				}
			}
		}
		if(this.potions.getValBoolean()) {
			ArrayList<PotionEffect> potions = new ArrayList<PotionEffect>();
		      for (PotionEffect effect: mc.thePlayer.getActivePotionEffects())
		    	  potions.add(effect); 
		      potions.sort((Comparator)new Comparator<PotionEffect>() {
		    	  public int compare(PotionEffect m1, PotionEffect m2) {
		    	  	  String s1 = I18n.format(m1.getEffectName(), new Object[0]);
		    	  	  String s2 = I18n.format(m2.getEffectName(), new Object[0]);
		    	  	  if (m1.getAmplifier() == 1) {
		    	  		  s1 = String.valueOf(s1) + " " + "2";
		    	  	  } else if (m1.getAmplifier() == 2) {
		    	  		  s1 = String.valueOf(s1) + " " + "3";
		    	  	  } else if (m1.getAmplifier() == 3) {
		    	  		  s1 = String.valueOf(s1) + " " + "4";
		    	  	  } else if (m1.getAmplifier() > 0) {
		    	  		  s1 = String.valueOf(s1) + " " + (m1.getAmplifier() + 1);
		    	      } 
		    	  	  if (m2.getAmplifier() == 1) {
		    	  		  s2 = String.valueOf(s2) + " " + "2";
		    	  	  } else if (m2.getAmplifier() == 2) {
		    	  		  s2 = String.valueOf(s2) + " " + "3";
		    	  	  } else if (m2.getAmplifier() == 3) {
		    	          s2 = String.valueOf(s2) + " " + "4";
		    	      } else if (m2.getAmplifier() > 0) {
		    	  		  s2 = String.valueOf(s2) + " " + (m2.getAmplifier() + 1);
		    	  	  } 
		    	  	  return mc.fontRendererObj.getStringWidth(s2) - mc.fontRendererObj.getStringWidth(s1);
		    	 }
		    });
		    for (PotionEffect effect: potions) {
		      	String name = I18n.format(effect.getEffectName(), new Object[0]);
		      	if (effect.getAmplifier() == 1) {
		      		name = String.valueOf(name) + " " + "2";
		      	} else if (effect.getAmplifier() == 2) {
		      		name = String.valueOf(name) + " " + "3";
		      	} else if (effect.getAmplifier() == 3) {
		      		name = String.valueOf(name) + " " + "4";
		      	} else if (effect.getAmplifier() > 0) {
		      		name = String.valueOf(name) + " " + (effect.getAmplifier() + 1);
		      	} 
		      	int var1 = effect.getDuration() / 20;
		      	int var2 = var1 / 60;
		      	var1 %= 60;
		      	char color = '7';
		      	if (var2 == 0) {
		      		if (var1 <= 5) {
		      			color = '4';
		      		} else if (var1 <= 10) {
		      			color = 'c';
		      		} else if (var1 <= 15) {
		      			color = '6';
		      		} else if (var1 <= 20) {
		      			color = 'e';
		      		}
		      	}
		    String maymay = String.format("%s %s", new Object[] { name, Potion.getDurationString(effect) });
		    name = String.format("%s \247%s%s", new Object[] { name, Character.valueOf(color), Potion.getDurationString(effect) });
		    mc.fontRendererObj.drawStringWithShadow(name, event.getWidth() - mc.fontRendererObj.getStringWidth(name) - 2, bottomRightY, (Potion.potionTypes[effect.getPotionID()]).getLiquidColor());
		    bottomRightY -= 12;
		    }
		}
		GlStateManager.popMatrix();
	}

}
