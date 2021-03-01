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
import rip.helium.ClientSupport;
import rip.helium.HeliumClient;
import rip.helium.event.EventTarget;
import rip.helium.event.events.impl.render.RenderHUDEvent;
import rip.helium.module.Module;
import rip.helium.setting.Setting;
import rip.helium.utils.render.ColorUtils;
import rip.helium.utils.render.Render2DUtils;

public class HUD extends Module implements ClientSupport {
	
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
	//	mc.hackedClient.getSettingManager().addSetting(speed);
		mc.hackedClient.getSettingManager().addSetting(potions);
		mc.hackedClient.getSettingManager().addSetting(rainbow);
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
			//mc.fontRendererObj.drawStringWithShadow(mc.hackedClient.getName() + " v" + mc.hackedClient.getVersion() + " \2477(rel-1.8.8, p: " + ProtocolUtils.getProtocolName(ViaFabric.clientSideVersion) + ")", 2, topLeftY, new Color((int)((Colors)mc.hackedClient.getModuleManager().getModule("Colors")).hudR.getValDouble(), (int)((Colors)mc.hackedClient.getModuleManager().getModule("Colors")).hudG.getValDouble(), (int)((Colors)mc.hackedClient.getModuleManager().getModule("Colors")).hudB.getValDouble()).getRGB());
			Vec3 lastPosVec = new Vec3(mc.thePlayer.prevPosX, mc.thePlayer.prevPosY, mc.thePlayer.prevPosZ);
			Vec3 currentPosVec = new Vec3(mc.thePlayer.posX, mc.thePlayer.posY, mc.thePlayer.posZ);

			// Euclidean distance between your last and current position.
			double distanceTraveled = lastPosVec.distanceTo(currentPosVec);

			//calculate speed
			// speed = distance * 60 *60 /1000
			double speed_in_m_s = distanceTraveled*10;
			DecimalFormat df = new DecimalFormat("###.#");

			Render2DUtils.drawRect(8, 8, mc.fontRendererObj.getStringWidth("H§felium §7- §f" + mc.hackedClient.getVersion() + " §7- §fBPS: " + df.format(speed_in_m_s)) + 24, 30, new Color(32, 32, 32).getRGB());
			Render2DUtils.drawRect(8, 8, mc.fontRendererObj.getStringWidth("H§felium §7- §f" + mc.hackedClient.getVersion() + " §7- §fBPS: " + df.format(speed_in_m_s)) + 24, 10, ColorUtils.setRainbow(35000000L * topRightY, 1f).getRGB());
			mc.fontRendererObj.drawStringWithShadow("§bH§felium §7- §f" + mc.hackedClient.getVersion() + " §7- §fBPS: " + df.format(speed_in_m_s), 16, 18, ColorUtils.setRainbow(35000000L * topRightY, 1f).getRGB());

			topLeftY += 10;
		}

		if(this.arrayList.getValBoolean()) {
			for(Module m: mc.hackedClient.getModuleManager().getModulesForArrayList()) {
				if (!m.isHidden() && (m.getState())) {
					String name = m.getDisplayName();
					if (m.getSuffix().length() != 0) {
						name += " \2477[" + m.getSuffix() + "]";
					}
					Render2DUtils.drawRect(event.getWidth() - mc.fontRendererObj.getStringWidth(name) - 10, topRightY + 12, event.getWidth() - mc.fontRendererObj.getStringWidth(name) + 999, topRightY + 15.5, rainbow.getValBoolean() ? ColorUtils.setRainbow(35000000L * topRightY, 1f).getRGB() : m.getColor());

					Render2DUtils.drawRect(event.getWidth() - mc.fontRendererObj.getStringWidth(name) - 10, 1 + topRightY - 1, event.getWidth(), topRightY + 14, new Color(32, 32, 32).getRGB());
					Render2DUtils.drawRect(event.getWidth() - mc.fontRendererObj.getStringWidth(name) - 10, 1 + topRightY + 2, event.getWidth() - mc.fontRendererObj.getStringWidth(name) - 8, topRightY + 14, rainbow.getValBoolean() ? ColorUtils.setRainbow(35000000L * topRightY, 1f).getRGB() : m.getColor());

					mc.fontRendererObj.drawStringWithShadow(name, event.getWidth() - mc.fontRendererObj.getStringWidth(name) - 2, 1 + topRightY + 2, rainbow.getValBoolean() ? ColorUtils.setRainbow(35000000L * topRightY, 1f).getRGB() : m.getColor());
					topRightY += 11;
				}
			}
		}
		if(this.potions.getValBoolean()) {
			ArrayList<PotionEffect> potions = new ArrayList<>(mc.thePlayer.getActivePotionEffects());
		      potions.sort((m1, m2) -> {
					String s1 = I18n.format(m1.getEffectName());
					String s2 = I18n.format(m2.getEffectName());
					if (m1.getAmplifier() == 1) {
						s1 = s1 + " " + "2";
					} else if (m1.getAmplifier() == 2) {
						s1 = s1 + " " + "3";
					} else if (m1.getAmplifier() == 3) {
						s1 = s1 + " " + "4";
					} else if (m1.getAmplifier() > 0) {
						s1 = s1 + " " + (m1.getAmplifier() + 1);
				  }
					if (m2.getAmplifier() == 1) {
						s2 = s2 + " " + "2";
					} else if (m2.getAmplifier() == 2) {
						s2 = s2 + " " + "3";
					} else if (m2.getAmplifier() == 3) {
					  s2 = s2 + " " + "4";
				  } else if (m2.getAmplifier() > 0) {
						s2 = s2 + " " + (m2.getAmplifier() + 1);
					}
					return mc.fontRendererObj.getStringWidth(s2) - mc.fontRendererObj.getStringWidth(s1);
			 });
		    for (PotionEffect effect: potions) {
		      	String name = I18n.format(effect.getEffectName());
		      	if (effect.getAmplifier() == 1) {
		      		name = name + " " + "2";
		      	} else if (effect.getAmplifier() == 2) {
		      		name = name + " " + "3";
		      	} else if (effect.getAmplifier() == 3) {
		      		name = name + " " + "4";
		      	} else if (effect.getAmplifier() > 0) {
		      		name = name + " " + (effect.getAmplifier() + 1);
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
		    String maymay = String.format("%s %s", name, Potion.getDurationString(effect));
		    name = String.format("%s \247%s%s", name, color, Potion.getDurationString(effect));
		    mc.fontRendererObj.drawStringWithShadow(name, event.getWidth() - mc.fontRendererObj.getStringWidth(name) - 2, bottomRightY, (Potion.potionTypes[effect.getPotionID()]).getLiquidColor());
		    bottomRightY -= 12;
		    }
		}
		GlStateManager.popMatrix();
	}

}
