package rip.helium.event.events.impl.render;

import net.minecraft.client.gui.FontRenderer;
import rip.helium.event.events.Event;

public class Render2DEvent implements Event {
	
	private int width;
	private int height;
	private FontRenderer fontRenderer;
	
	public int getWidth() {
		return this.width;
	}
	
	public int getHeight() {
		return this.height;
	}
	
	public FontRenderer getFontRenderer() {
		return this.fontRenderer;
	}
	
	public Render2DEvent(int width, int height, FontRenderer fontRenderer) {
		this.width = width;
		this.height = height;
		this.fontRenderer = fontRenderer;
	}
	
}
