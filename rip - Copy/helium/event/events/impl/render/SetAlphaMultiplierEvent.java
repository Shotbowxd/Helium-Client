package rip.helium.event.events.impl.render;

import rip.helium.event.events.Event;

public class SetAlphaMultiplierEvent implements Event {
	
	int multiplier;
	
	public SetAlphaMultiplierEvent(int multiplier) {
		this.multiplier = multiplier;
	}

	public int getMultiplier() {
		return multiplier;
	}

	public void setMultiplier(int multiplier) {
		this.multiplier = multiplier;
	}
	
}
