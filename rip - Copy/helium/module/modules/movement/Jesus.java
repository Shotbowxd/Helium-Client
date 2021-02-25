package rip.helium.module.modules.movement;

import java.util.ArrayList;
import java.util.Arrays;

import net.minecraft.block.BlockLiquid;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.BlockPos;
import rip.helium.event.EventTarget;
import rip.helium.event.events.impl.player.BoundingBoxEvent;
import rip.helium.event.events.impl.player.MoveEvent;
import rip.helium.event.events.impl.player.UpdateEvent;
import rip.helium.module.Module;
import rip.helium.setting.Setting;
import rip.helium.utils.client.Timer;
import rip.helium.utils.entity.PlayerUtils;
import rip.helium.utils.render.ColorUtils;

public class Jesus extends Module {

	private Setting mode;
	private int stage;
	private Timer timer;
	
	public Jesus(int bind, String name, Category category) {
		super(bind, name, category);
		this.setColor(ColorUtils.generateColor());
		
		ArrayList<String> modes = new ArrayList<String>();
		modes.add("Solid");
		modes.add("NCP");
		modes.add("Mineplex");
		
		this.mode = new Setting("Mode", this, "Solid", modes);
		
		mc.hackedClient.getSettingManager().addSetting(this.mode);
		
		this.timer = new Timer();
	}
	
	@EventTarget
	public void onUpdatePre(UpdateEvent event) {
		this.setSuffix(this.mode.getValString());
		if(mode.getValString().equalsIgnoreCase("Solid")) {
			if (event.isPre()) {
				if (shouldJesus() && (mc).thePlayer.isInsideOfMaterial(Material.air) && !mc.thePlayer.isSneaking())
					(mc).thePlayer.motionY = 0.085D; 
			} 
		}
	}
	
	@EventTarget
	public void onBB(BoundingBoxEvent event) {
		if(mode.getValString().equalsIgnoreCase("Solid")) {
			if (!shouldJesus() && event.getBlock() instanceof BlockLiquid && mc.theWorld.getBlockState(event.getBlockPos()).getBlock() instanceof BlockLiquid && ((Integer)mc.theWorld.getBlockState(event.getBlockPos()).getValue((IProperty)BlockLiquid.LEVEL)).intValue() == 0 && shouldSetBoundingBox() && (event.getBlockPos().getY() + 1) <= (mc).thePlayer.boundingBox.minY)
			      event.setBoundingBox(new AxisAlignedBB(event.getBlockPos().getX(), event.getBlockPos().getY(), event.getBlockPos().getZ(), (event.getBlockPos().getX() + 1), (event.getBlockPos().getY() + 1), (event.getBlockPos().getZ() + 1))); 
		}
	}
	
	@EventTarget
	public void onMove(MoveEvent event) {
        if (mode.getValString().equalsIgnoreCase("NCP")) {
        	if (mc.thePlayer.isInWater() && !mc.thePlayer.isSneaking() && shouldJesus()) {
                mc.thePlayer.motionY = 0.12;
            }
            if (mc.thePlayer.onGround && !mc.thePlayer.isInWater() && shouldJesus()) {
                stage = 1;
                timer.updateLastTime();
            }
            if (stage > 0 && !timer.hasPassed(2500)) {

                if ((mc.thePlayer.isCollidedVertically && mc.thePlayer.onGround || mc.thePlayer.isSneaking())) {
                    stage = -1;
                }
                mc.thePlayer.motionX *= 0;
                mc.thePlayer.motionZ *= 0;
                if (!mc.thePlayer.isInWater()) {
                	/* MichaelMaymays: this used to be setMotion, which was just setMoveSpeed,
                	 *  and then there was also Aris speed code in the same class,
                	 *   which was the exact same thing.
                	 *    who the hell skidded this? :thonkpad: */
                    PlayerUtils.setMoveSpeed(event, 1.1 + mc.thePlayer.getSpeedEffect() * 0.05);
                }
                double motionY = getMotionY(stage);
                if (motionY != -999) {
                    mc.thePlayer.motionY = motionY + 0.1;

                }

                stage += 1;
            }
        } else if (mode.getValString().equalsIgnoreCase("Mineplex")) {
            final BlockPos blockPos = mc.thePlayer.getPosition().down();
            if (mc.thePlayer.isInWater() && !mc.thePlayer.isSneaking() && shouldJesus()) {
                mc.thePlayer.onGround = true;
                mc.thePlayer.jump();
            }
        }
	}
	
	private boolean shouldSetBoundingBox() {
		if (!mc.thePlayer.isSneaking() && mc.thePlayer.fallDistance < 4.0F)
		      return true; 
		    return false;
	}
	
    private boolean shouldJesus() {
        double x = mc.thePlayer.posX;
        double y = mc.thePlayer.posY;
        double z = mc.thePlayer.posZ;
        ArrayList<BlockPos> pos = new ArrayList<>(Arrays.asList(new BlockPos(x + 0.3, y, z + 0.3),
                new BlockPos(x - 0.3, y, z + 0.3), new BlockPos(x + 0.3, y, z - 0.3), new BlockPos(x - 0.3, y, z - 0.3)));
        for (BlockPos poss : pos) {
            if (!(mc.theWorld.getBlockState(poss).getBlock() instanceof BlockLiquid))
                continue;
            if (mc.theWorld.getBlockState(poss).getProperties().get(BlockLiquid.LEVEL) instanceof Integer) {
                if ((int) mc.theWorld.getBlockState(poss).getProperties().get(BlockLiquid.LEVEL) <= 4) {
                    return true;
                }
            }
        }


        return false;
    }

    private double getMotionY(int stage) {
        //modified from longjump
        stage--;
        double[] motion = new double[]{0.500, 0.484, 0.468, 0.436, 0.404, 0.372, 0.340, 0.308, 0.276, 0.244, 0.212, 0.180, 0.166, 0.166, 0.156, 0.123, 0.135, 0.111, 0.086, 0.098, 0.073, 0.048, 0.06, 0.036, 0.0106, 0.015, 0.004, 0.004, 0.004, 0.004, -0.013, -0.045, -0.077, -0.109};
        if (stage < motion.length && stage >= 0)
            return motion[stage];
        else
            return -999;
    }
	
}
