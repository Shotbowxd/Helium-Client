package rip.helium.event.events.impl.player;

import net.minecraft.block.Block;
import rip.helium.event.events.Event;

public class BreakBlockEvent implements Event {
	
	Block block;
	
	public BreakBlockEvent(Block block) {
		this.block = block;
	}

	public Block getBlock() {
		return block;
	}

	public void setBlock(Block block) {
		this.block = block;
	}
	
}
