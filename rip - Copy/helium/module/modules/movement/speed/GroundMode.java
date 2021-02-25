package rip.helium.module.modules.movement.speed;

import rip.helium.ClientSupport;
import rip.helium.event.events.impl.player.MoveEvent;
import rip.helium.utils.entity.PlayerUtils;

public class GroundMode implements ClientSupport {
	
	public void onMove(MoveEvent event) {
		if(mc.thePlayer.moveForward != 0.0f || mc.thePlayer.moveStrafing != 0.0f) {
			PlayerUtils.setMoveSpeed(event, 1);
		}
	}
	
}
