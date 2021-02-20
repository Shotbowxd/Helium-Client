package rip.helium.module.modules.movement;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import net.minecraft.network.play.client.C03PacketPlayer;
import rip.helium.event.EventTarget;
import rip.helium.event.events.impl.player.BlockStepEvent;
import rip.helium.module.Module;
import rip.helium.setting.Setting;
import rip.helium.utils.client.Timer;
import rip.helium.utils.render.ColorUtils;

public class Step extends Module {
	
	private Setting mode;
	
	private Timer timer;
	private Timer lastStep;
	private boolean resetTimer;
	
	public Step(int bind, String name, Category category) {
		super(bind, name, category);
		this.setColor(ColorUtils.generateColor());
		
		ArrayList<String> modes = new ArrayList<String>();
		modes.add("Vanilla");
		modes.add("NCP");
		
		this.mode = new Setting("Mode", this, "Vanilla", modes);
		
		mc.hackedClient.getSettingManager().addSetting(this.mode);
		
		this.timer = new Timer();
		this.lastStep = new Timer();
	}
	
	@EventTarget
	public void onStep(BlockStepEvent event) {
		switch(this.mode.getValString()) {
		case "Vanilla":
			event.setStepHeight(2.5f);
			break;
		case "NCP":
			if (resetTimer) {
	            resetTimer = !resetTimer;
	            mc.timer.timerSpeed = 1;
	        }
	        if (!mc.thePlayer.isInWater() && mc.getCurrentServerData() != null)
	            if (event.isPre()) {
	                if (mc.thePlayer.isCollidedVertically && !mc.gameSettings.keyBindJump.isKeyDown() && timer.hasPassed(300)) {
	                	event.setStepHeight(2.5F);
	                    //mc.timer.timerSpeed = 0.37F;
	                    //mc.timer.timerSpeed = 1f;
	                    //sigmaSkiddedStepYesIkItsSkiddedNowStfu(2.5f);
	                }

	                if ((mc.hackedClient.getModuleManager().getModule("Flight").getState()))
	                    event.setStepHeight(0F);

	            } else {

	                double rheight = mc.thePlayer.getEntityBoundingBox().minY - mc.thePlayer.posY;
	                boolean canStep = rheight >= 0.625;
	                if (canStep) {
	                    lastStep.updateLastTime();
	                    timer.updateLastTime();
	                }
	                if (canStep) {
	                	mc.timer.timerSpeed = 0.37F - (rheight >= 1 ? Math.abs(1 - (float) rheight) * (0.37F * 0.55f) : 0);
	                    if (mc.timer.timerSpeed <= 0.05f) {
	                    	mc.timer.timerSpeed = 0.05f;
	                    }
	                    resetTimer = true;
	                    step(rheight);
	                }
	        }
	        break;
		}
	}
	
	private void step(double height) {
        if (mc.hackedClient.getModuleManager().getModule("Flight").getState() || mc.hackedClient.getModuleManager().getModule("Speed").getState() || mc.hackedClient.getModuleManager().getModule("LongJump").getState())
            height = 0;
        List<Double> offset = Arrays.asList(0.42, 0.333, 0.248, 0.083, -0.078);
        double posX = mc.thePlayer.posX;
        double posZ = mc.thePlayer.posZ;
        double y = mc.thePlayer.posY;
        if (height < 1.1) {
            double first = 0.42;
            double second = 0.75;
            if (height != 1) {
                first *= height;
                second *= height;
                if (first > 0.425) {
                    first = 0.425;
                }
                if (second > 0.78) {
                    second = 0.78;
                }
                if (second < 0.49) {
                    second = 0.49;
                }
            }
            if (first == 0.42)
                first = 0.41999998688698;
            mc.thePlayer.sendQueue.addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(posX, y + first, posZ, false));
            if (y + second < y + height)
                mc.thePlayer.sendQueue.addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(posX, y + second, posZ, false));
            return;
        } else if (height < 1.6) {
            for (int i = 0; i < offset.size(); i++) {
                double off = offset.get(i);
                y += off;
                mc.thePlayer.sendQueue.addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(posX, y, posZ, false));
            }
        } else if (height < 2.1) {
            double[] heights = {0.425, 0.821, 0.699, 0.599, 1.022, 1.372, 1.652, 1.869};
            for (double off : heights) {
                mc.thePlayer.sendQueue.addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(posX, y + off, posZ, false));
            }
        } else {
            double[] heights = {0.425, 0.821, 0.699, 0.599, 1.022, 1.372, 1.652, 1.869, 2.019, 1.907};
            for (double off : heights) {
                mc.thePlayer.sendQueue.addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(posX, y + off, posZ, false));
            }
        }
    }

}
