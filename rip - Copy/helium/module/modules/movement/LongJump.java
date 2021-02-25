package rip.helium.module.modules.movement;

import java.util.ArrayList;

import net.minecraft.network.play.client.C00PacketKeepAlive;
import net.minecraft.network.play.client.C03PacketPlayer;
import net.minecraft.network.play.client.C09PacketHeldItemChange;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import rip.helium.event.EventTarget;
import rip.helium.event.events.impl.network.PacketSendEvent;
import rip.helium.event.events.impl.player.MoveEvent;
import rip.helium.event.events.impl.player.UpdateEvent;
import rip.helium.module.Module;
import rip.helium.setting.Setting;
import rip.helium.utils.entity.PlayerUtils;
import rip.helium.utils.misc.Vec3d;
import rip.helium.utils.render.ColorUtils;

public class LongJump extends Module {

	private final Setting mode;
    private int i = 0;
    private double mineplexSpeed;
    private boolean done;
    private boolean back;
    private int setback;
    private int direction;
	
	public LongJump(int bind, String name, String displayName, Category category) {
		super(bind, name, displayName, category);
		this.setColor(ColorUtils.generateColor());
		
		ArrayList<String> modes = new ArrayList<String>();
		modes.add("Vanilla");
		modes.add("Mineplex");
		modes.add("Fierce");
		modes.add("SunPvP");
		modes.add("Emeraldcraft");
		
		this.mode = new Setting("Mode", this, "Vanilla", modes);
		
		mc.hackedClient.getSettingManager().addSetting(this.mode);
	}
	
	@EventTarget
	public void onEnable() {
		super.onEnable();
		this.back = false;
        this.done = false;
        this.mineplexSpeed = 0.2;
	}
	
	@EventTarget
	public void onDisable() {
		super.onDisable();
		mc.timer.timerSpeed = 1f;
		PlayerUtils.setSpeed(0);
        PlayerUtils.setMoveSpeed(0);
	}
	@EventTarget
	public void onMove(MoveEvent event) {
		this.setSuffix(this.mode.getValString());
		switch (mode.getValString()) {
        case "Mineplex": {
            if (PlayerUtils.airSlot() == -10) {
                PlayerUtils.setMoveSpeed2(event, 0);
                return;
            }
            if (!done) {
                mc.thePlayer.sendQueue.addToSendQueueNoEvent(new C09PacketHeldItemChange(PlayerUtils.airSlot()));
                placeHeldItemUnderPlayer();
                PlayerUtils.setMoveSpeed(event, back ? -mineplexSpeed : mineplexSpeed);
                back = !back;
                if ((mc.thePlayer.moveForward != 0.0f || mc.thePlayer.moveStrafing != 0.0f) && mc.thePlayer.onGround && mc.thePlayer.ticksExisted % 2 == 0) {
                    mineplexSpeed += 0.135;
                }
                if ((mc.thePlayer.moveForward != 0.0f || mc.thePlayer.moveStrafing != 0.0f) && mc.thePlayer.onGround) {
                    event.setY(mc.thePlayer.motionY = 0.42f);
                    PlayerUtils.setMoveSpeed2(event, 0);
                    done = true;
                }
            } else {
                mc.thePlayer.motionY += mc.thePlayer.fallDistance == 0 ? 0.038 : mc.thePlayer.fallDistance > 1.4 ? 0.032 : 0;
                if (mc.thePlayer.ticksExisted % 2 == 0) {
                	mc.timer.timerSpeed = 0.1f;
                    PlayerUtils.setMoveSpeed2(2500.5);
                } else {
                	mc.timer.timerSpeed = 0.7f;
                    PlayerUtils.setMoveSpeed2(5.1);
                }
                PlayerUtils.setMoveSpeed2(event, mineplexSpeed *= (mineplexSpeed < 1 ? 1.19 : mineplexSpeed < 2 ? 0.985 : mineplexSpeed < 2.5 ? 0.972 : 0.97));
                if ((mc.thePlayer.moveForward != 0.0f || mc.thePlayer.moveStrafing != 0.0f) && mc.thePlayer.onGround) {
                    done = false;
                }
                if (mc.thePlayer.onGround) {
                    PlayerUtils.setMoveSpeed2(0);
                }
            }
            break;
        }
        case "Vanilla": {
            if (mc.thePlayer.onGround) {
                event.setY(mc.thePlayer.motionY = 0.4);
            }
            PlayerUtils.setMoveSpeed2(event, 9.2);
            break;
        }
        case "Fierce": {
            double yaw = Math.toRadians(mc.thePlayer.rotationYaw);
            double x = -Math.sin(yaw) * 5.9;
            double z = Math.cos(yaw) * 5.9;
            //mc.thePlayer.motionY = 0;
            mc.thePlayer.lastReportedPosY = 0;
            //mc.thePlayer.onGround = false;

            if (mc.thePlayer.onGround) {
                event.setY(mc.thePlayer.motionY = 0.369432141234234);
            }

            if (mc.thePlayer.ticksExisted % 5 == 0 && mc.gameSettings.keyBindForward.pressed) {
                mc.timer.timerSpeed = 1f;
                mc.thePlayer.setPosition(mc.thePlayer.posX + x, mc.thePlayer.posY, mc.thePlayer.posZ + z);
            } else if (mc.thePlayer.ticksExisted % 2 == 0 && !mc.gameSettings.keyBindForward.pressed && !mc.thePlayer.onGround) {
            	mc.timer.timerSpeed = 1f;
                mc.thePlayer.setPosition(mc.thePlayer.posX, mc.thePlayer.posY, mc.thePlayer.posZ);
                PlayerUtils.setMoveSpeed2(0f);
            } else {
                PlayerUtils.setMoveSpeed2(0f);
            }
            break;
        }
        case "SunPvP":
            double sunspeed = PlayerUtils.getBaseMoveSpeed();
            if (mc.thePlayer.moveForward != 0.0f || mc.thePlayer.moveStrafing != 0.0f) {
                PlayerUtils.setMoveSpeed(event, 9.3);
                PlayerUtils.setMoveSpeed(event, 9.3);
                if (mc.thePlayer.onGround) {
                    event.setY(mc.thePlayer.motionY = 0.42); // 3999998
                    //  moveSpeed = sunspeed * 2.15 - 1.0E-4;
                }
                break;
            }
		}
	}
	
