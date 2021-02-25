package rip.helium.utils.entity;

import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.potion.Potion;
import net.minecraft.util.MathHelper;
import rip.helium.ClientSupport;
import rip.helium.event.events.impl.player.MoveEvent;

public class PlayerUtils implements ClientSupport {
	
	public static boolean isMoving() {
        return mc.thePlayer.moveForward != 0.0f || mc.thePlayer.moveStrafing != 0.0f;
    }
	
	public static float[] getNeededRotations(EntityLivingBase entityIn) {
		double d0 = entityIn.posX - mc.thePlayer.posX;
		double d1 = entityIn.posZ - mc.thePlayer.posZ;
		double d2 = entityIn.posY + entityIn.getEyeHeight() - (mc.thePlayer.getEntityBoundingBox().minY
				+ (mc.thePlayer.getEntityBoundingBox().maxY - mc.thePlayer.getEntityBoundingBox().minY));
		double d3 = MathHelper.sqrt_double(d0 * d0 + d1 * d1);
		float f = (float) (MathHelper.func_181159_b(d1, d0) * 180.0D / Math.PI) - 90.0F;
		float f1 = (float) (-(MathHelper.func_181159_b(d2, d3) * 180.0D / Math.PI));
		return new float[] { f, f1 };
	}
	
    public static int airSlot() {
        for (int j = 0; j < 8; ++j) {
            if (mc.thePlayer.inventory.mainInventory[j] == null) {
                return j;
            }
        }
        return -10;
    }
    
    public static double getBaseMoveSpeed() {
        double baseSpeed = 0.2875;

        if (mc.thePlayer.isPotionActive(Potion.moveSpeed)) {
            baseSpeed *= 1 + .2 * (mc.thePlayer.getActivePotionEffect(Potion.moveSpeed).getAmplifier() + 1);
        }

        return baseSpeed;
    }
	
	public static void setMoveSpeed(MoveEvent event, double speed) {
		double forward = mc.thePlayer.moveForward;
		double strafe = mc.thePlayer.moveStrafing;
		float yaw = mc.thePlayer.rotationYaw;
		if(forward != 0) {
			if(strafe > 0) {
				yaw += ((forward > 0) ? -45 : 45);
			} else if(strafe < 0) {
				yaw += ((forward > 0) ? 45 : -45);
			}
			strafe = 0;
			if(forward > 0) {
				forward = 1;
			}else {
				forward = -1;
			}
		}
		event.setX(forward * speed * Math.cos(Math.toRadians((yaw + 90.0F))) + strafe * speed * Math.sin(Math.toRadians((yaw + 90.0F))));
		event.setZ(forward * speed * Math.sin(Math.toRadians((yaw + 90.0F))) - strafe * speed * Math.cos(Math.toRadians((yaw + 90.0F))));
	}
	
	public static void setMoveSpeed(double speed) {
		double forward = mc.thePlayer.moveForward;
		double strafe = mc.thePlayer.moveStrafing;
		float yaw = mc.thePlayer.rotationYaw;
		if(forward != 0) {
			if(strafe > 0) {
				yaw += ((forward > 0) ? -45 : 45);
			} else if(strafe < 0) {
				yaw += ((forward > 0) ? 45 : -45);
			}
			strafe = 0;
			if(forward > 0) {
				forward = 1;
			}else {
				forward = -1;
			}
		}
		mc.thePlayer.motionX = forward * speed * Math.cos(Math.toRadians((yaw + 90.0F))) + strafe * speed * Math.sin(Math.toRadians((yaw + 90.0F)));
		mc.thePlayer.motionZ = forward * speed * Math.sin(Math.toRadians((yaw + 90.0F))) - strafe * speed * Math.cos(Math.toRadians((yaw + 90.0F)));
	}
	
	public static void setMoveSpeedRidingEntity(double speed) {
		double forward = mc.thePlayer.moveForward;
		double strafe = mc.thePlayer.moveStrafing;
		float yaw = mc.thePlayer.rotationYaw;
		if(forward != 0) {
			if(strafe > 0) {
				yaw += ((forward > 0) ? -45 : 45);
			} else if(strafe < 0) {
				yaw += ((forward > 0) ? 45 : -45);
			}
			strafe = 0;
			if(forward > 0) {
				forward = 1;
			}else {
				forward = -1;
			}
		}
		mc.thePlayer.ridingEntity.motionX = forward * speed * Math.cos(Math.toRadians((yaw + 90.0F))) + strafe * speed * Math.sin(Math.toRadians((yaw + 90.0F)));
		mc.thePlayer.ridingEntity.motionZ = forward  * speed * Math.sin(Math.toRadians((yaw + 90.0F))) - strafe * speed * Math.cos(Math.toRadians((yaw + 90.0F)));
	}
	
