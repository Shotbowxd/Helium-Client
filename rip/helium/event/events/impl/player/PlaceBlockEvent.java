package rip.helium.event.events.impl.player;

import net.minecraft.block.Block;
import rip.helium.event.events.Event;

public class PlaceBlockEvent implements Event {
	
	Block block;
	
	public PlaceBlockEvent(Block block) {
		this.block = block;
	}

	public Block getBlock() {
		return block;
	}

	public void setBlock(Block block) {
		this.block = block;
	}
	
}
