package rip.helium.module.modules.movement.speed;

import net.minecraft.network.play.client.C03PacketPlayer;
import net.minecraft.network.play.server.S08PacketPlayerPosLook;
import rip.helium.ClientSupport;
import rip.helium.event.events.impl.network.PacketReceiveEvent;
import rip.helium.event.events.impl.network.PacketSendEvent;

public class ViperMode implements ClientSupport {
	
	public void onPacketReceive(PacketReceiveEvent event) {
		if (event.getPacket() instanceof S08PacketPlayerPosLook) {
            S08PacketPlayerPosLook playerPosLook = (S08PacketPlayerPosLook) event.getPacket();

            playerPosLook.y += 1.0E-4F;
        }
	}
	
	public void onPacketSend(PacketSendEvent event) {
		if (event.getPacket() instanceof C03PacketPlayer) {
            C03PacketPlayer packetPlayer = (C03PacketPlayer) event.getPacket();
            if (mc.thePlayer.ticksExisted < 50) {

                mc.thePlayer.sendQueue.addToSendQueueNoEvent(new C03PacketPlayer.C04PacketPlayerPosition());
            } else {

                packetPlayer.y += 0.42F;
            }
        }
	}
	
}
