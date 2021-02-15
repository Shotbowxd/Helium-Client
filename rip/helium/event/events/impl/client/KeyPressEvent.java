package rip.helium.event.events.impl.client;

import rip.helium.event.events.Event;

public class KeyPressEvent implements Event {
	
	private final int keyCode;
	
	public KeyPressEvent(int keyCode)
	{
		this.keyCode = keyCode;
	}
	
	public int getKeyCode()
	{
		return keyCode;
	}
	
}
