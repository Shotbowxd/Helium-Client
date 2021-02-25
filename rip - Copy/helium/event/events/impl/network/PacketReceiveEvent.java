package rip.helium.event.events.impl.network;

import net.minecraft.network.Packet;
import rip.helium.event.events.callables.EventCancellable;

public class PacketReceiveEvent extends EventCancellable {
	
	private Packet packet;
	
	public PacketReceiveEvent(Packet packet) {
	    this.packet = packet;
	}
	  
	public Packet getPacket() {
	    return this.packet;
	}
	  
	public void setPacket(Packet packet) {
	    this.packet = packet;
	}
	  
	
}
