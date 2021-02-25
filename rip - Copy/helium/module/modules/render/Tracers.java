package rip.helium.module.modules.render;

import java.awt.Color;

import org.lwjgl.opengl.GL11;

import net.minecraft.client.renderer.GlStateManager;
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
import rip.helium.utils.misc.Vec3d;
import rip.helium.utils.render.ColorUtils;

public class Tracers extends Module {

	public Tracers(int bind, String name, Category category) {
		super(bind, name, category);
		this.setColor(ColorUtils.generateColor());
		this.setHidden(true);
	}
	
	@EventTarget
	public void onRender3D(Render3DEvent event) {
		GlStateManager.pushMatrix();
        final float[][] colour = new float[1][1];
        mc.theWorld.loadedEntityList.forEach(entity -> {
        	if (entity != mc.thePlayer && (entity instanceof EntityPlayer && ((Targeting)mc.hackedClient.getModuleManager().getModule("Targeting")).players.getValBoolean())
                    || (entity instanceof EntityMob && ((Targeting)mc.hackedClient.getModuleManager().getModule("Targeting")).monsters.getValBoolean())
                    || (entity instanceof EntitySlime && ((Targeting)mc.hackedClient.getModuleManager().getModule("Targeting")).monsters.getValBoolean())
                    || (entity instanceof EntityAnimal && ((Targeting)mc.hackedClient.getModuleManager().getModule("Targeting")).animals.getValBoolean())
                    || (entity instanceof EntityPig && ((Targeting)mc.hackedClient.getModuleManager().getModule("Targeting")).animals.getValBoolean())
                    || (entity instanceof EntityVillager && ((Targeting)mc.hackedClient.getModuleManager().getModule("Targeting")).villagers.getValBoolean())
                    || (entity instanceof EntityGolem && ((Targeting)mc.hackedClient.getModuleManager().getModule("Targeting")).golems.getValBoolean())) {
        		EntityLivingBase player = (EntityLivingBase) entity;
        		if(!entity.getName().equalsIgnoreCase("Freecam")) {
        			colour[0] = this.getColorByDistance(player);
                    this.drawLineToEntity(player, colour[0][0], colour[0][1], colour[0][2], colour[0][3]);
                    return;
        		}
        	}
        });
        GlStateManager.popMatrix();
	}
	
	public double interpolate(final double now, final double then) {
        return then + (now - then) * mc.timer.renderPartialTicks;
    }

    public double[] interpolate(final Entity entity) {
        final double posX = this.interpolate(entity.posX, entity.lastTickPosX) - mc.getRenderManager().renderPosX;
        final double posY = this.interpolate(entity.posY, entity.lastTickPosY) - mc.getRenderManager().renderPosY;
        final double posZ = this.interpolate(entity.posZ, entity.lastTickPosZ) - mc.getRenderManager().renderPosZ;
        return new double[] { posX, posY, posZ };
    }

    public void drawLineToEntity(final Entity e, final float red, final float green, final float blue, final float opacity) {
        final double[] xyz = this.interpolate(e);
        this.drawLine(xyz[0], xyz[1], xyz[2], e.height, red, green, blue, opacity);
    }

    public void drawLine(final double posx, final double posy, final double posz, final double up, final float red, final float green, final float blue, final float opacity) {
        final Vec3d eyes = new Vec3d(0.0, 0.0, 1.0).rotatePitch(-(float)Math.toRadians(mc.thePlayer.rotationPitch)).rotateYaw(-(float)Math.toRadians(mc.thePlayer.rotationYaw));
        drawLineFromPosToPos(eyes.xCoord, eyes.yCoord + mc.thePlayer.getEyeHeight() + (float) 0f, eyes.zCoord, posx, posy, posz, up, red, green, blue, opacity);

    }

    public void drawLineFromPosToPos(final double posx, final double posy, final double posz, final double posx2, final double posy2, final double posz2, final double up, final float red, final float green, final float blue, final float opacity) {
        GL11.glBlendFunc(770, 771);
        GL11.glEnable(3042);
        GL11.glLineWidth((float) 1);
        GL11.glDisable(3553);
        GL11.glDisable(2929);
        GL11.glDepthMask(false);
        GL11.glColor4f(red, green, blue, opacity);
        GlStateManager.disableLighting();
        GL11.glLoadIdentity();
        mc.entityRenderer.orientCamera(mc.timer.renderPartialTicks);
        GL11.glBegin(1);
        GL11.glVertex3d(posx, posy, posz);
        GL11.glVertex3d(posx2, posy2, posz2);
        GL11.glVertex3d(posx2, posy2, posz2);
        GL11.glEnd();
        GL11.glEnable(3553);
        GL11.glEnable(2929);
        GL11.glDepthMask(true);
        GL11.glDisable(3042);
        GL11.glColor3d(1.0, 1.0, 1.0);
        GlStateManager.enableLighting();
    }

    public float[] getColorByDistance(final Entity entity) {
        if (entity instanceof EntityPlayer && mc.hackedClient.getFriendManager().isFriend(entity.getName())) {
            return new float[] { (85 / 255), (85 / 255), (255 / 255), 1.0f };
        }
        final Color col = new Color(Color.HSBtoRGB((float)(Math.max(0.0, Math.min(mc.thePlayer.getDistanceSqToEntity(entity), 2500.0f) / 2500.0f) / 3.0), 1.0f, 1.0f) | 0xFF000000);
        return new float[] { col.getRed() / 255.0f, col.getGreen() / 255.0f, col.getBlue() / 255.0f, 1.0f };
    }


}
