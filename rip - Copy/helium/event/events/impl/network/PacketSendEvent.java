package rip.helium.event.events.impl.network;

import net.minecraft.network.Packet;
import rip.helium.event.events.callables.EventCancellable;

public class PacketSendEvent extends EventCancellable {
	
	private Packet packet;
	
	public PacketSendEvent(Packet packet) {
	    this.packet = packet;
	}
	  
	public Packet getPacket() {
	    return this.packet;
	}
	  
	public void setPacket(Packet packet) {
	    this.packet = packet;
	}
	  
	
}
