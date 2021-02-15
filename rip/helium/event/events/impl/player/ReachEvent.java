package rip.helium.event.events.impl.player;

import rip.helium.event.events.Event;

public class ReachEvent implements Event {
	
	private float reach;
	
	public ReachEvent(float reach) {
		this.reach = reach;
	}

	public float getReach() {
		return reach;
	}

	public void setReach(float reach) {
		this.reach = reach;
	}
	
}
