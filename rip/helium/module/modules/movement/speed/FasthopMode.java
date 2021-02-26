package rip.helium.module.modules.movement.speed;

import rip.helium.ClientSupport;
import rip.helium.event.events.impl.player.MoveEvent;
import rip.helium.module.modules.movement.TargetStrafe;
import rip.helium.utils.entity.PlayerUtils;

public class FasthopMode implements ClientSupport {
	
	public void onMove(MoveEvent event) {
		if(mc.thePlayer.isMoving()) {
			if (!TargetStrafe.doStrafeAtSpeed(event, 1)) {
				if (mc.thePlayer.onGround) {
					event.setY(mc.thePlayer.motionY = 0.4d);
				}
				PlayerUtils.setMoveSpeed(event, 1);
			}
		}
	}
}
