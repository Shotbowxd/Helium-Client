package rip.helium.event.events.impl.player;

import rip.helium.event.events.Event;

public class SwitchItemEvent implements Event {
	
	String item;
	boolean pre;
	
	public SwitchItemEvent(String item, boolean pre) {
		this.item = item;
		this.pre = pre;
	}

	public String getItem() {
		return item;
	}

	public void setItem(String item) {
		this.item = item;
	}

	public boolean isPre() {
		return pre;
	}

	public void setPre(boolean pre) {
		this.pre = pre;
	}
	
}
