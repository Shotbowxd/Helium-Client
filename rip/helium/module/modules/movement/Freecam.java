package rip.helium.module.modules.movement;

import java.util.UUID;

import com.mojang.authlib.GameProfile;

import net.minecraft.client.entity.EntityOtherPlayerMP;
import net.minecraft.network.play.client.C03PacketPlayer;
import rip.helium.event.EventTarget;
import rip.helium.event.events.impl.player.BlockPushEvent;
import rip.helium.event.events.impl.player.BoundingBoxEvent;
import rip.helium.event.events.impl.player.MoveEvent;
import rip.helium.event.events.impl.player.UpdateEvent;
import rip.helium.event.events.impl.render.InsideBlockRenderEvent;
import rip.helium.module.Module;
import rip.helium.utils.entity.PlayerUtils;
import rip.helium.utils.render.ColorUtils;

public class Freecam extends Module {

	private double x, y, z, yaw, pitch;
	
	public Freecam(int bind, String name, Category category) {
		super(bind, name, category);
		this.setColor(ColorUtils.generateColor());
		this.setHidden(true);
	}
	
	@Override
	public void onEnable() {
		super.onEnable();
		if(mc.theWorld != null) {
			this.x = mc.thePlayer.posX;
            this.y = mc.thePlayer.posY;
            this.z = mc.thePlayer.posZ;
            this.yaw = mc.thePlayer.rotationYaw;
            this.pitch = mc.thePlayer.rotationPitch;
            EntityOtherPlayerMP entityOtherPlayerMP = new EntityOtherPlayerMP(mc.theWorld, new GameProfile(mc.thePlayer.getGameProfile().getId(), "Freecam"));
            entityOtherPlayerMP.inventory = mc.thePlayer.inventory;
            entityOtherPlayerMP.inventoryContainer = mc.thePlayer.inventoryContainer;
            entityOtherPlayerMP.setPositionAndRotation(this.x, mc.thePlayer.getEntityBoundingBox().minY, this.z, mc.thePlayer.rotationYaw, mc.thePlayer.rotationPitch);
            entityOtherPlayerMP.rotationYawHead = mc.thePlayer.rotationYawHead;
            entityOtherPlayerMP.setSneaking(mc.thePlayer.isSneaking());

            mc.theWorld.addEntityToWorld(-6969, entityOtherPlayerMP);
		}
	}
	
	@Override
	public void onDisable() {
		super.onDisable();
		if(mc.theWorld != null) {
			mc.thePlayer.jumpMovementFactor = 0.02f;
            mc.thePlayer.setPosition(this.x, this.y, this.z);
            mc.thePlayer.sendQueue.addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(mc.thePlayer.posX, mc.thePlayer.posY + 0.01, mc.thePlayer.posZ, mc.thePlayer.onGround));
            mc.thePlayer.noClip = false;
            mc.theWorld.removeEntityFromWorld(-6969);
            mc.thePlayer.motionY = 0.0;
            mc.thePlayer.rotationPitch = (float) pitch;
            mc.thePlayer.rotationYaw = (float) yaw;
            yaw = pitch = 0;
		}
		mc.renderGlobal.loadRenderers();
	}
	
	@EventTarget
	public void onUpdate(UpdateEvent event) {
		mc.thePlayer.setVelocity(0.0, 0.0, 0.0);
        mc.thePlayer.jumpMovementFactor = 1;
        if (mc.currentScreen == null) {
            if (mc.gameSettings.isKeyDown(mc.gameSettings.keyBindJump)) {
                mc.thePlayer.motionY += 1;
            }
            if (mc.gameSettings.isKeyDown(mc.gameSettings.keyBindSneak)) {
                mc.thePlayer.motionY -= 1;
            }
        }
        mc.thePlayer.noClip = true;
        mc.thePlayer.renderArmPitch = 5000.0f;
	}
	
	@EventTarget
	public void onMove(MoveEvent event) {
		PlayerUtils.setMoveSpeed(event, 1);
	}
	
	@EventTarget
	public void onBB(BoundingBoxEvent event) {
		event.setCancelled(true);
	}
	
	@EventTarget
	public void onPush(BlockPushEvent event) {
		event.setCancelled(true);
	}
	
	@EventTarget
	public void onInsideBlockRender(InsideBlockRenderEvent event) {
		event.setCancelled(true);
	}

}
