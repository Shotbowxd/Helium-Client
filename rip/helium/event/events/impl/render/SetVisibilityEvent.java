package rip.helium.event.events.impl.render;

import rip.helium.event.events.Event;

public class SetVisibilityEvent implements Event {
	
	boolean visible;
	boolean shouldSet;
	
	public SetVisibilityEvent(boolean visible, boolean shouldSet) {
		this.visible = visible;
		this.shouldSet = shouldSet;
	}

	public boolean isVisible() {
		return visible;
	}

	public void setVisible(boolean visible) {
		this.visible = visible;
	}

	public boolean isShouldSet() {
		return shouldSet;
	}

	public void setShouldSet(boolean shouldSet) {
		this.shouldSet = shouldSet;
	}
	
}