	@EventTarget
	public void onUpdate(UpdateEvent event) {
		switch (this.mode.getValString()) {
        	case "Faithful":
        		if (mc.thePlayer.onGround) {
        			event.setPosY(mc.thePlayer.motionY = 0.7);
        		}
        		PlayerUtils.setMoveSpeed2(2.5);
        		break;
        	case "Emeraldcraft":
    			mc.timer.timerSpeed = 0.75F;
    		      if (PlayerUtils.isMoving())
    		    	  if(mc.thePlayer.onGround) {
    		    		  mc.thePlayer.motionY = 0.4255;
    		    	  }
    		    	  if(mc.thePlayer.ticksExisted%3==0) {
    		    		  PlayerUtils.setMoveSpeed(5d);
    		    	  } else {
    		    		  PlayerUtils.setMoveSpeed(4.75d);
    		    	  }
    			break;
		}
	}
	
	@EventTarget
	public void onPacketSend(PacketSendEvent event) {
		switch (this.mode.getValString()) {
			case "Faithful": {
            	if (mc.thePlayer.isEating() && !mc.hackedClient.getModuleManager().getModule("Speed").getState() && !mc.hackedClient.getModuleManager().getModule("Flight").getState() && mc.thePlayer.isSneaking()) {
            		return;
            	}
            	if (mc.thePlayer != null && event.getPacket() instanceof C03PacketPlayer) {
            		event.setCancelled(true);
            		if (i > 2) {
            			mc.thePlayer.sendQueue.addToSendQueueNoEvent(new C03PacketPlayer.C06PacketPlayerPosLook(mc.thePlayer.prevPosX + ((mc.thePlayer.posX - mc.thePlayer.prevPosX) / 3), mc.thePlayer.prevPosY + ((mc.thePlayer.posY - mc.thePlayer.prevPosY) / 3), mc.thePlayer.prevPosZ + ((mc.thePlayer.posZ - mc.thePlayer.prevPosZ) / 3),
            					mc.thePlayer.rotationYaw, mc.thePlayer.rotationPitch, true));
            			i = 0;
            		} else {
            			mc.thePlayer.sendQueue.addToSendQueueNoEvent(new C00PacketKeepAlive(-Integer.MAX_VALUE));
            		}
            		i++;
            	}
            	break;
			}
		}
	}
	
    public static void placeHeldItemUnderPlayer() {
        final BlockPos blockPos = new BlockPos(mc.thePlayer.posX, mc.thePlayer.getEntityBoundingBox().minY - 1,
                mc.thePlayer.posZ);
        final Vec3d vec = new Vec3d(blockPos).addVector(0.4F, 0.4F, 0.4F);
        mc.playerController.onPlayerRightClick(mc.thePlayer, mc.theWorld, null, blockPos, EnumFacing.UP,
                vec.scale(0.4));
    }
	
}
