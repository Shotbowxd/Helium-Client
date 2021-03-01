package rip.helium.module.modules.movement;

import java.util.ArrayList;

import net.minecraft.network.Packet;
import net.minecraft.network.play.client.C03PacketPlayer;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.ChatComponentText;
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

	//rinaorc fly
	boolean allowSendPacket = false;

	ArrayList<Packet> packets = new ArrayList<>();
	
	private Timer flyTimer;
	
	public Flight(int bind, String name, Category category) {
		super(bind, name, category);
		this.setColor(ColorUtils.generateColor());
		
		ArrayList<String> modes = new ArrayList<String>();
		modes.add("Motion");
		modes.add("Notch");
		modes.add("Shotbow");
		modes.add("Emeraldcraft");
		modes.add("Dynamic");
		modes.add("RinaOrc");

		this.mode = new Setting("Mode", this, "Motion", modes);
		this.speed = new Setting("Speed", this, 1.0d, 0.1d, 10.0d, false);
		this.antiKick = new Setting("AntiKick", this, false);
		
		mc.hackedClient.getSettingManager().addSetting(this.mode);
		mc.hackedClient.getSettingManager().addSetting(this.speed);
		mc.hackedClient.getSettingManager().addSetting(this.antiKick);
		
		this.flyTimer = new Timer();
	}

	@EventTarget
	public void onPacket(PacketSendEvent event) {
        switch(this.mode.getValString()) {
			case "RinaOrc":
				/*/if (event.getPacket() instanceof C03PacketPlayer) {
					event.setCancelled(true);
					packets.add(event.getPacket());
					mc.thePlayer.addChatMessage(new ChatComponentText("lol canceled"));
				}/*/
				break;
		}
    }
	
	@EventTarget
	public void onUpdate(UpdateEvent event) {
		this.setSuffix(this.mode.getValString());
		switch(this.mode.getValString()) {
			case "Notch":
				mc.thePlayer.capabilities.isFlying = true;
				break;
			case "RinaOrc":
				break;
			case "Emeraldcraft":
				mc.timer.timerSpeed = 0.4F;
				//mc.thePlayer.capabilities.setFlySpeed(1.5F);
			    //mc.thePlayer.capabilities.isFlying = true;
				if(mc.thePlayer.movementInput.jump) {
					if(mc.thePlayer.ticksExisted%3==0) {
						mc.thePlayer.motionY = 1.5d;
					} else {
						mc.thePlayer.motionY = 1d;
					}
				} else if(mc.thePlayer.movementInput.sneak) {
					if(mc.thePlayer.ticksExisted%3==0) {
						mc.thePlayer.motionY = -1.5d;
					} else {
						mc.thePlayer.motionY = -1d;
					}
				} else {
					mc.thePlayer.motionY = 0.0d;
				}
			    if(PlayerUtils.isMoving()) {
			    	if(mc.thePlayer.ticksExisted%3==0) {
			    		PlayerUtils.setMoveSpeed(5d);
			    	} else {
			    		PlayerUtils.setMoveSpeed(4.5d);
			    	}
			    }
			    break;
		}
		if(this.antiKick.getValBoolean()) {
            if(!mc.thePlayer.onGround) {
            	if(flyTimer.hasPassed(75)) {
            		this.fall();
            		this.flyTimer.updateLastTime();
            	} else {
            		this.ascend();
            	}
            }
		}
	}
	
	@EventTarget
	public void onMove(MoveEvent event) {
		switch(this.mode.getValString()) {
			case "RinaOrc":
				if (mc.thePlayer.ticksExisted % 10 == 0) {
					if (mc.thePlayer.isMoving()) {
						if (mc.thePlayer.movementInput.jump) {
							event.setY(mc.thePlayer.motionY = 1.5d);
						} else if (mc.thePlayer.movementInput.sneak) {
							event.setY(mc.thePlayer.motionY = -1.5d);
						} else {
							event.setY(mc.thePlayer.motionY = 0.0d);
						}
						PlayerUtils.setMoveSpeed(event, speed.getValDouble());
					}
				}
				break;
			case "Motion":
				if(mc.thePlayer.movementInput.jump) {
					event.setY(mc.thePlayer.motionY = 1.5d);
				} else if(mc.thePlayer.movementInput.sneak) {
					event.setY(mc.thePlayer.motionY = -1.5d);
				} else {
					event.setY(mc.thePlayer.motionY = 0.0d);
				}
				PlayerUtils.setMoveSpeed(event, speed.getValDouble());
				break;
			case "Notch":
				PlayerUtils.setMoveSpeed(event, speed.getValDouble());
				break;
			case "Shotbow":
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
			case "Dynamic":
				double yaw1 = Math.toRadians(mc.thePlayer.rotationYaw);
				double x1 = -Math.sin(yaw1) * 3.8;
				double z1 = Math.cos(yaw1) * 3.8;
				if (mc.thePlayer.isMoving()) {
					if (mc.thePlayer.ticksExisted % 5 == 0) {
						mc.thePlayer.setPosition(mc.thePlayer.posX + x1 , mc.thePlayer.posY, mc.thePlayer.posZ + z1);
					}
				}
				if (mc.gameSettings.keyBindJump.pressed) {
					if (mc.thePlayer.ticksExisted % 5 == 0) {
						mc.thePlayer.setPosition(mc.thePlayer.posX + x1 , mc.thePlayer.posY + 5, mc.thePlayer.posZ + z1);
					}
					PlayerUtils.setMoveSpeed(event, 0.0);
				}
				if (mc.gameSettings.keyBindSneak.pressed) {
					event.setY(mc.thePlayer.motionY = -1.5d);
					PlayerUtils.setMoveSpeed(event, 0.0);
				}
				mc.thePlayer.motionY = 0.0f;
				break;
		}
	}
	
	@Override
	public void onDisable() {
	    super.onDisable();
	    mc.timer.timerSpeed = 1f;
	    mc.thePlayer.capabilities.setFlySpeed(0.1F);
	    mc.thePlayer.capabilities.isFlying = false;
	    PlayerUtils.setSpeed(0d);

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
