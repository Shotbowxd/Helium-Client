package rip.helium.module.modules.misc;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.network.play.client.C01PacketChatMessage;
import net.minecraft.network.play.client.C02PacketUseEntity;
import net.minecraft.network.play.client.C02PacketUseEntity.Action;
import rip.helium.event.EventTarget;
import rip.helium.event.events.impl.network.PacketSendEvent;
import rip.helium.event.events.impl.player.BreakBlockEvent;
import rip.helium.event.events.impl.player.PlaceBlockEvent;
import rip.helium.event.events.impl.player.SwitchItemEvent;
import rip.helium.module.Module;
import rip.helium.utils.render.ColorUtils;

public class Announcer extends Module {

	public Announcer(int bind, String name, Category category) {
		super(bind, name, category);
		this.setColor(ColorUtils.generateColor());
	}
	
	@EventTarget
	public void onPlaceBlock(PlaceBlockEvent event) {
		mc.getNetHandler().addToSendQueue(new C01PacketChatMessage("I just placed a " + event.getBlock().getLocalizedName() + ". #Helium"));
	}
	
	@EventTarget
	public void onBreakBlock(BreakBlockEvent event) {
		mc.getNetHandler().addToSendQueue(new C01PacketChatMessage("I just mined a " + event.getBlock().getLocalizedName() + ". #Helium"));
	}
	
	@EventTarget
	public void onSwitchItem(SwitchItemEvent event) {
		String[] names = {"Nothing", "Nothing"};
		if(event.isPre()) {
			names[0] = event.getItem();
		} else {
			names[1] = event.getItem();
			mc.getNetHandler().addToSendQueue(new C01PacketChatMessage("I just switched the item in my hand to " + (names[1].equalsIgnoreCase("Nothing") ? "" : "a ") + names[1] + ". #Helium"));
		}
	}
	
	@EventTarget
	public void onPacketSend(PacketSendEvent event) {
		if(event.getPacket() instanceof C02PacketUseEntity) {
			C02PacketUseEntity packet = (C02PacketUseEntity) event.getPacket();
				if(packet.getAction() == Action.ATTACK) {
					if(packet.getEntityFromWorld(mc.theWorld) instanceof EntityLivingBase){ 
						EntityLivingBase e = (EntityLivingBase) packet.getEntityFromWorld(mc.theWorld);{
						if(packet.getEntityFromWorld(mc.theWorld) instanceof EntityPlayer) {
							mc.getNetHandler().addToSendQueue(new C01PacketChatMessage("I just attacked a player named " + packet.getEntityFromWorld(mc.theWorld).getName() + ". #Helium"));
						} else {
							String name = e.getDisplayName().getUnformattedText();
							mc.getNetHandler().addToSendQueue(new C01PacketChatMessage("I just attacked a " + name + ". #Helium"));
						}
					}
				}
			}
		}
	}
}
