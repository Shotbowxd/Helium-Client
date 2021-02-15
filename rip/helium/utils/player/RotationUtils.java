package rip.helium.utils.player;

import net.minecraft.util.MathHelper;
import rip.helium.ClientSupport;

public class RotationUtils implements ClientSupport {
	
	public static float[] getRotationFromPosition(double x, double y, double z) {
		double xDiff = x - mc.thePlayer.posX;
	    double zDiff = z - mc.thePlayer.posZ;
	    double yDiff = y - mc.thePlayer.posY - 0.6D;
	    double dist = MathHelper.sqrt_double(xDiff * xDiff + zDiff * zDiff);
	    float yaw = (float)(Math.atan2(zDiff, xDiff) * 180.0D / Math.PI) - 90.0F;
	    float pitch = (float)-(Math.atan2(yDiff, dist) * 180.0D / Math.PI);
	    return new float[] { yaw, pitch };
	}
	
}
