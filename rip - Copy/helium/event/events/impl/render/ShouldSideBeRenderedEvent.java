package rip.helium.event.events.impl.render;

import net.minecraft.block.Block;
import rip.helium.event.events.Event;

public class ShouldSideBeRenderedEvent implements Event {
	
	boolean rendered;
	Block block;
	
	public ShouldSideBeRenderedEvent(boolean rendered, Block block) {
		this.rendered = rendered;
		this.block = block;
	}

	public Block getBlock() {
		return block;
	}

	public void setBlock(Block block) {
		this.block = block;
	}

	public boolean isRendered() {
		return rendered;
	}

	public void setRendered(boolean rendered) {
		this.rendered = rendered;
	}
	
}
