package rip.helium.module.modules.movement;

import java.awt.Color;

import org.lwjgl.opengl.GL11;

import net.minecraft.entity.Entity;
import rip.helium.event.EventTarget;
import rip.helium.event.events.impl.player.MoveEvent;
import rip.helium.event.events.impl.player.UpdateEvent;
import rip.helium.event.events.impl.render.Render3DEvent;
import rip.helium.module.Module;
import rip.helium.module.modules.combat.KillAura;
import rip.helium.setting.Setting;
import rip.helium.utils.entity.PlayerUtils;
import rip.helium.utils.render.ColorUtils;

public class TargetStrafe extends Module {

	private static Setting holdSpace;
	private static Setting distance;
	//private Setting speed;
	
	private static int direction = -1;
	
	public TargetStrafe(int bind, String name, String displayName, Category category) {
		super(bind, name, displayName, category);
		this.setColor(ColorUtils.generateColor());
		
		this.holdSpace = new Setting("Hold Space", this, true);
		this.distance = new Setting("Distance", this, 2.5, 0.1, 6.0, false);
		//this.speed = new Setting("Speed", this, 0.2875, 0.1, 5, false);
		
		mc.hackedClient.getSettingManager().addSetting(this.holdSpace);
		mc.hackedClient.getSettingManager().addSetting(this.distance);
		//mc.hackedClient.getSettingManager().addSetting(this.speed);
	}
	
	@EventTarget
	public void onUpdate(UpdateEvent event) {
		if(event.isPre()) {
			if (mc.thePlayer.isCollidedHorizontally) {
                switchDirection();
            }
		}
	}
	
	private void switchDirection() {
        if (direction == 1) {
            direction = -1;
        } else {
            direction = 1;
        }
    }
	
	public static final boolean doStrafeAtSpeed(MoveEvent event, final double moveSpeed) {
        final boolean strafe = canStrafe();
        if (strafe) {
            float[] rotations = PlayerUtils.getNeededRotations((((KillAura)mc.hackedClient.getModuleManager().getModule("KillAura")).targetList.get(((KillAura)mc.hackedClient.getModuleManager().getModule("KillAura")).targetIndex)));
            if (mc.thePlayer.getDistanceToEntity(((KillAura)mc.hackedClient.getModuleManager().getModule("KillAura")).targetList.get(((KillAura)mc.hackedClient.getModuleManager().getModule("KillAura")).targetIndex)) <= distance.getValDouble()) {
                PlayerUtils.setCockSpeed(event, moveSpeed, rotations[0], direction, 0);
            } else {
                PlayerUtils.setCockSpeed(event, moveSpeed, rotations[0], direction, 1);
            }
        }
        return strafe;
    }
	
	double nigger = 0;
    boolean up;
    
    @EventTarget
    public void onRender3D(Render3DEvent event) {
    	 if (canStrafe()) {
             Color color = ColorUtils.setRainbow(-6000, 1);
             drawCircle(((KillAura)mc.hackedClient.getModuleManager().getModule("KillAura")).targetList.get(((KillAura)mc.hackedClient.getModuleManager().getModule("KillAura")).targetIndex), event.getPartialTicks(), distance.getValDouble(), color);
         }
    }

    private void drawCircle(Entity entity, float partialTicks, double rad, Color color) {
        GL11.glPushMatrix();
        GL11.glColor3d(color.getRed(), color.getGreen(), color.getBlue());
        GL11.glDisable(GL11.GL_TEXTURE_2D);
        GL11.glDisable(GL11.GL_DEPTH_TEST);
        GL11.glDepthMask(false);
        GL11.glLineWidth(5.5f);
        GL11.glBegin(GL11.GL_LINE_STRIP);

        final double x = entity.lastTickPosX + (entity.posX - entity.lastTickPosX) * partialTicks - mc.getRenderManager().viewerPosX;
        final double y = entity.lastTickPosY + (entity.posY - entity.lastTickPosY) * partialTicks - mc.getRenderManager().viewerPosY + nigger;
        final double z = entity.lastTickPosZ + (entity.posZ - entity.lastTickPosZ) * partialTicks - mc.getRenderManager().viewerPosZ;

        if (nigger < 1.8) {
            if (up) {
                nigger = nigger + 0.01;
            }
            up = true;
        } else {
            nigger = 0;
        }


        final double pix2 = Math.PI * 2.0D;
        //ChatUtil.chat(color.getRed() + " is red, " + color.getBlue() + " is blue, " + color.getGreen());
        for (int i = 0; i <= 90; ++i) {
            //ChatUtil.chat(color.getRGB() + " hi");
            //GL11.glColor3d(color.getRed(), color.getGreen(), color.getBlue());
           // for (int k = 1; z < 5; z++) {
            GL11.glVertex3d(x + 1 * Math.cos(i * pix2 / 45.0), y, z + 1 * Math.sin(i * pix2 / 45.0));
        }

        GL11.glEnd();
        GL11.glDepthMask(true);
        GL11.glEnable(GL11.GL_DEPTH_TEST);
        GL11.glEnable(GL11.GL_TEXTURE_2D);
        GL11.glPopMatrix();
    }

    public static boolean canStrafe() {
        if (holdSpace.getValBoolean() && !mc.gameSettings.keyBindJump.isKeyDown())
        	return false;
        if ((mc.hackedClient.getModuleManager().getModule("KillAura").getState()) && (((KillAura)mc.hackedClient.getModuleManager().getModule("KillAura")).targetList.get(((KillAura)mc.hackedClient.getModuleManager().getModule("KillAura")).targetIndex) != null)) {
            return true;
        } else {
            return false;
        }
    }

}