	public static void setMoveSpeed2(MoveEvent e, double speed) {
        if (isMoving()) {
            final double d = 0.0;
            e.setX(-(MathHelper.sin(getDir(mc.thePlayer)) * (speed + d)));
            e.setZ(MathHelper.cos(getDir(mc.thePlayer)) * (speed + d));
        } else {
            e.setX(0.0);
            e.setZ(0.0);
        }
    }
	
	public static void setMoveSpeed2(double speed) {
        if (isMoving()) {
            final double d = 0.0;
            mc.thePlayer.motionX = -(MathHelper.sin(getDir(mc.thePlayer)) * (speed + d));
            mc.thePlayer.motionZ = MathHelper.cos(getDir(mc.thePlayer)) * (speed + d);
        } else {
            mc.thePlayer.motionX = 0.0f;
            mc.thePlayer.motionZ = 0.0f;
        }
    }
	
	public static void setSpeed(double speed) {
	    mc.thePlayer.motionX = -MathHelper.sin(getDirection()) * speed;
	    mc.thePlayer.motionZ = MathHelper.cos(getDirection()) * speed;
	}
	
	public static void setCockSpeed(final MoveEvent moveEvent, final double moveSpeed, final float pseudoYaw, final double pseudoStrafe, final double pseudoForward) {
        double forward = pseudoForward;
        double strafe = pseudoStrafe;
        float yaw = pseudoYaw;

        if (forward == 0.0 && strafe == 0.0) {
            moveEvent.setX(mc.thePlayer.motionX = 0);
            moveEvent.setZ(mc.thePlayer.motionZ = 0);
        } else {
            if (forward != 0.0) {
                if (strafe > 0.0) {
                    yaw += ((forward > 0.0) ? -45 : 45);
                } else if (strafe < 0.0) {
                    yaw += ((forward > 0.0) ? 45 : -45);
                }
                strafe = 0.0;
                if (forward > 0.0) {
                    forward = 1.0;
                } else if (forward < 0.0) {
                    forward = -1.0;
                }
            }
            final double cos = Math.cos(Math.toRadians(yaw + 90.0f));
            final double sin = Math.sin(Math.toRadians(yaw + 90.0f));

            moveEvent.setX(mc.thePlayer.motionX = (forward * moveSpeed * cos + strafe * moveSpeed * sin));
            moveEvent.setZ(mc.thePlayer.motionZ = (forward * moveSpeed * sin - strafe * moveSpeed * cos));
        }
    }
	
	public static Float getDir(final EntityPlayer player) {
        double f = player.moveForward;
        final double s = player.moveStrafing;
        double y = player.rotationYaw;
        final double st = 45.0;
        if (s != 0.0 && f == 0.0) {
            y += 360.0;
            if (s > 0.0) {
                y -= 89.0;
            } else if (s < 0.0) {
                y += 89.0;
            }
            f = 0.0;
        } else if (f > 0.0) {
            if (s > 0.0) {
                y -= st;
            }
            if (s < 0.0) {
                y += st;
            }
        } else {
            if (s > 0.0) {
                y += st;
            }
            if (s < 0.0) {
                y -= st;
            }
        }
        if (f < 0.0) {
            y -= 180.0;
        }
        y *= 0.01746532879769802;
        return (float) y;
    }
	
	public static float getDirection() {
        float yaw = mc.thePlayer.rotationYaw, forward = mc.thePlayer.moveForward, strafe = mc.thePlayer.moveStrafing;
        yaw += (forward < 0 ? 180 : 0);
        if (strafe < 0) {
            yaw += forward == 0 ? 90 : forward < 0 ? -45 : 45;
        }
        if (strafe > 0) {
            yaw -= forward == 0 ? 90 : forward < 0 ? -45 : 45;
        }
        return yaw * MathHelper.deg2Rad;
    }
	
	public static float getDir(float yaw) {
        EntityPlayerSP player = mc.thePlayer;
        float theyaw = yaw;
        if (player.movementInput.moveForward < 0) {
            theyaw += 180;
        }
        float forward = 1;
        if (player.movementInput.moveForward < 0) {
            forward = -0.5F;
        } else if (player.movementInput.moveForward > 0) {
            forward = 0.5F;
        }
        if (player.movementInput.moveStrafe > 0) {
            theyaw -= 90 * forward;
        }
        if (player.movementInput.moveStrafe < 0) {
            theyaw += 90 * forward;
        }
        theyaw *= 0.017453292F;//<- this little value can be found in jump() in entity living base
        return theyaw;
    }
	
}
