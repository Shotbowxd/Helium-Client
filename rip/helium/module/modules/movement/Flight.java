package rip.helium.module.modules.movement;

import java.util.ArrayList;

import net.minecraft.network.play.client.C03PacketPlayer;
import net.minecraft.util.AxisAlignedBB;
import rip.helium.event.EventTarget;
import rip.helium.event.events.impl.network.PacketSendEvent;
import rip.helium.event.events.impl.player.MoveEvent;
import rip.helium.event.events.impl.player.UpdateEvent;
import rip.helium.module.Module;
import rip.helium.setting.Setting;
import rip.helium.utils.client.Timer;
import rip.helium.utils.entity.PlayerUtils;
import rip.helium.utils.render.ColorUtils;

public class Flight extends Module {

	private Setting mode;
	private Setting speed;
	private Setting antiKick;
	
	private Timer flyTimer;
	
	public Flight(int bind, String name, Category category) {
		super(bind, name, category);
		this.setColor(ColorUtils.generateColor());
		
		ArrayList<String> modes = new ArrayList<String>();
		modes.add("Motion");
		modes.add("Notch");
		modes.add("AAC");
		
		this.mode = new Setting("Mode", this, "Motion", modes);
		this.speed = new Setting("Speed", this, 1.0d, 0.1d, 10.0d, false);
		this.antiKick = new Setting("AntiKick", this, false);
		
		mc.hackedClient.getSettingManager().addSetting(this.mode);
		mc.hackedClient.getSettingManager().addSetting(this.speed);
		mc.hackedClient.getSettingManager().addSetting(this.antiKick);
		
		this.flyTimer = new Timer();
	}
	
	public void onPacket(PacketSendEvent event) {
        switch(this.mode.getValString()) {
        case "Shotbow":
            if (event.getPacket() instanceof C03PacketPlayer) {
                event.setCancelled(true);
            }
        }
    }
	
	@EventTarget
	public void onUpdate(UpdateEvent event) {
		this.setSuffix(this.mode.getValString());
		switch(this.mode.getValString()) {
			case "Notch":
				mc.thePlayer.capabilities.isFlying = true;
				break;
		}
		if(this.antiKick.getValBoolean() && this.flyTimer.hasPassed(75.0f)) {
            if(!mc.thePlayer.onGround) {
            	this.fall();
                this.ascend();
            }
            this.flyTimer.updateLastTime();
		}
	}
	
	@EventTarget
	public void onMove(MoveEvent event) {
		switch(this.mode.getValString()) {
			case "Motion":
				if(mc.thePlayer.movementInput.jump) {
					event.setY(mc.thePlayer.motionY = 1.5d);
				} else if(mc.thePlayer.movementInput.sneak) {
					event.setY(mc.thePlayer.motionY = -1.5d);
				} else {
					event.setY(mc.thePlayer.motionY = 0.0d);
				}
				break;
			case "AAC":
				mc.timer.timerSpeed = 0.75f;
                float speed = 10 / 2.25f;
                PlayerUtils.setMoveSpeed(event, speed);
                event.setY(mc.thePlayer.movementInput.jump ? speed / 3 : mc.thePlayer.movementInput.sneak ? -speed / 3 : 0.42F);
                if (mc.thePlayer.ticksExisted % 2 == 0) {
                mc.getNetHandler().getNetworkManager().sendPacket(new C03PacketPlayer.C06PacketPlayerPosLook(mc.thePlayer.posX + mc.thePlayer.motionX, mc.thePlayer.posY + mc.thePlayer.motionY + 0.0001, mc.thePlayer.posZ + mc.thePlayer.motionY, mc.thePlayer.rotationYaw, mc.thePlayer.rotationPitch, true));
                mc.getNetHandler().getNetworkManager().sendPacket(new C03PacketPlayer.C06PacketPlayerPosLook(mc.thePlayer.posX, mc.thePlayer.posY, mc.thePlayer.posZ + 20.0D, mc.thePlayer.rotationYaw, mc.thePlayer.rotationPitch, true));
            }
            mc.thePlayer.motionY = 0.0f;
				break;
		}
		PlayerUtils.setMoveSpeed(event, speed.getValDouble());
	}
	
	@Override
	public void onDisable() {
		mc.thePlayer.capabilities.isFlying = false;
		mc.timer.timerSpeed = 1;
		super.onDisable();
	}
	
    private boolean isColliding(final AxisAlignedBB box) {
        return mc.theWorld.checkBlockCollision(box);
    }
	
    public double getGroundLevel() {
        for (int i = (int) Math.round(mc.thePlayer.posY); i > 0; --i) {
            final AxisAlignedBB box = mc.thePlayer.boundingBox.addCoord(0.0, 0.0, 0.0);
            box.minY = i - 1;
            box.maxY = i;
            if (this.isColliding(box) && box.minY <= mc.thePlayer.posY) {
                return i;
            }
        }
        return 0.0;
    }
	
    public void fall() {
    	mc.thePlayer.sendQueue.addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(mc.thePlayer.posX, mc.thePlayer.posY - 0.035, mc.thePlayer.posZ, true));
    }

    private void ascend() {
    	mc.thePlayer.sendQueue.addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(mc.thePlayer.posX, mc.thePlayer.posY + 0.035, mc.thePlayer.posZ, true));
    }

}