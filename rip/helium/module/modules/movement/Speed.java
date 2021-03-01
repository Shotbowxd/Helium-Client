package rip.helium.module.modules.movement;

import java.util.ArrayList;

import net.minecraft.network.play.client.C03PacketPlayer;
import net.minecraft.potion.Potion;
import rip.helium.event.EventTarget;
import rip.helium.event.events.impl.network.PacketReceiveEvent;
import rip.helium.event.events.impl.network.PacketSendEvent;
import rip.helium.event.events.impl.player.MoveEvent;
import rip.helium.event.events.impl.player.UpdateEvent;
import rip.helium.module.Module;
import rip.helium.module.modules.movement.speed.BhopMode;
import rip.helium.module.modules.movement.speed.FasthopMode;
import rip.helium.module.modules.movement.speed.GroundMode;
import rip.helium.module.modules.movement.speed.ViperMode;
import rip.helium.setting.Setting;
import rip.helium.utils.client.Timer;
import rip.helium.utils.entity.PlayerUtils;
import rip.helium.utils.misc.MathUtils;
import rip.helium.utils.render.ColorUtils;

public class Speed extends Module {

	private Setting mode;
	
	Timer ncpTimer;

	private double moveSpeed;
	private double lastDist;
	private int stage;

	private boolean doSlow;

	private BhopMode bhop;
	private FasthopMode fasthop;
	private GroundMode ground;
	private ViperMode viper;
	
	public Speed(int bind, String name, Category category) {
		super(bind, name, category);
		this.setColor(ColorUtils.generateColor());
		
		ArrayList<String> modes = new ArrayList<String>();
		modes.add("Bhop");
		modes.add("Fasthop");
		modes.add("Ground");
		modes.add("Viper");
		modes.add("Fierce");
		modes.add("SunPvP");
		modes.add("Emeraldcraft");
		modes.add("Timer");
		modes.add("Dynamic");
		modes.add("Hypixel");
		modes.add("VerusTest");
		
		this.mode = new Setting("Mode", this, "Ground", modes);
		
		mc.hackedClient.getSettingManager().addSetting(mode);
		
		this.ncpTimer = new Timer();
		
		this.bhop = new BhopMode();
		this.fasthop = new FasthopMode();
		this.ground = new GroundMode();
		this.viper = new ViperMode();
	}
	
	@Override
	public void onDisable() {
		super.onDisable();
		mc.timer.timerSpeed = 1f;
		PlayerUtils.setSpeed(0d);
	}
	
	@EventTarget
	public void onMove(MoveEvent event) {
		switch(this.mode.getValString()) {
			case "Bhop":
				this.bhop.onMove(event);
				break;
			case "Fasthop":
				this.fasthop.onMove(event);
				break;
			case "Ground":
				this.ground.onMove(event);
				break;
			case "Viper":
				if (mc.thePlayer.onGround) {
					mc.thePlayer.motionY = 0.4255;
				}
				PlayerUtils.setMoveSpeed(0.8);
				break;
			case "VerusTest": {
				if (ncpTimer.hasPassed(35)) {
					int randomPos = MathUtils.getRandomInRange(3, 34);
					mc.thePlayer.sendQueue.addToSendQueue(new C03PacketPlayer.C06PacketPlayerPosLook(mc.thePlayer.posX, mc.thePlayer.posY - randomPos, mc.thePlayer.posZ,
							mc.thePlayer.rotationYaw, mc.thePlayer.rotationPitch, true));
					ncpTimer.updateLastTime();
				}
				if (mc.thePlayer.isMoving()) {
					PlayerUtils.setMoveSpeed(event, 7.4);
				}
				break;
			}
			case "SunPvP":
	            double sunspeed = PlayerUtils.getBaseMoveSpeed();
	            if (mc.thePlayer.moveForward != 0.0f || mc.thePlayer.moveStrafing != 0.0f) {
	                PlayerUtils.setMoveSpeed(event, 1);
	                PlayerUtils.setMoveSpeed(event, 1);
	                if (mc.thePlayer.onGround) {
	                    event.setY(mc.thePlayer.motionY = 0.42); // 3999998
	                    //  moveSpeed = sunspeed * 2.15 - 1.0E-4;
	                }
	            }
	            break;
	        case "Fierce":
	        	double yaw = Math.toRadians(mc.thePlayer.rotationYaw);
                double x = -Math.sin(yaw) * 3.5;
                double z = Math.cos(yaw) * 3.5;
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
			case "Timer":
				net.minecraft.util.Timer.timerSpeed = 1.45f;
				break;
			case "Hypixel":
				if (MathUtils.round(mc.thePlayer.motionY, 3) == MathUtils.round(-.245D, 3)) {
					event.setY(mc.thePlayer.motionY -= 0.138F);
				}
				if (mc.thePlayer.isMoving() && !mc.thePlayer.isInWater()) {
					double baseSpeed = getBaseMoveSpeed();
					if (mc.thePlayer.onGround) {
						event.setY(mc.thePlayer.motionY = getJumpBoostModifier(0.3999998F)); // 3999998
						moveSpeed = baseSpeed * 2.15 - 1.0E-4;
						doSlow = true;
					} else if (doSlow || mc.thePlayer.isCollidedHorizontally) {
						moveSpeed -= 0.6336 * (moveSpeed - baseSpeed);
						doSlow = false;
					} else {
						moveSpeed -= moveSpeed / 159;
					}

					if (mc.thePlayer.isCollidedHorizontally || !mc.thePlayer.isMoving())
						moveSpeed = getBaseMoveSpeed();

					PlayerUtils.setMoveSpeed(event, Math.max(moveSpeed, baseSpeed));
					if (!TargetStrafe.doStrafeAtSpeed(event, Math.max(moveSpeed, baseSpeed))) {
						PlayerUtils.setMoveSpeed(event, Math.max(moveSpeed, baseSpeed));
					}
				}
				break;
			case "Dynamic":
				break;
		}
	}
	
	@EventTarget
	public void onUpdate(UpdateEvent event) {
		this.setSuffix(this.mode.getValString());
		switch(this.mode.getValString()) {
		case "Emeraldcraft":
			mc.timer.timerSpeed = 0.75F;
		      if (PlayerUtils.isMoving() && !(mc.hackedClient.getModuleManager().getModule("TargetStrafe").getState()))
		    	  if(mc.thePlayer.ticksExisted%3==0) {
		    		  PlayerUtils.setMoveSpeed(0.75d);
		    	  } else {
		    		  PlayerUtils.setMoveSpeed(1d);
		    	  }
			break;
		}
	}

	public static double getBaseMoveSpeed() {
		double baseSpeed = 0.2873;
		if (mc.thePlayer.isPotionActive(Potion.moveSpeed)) {
			final int amplifier = mc.thePlayer.getActivePotionEffect(Potion.moveSpeed).getAmplifier();
			baseSpeed *= 1.0 + 0.2 * (amplifier + 1);
		}
		return baseSpeed;
	}

	public static double getJumpBoostModifier(double baseJumpHeight) {
		if (mc.thePlayer.isPotionActive(Potion.jump)) {
			int amplifier = mc.thePlayer.getActivePotionEffect(Potion.jump).getAmplifier();
			baseJumpHeight += (float) (amplifier + 1) * 0.1F;
		}

		return baseJumpHeight;
	}
	
}
