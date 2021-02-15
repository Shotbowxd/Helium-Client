package rip.helium.module.modules.movement.speed;

import rip.helium.ClientSupport;
import rip.helium.event.events.impl.player.MoveEvent;
import rip.helium.utils.entity.PlayerUtils;

public class BhopMode implements ClientSupport {
	
	public void onMove(MoveEvent event) {
		if(mc.thePlayer.moveForward != 0.0f || mc.thePlayer.moveStrafing != 0.0f) {
			if(mc.thePlayer.onGround) {
				event.setY(mc.thePlayer.motionY = 0.4d);
			}
			PlayerUtils.setMoveSpeed(event, 0.65);
		}
	}
	
}
