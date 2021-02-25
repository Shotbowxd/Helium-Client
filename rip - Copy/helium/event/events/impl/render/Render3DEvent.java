package rip.helium.event.events.impl.render;

import rip.helium.event.events.Event;

public class Render3DEvent implements Event {
	
	float partialTicks;
	
	public Render3DEvent(float partialTicks) {
		this.partialTicks = partialTicks;
	}
	
	public float getPartialTicks() {
		return partialTicks;
	}
	
}
