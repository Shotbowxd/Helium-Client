package rip.helium.module.modules.movement.speed;

import rip.helium.ClientSupport;
import rip.helium.event.events.impl.player.MoveEvent;
import rip.helium.module.modules.movement.TargetStrafe;
import rip.helium.utils.entity.PlayerUtils;

public class BhopMode implements ClientSupport {
	
	public void onMove(MoveEvent event) {
		if(mc.thePlayer.isMoving()) {
			if (!TargetStrafe.doStrafeAtSpeed(event, 0.65)) {
				if (mc.thePlayer.onGround) {
					event.setY(mc.thePlayer.motionY = 0.4d);
				}
				PlayerUtils.setMoveSpeed(event, 0.65);
			}
		}
	}
	
}
