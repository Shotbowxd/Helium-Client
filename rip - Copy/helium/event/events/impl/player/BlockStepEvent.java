package rip.helium.event.events.impl.player;

import rip.helium.event.events.callables.EventCancellable;

public class BlockStepEvent extends EventCancellable {
	
	private double stepHeight;
    private final boolean pre;

    public BlockStepEvent(double stepHeight) {
        this.stepHeight = stepHeight;
        pre = true;
    }

    public BlockStepEvent() {
    	pre = false;
    }
    
    public double getStepHeight() {
    	return this.stepHeight;
    }
    
    public void setStepHeight(double stepHeight) {
    	this.stepHeight = stepHeight;
    }
    
    public boolean isPre() {
    	return this.pre;
    }
	
}
