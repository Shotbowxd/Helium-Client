package rip.helium.module.modules.movement;

import net.minecraft.network.play.client.C07PacketPlayerDigging;
import net.minecraft.network.play.client.C07PacketPlayerDigging.Action;
import net.minecraft.network.play.client.C08PacketPlayerBlockPlacement;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import rip.helium.event.EventTarget;
import rip.helium.event.events.impl.player.ItemSlowEvent;
import rip.helium.event.events.impl.player.UpdateEvent;
import rip.helium.module.Module;
import rip.helium.setting.Setting;
import rip.helium.utils.render.ColorUtils;

public class NoSlowdown extends Module {

	private Setting packet;
	
	public NoSlowdown(int bind, String name, String displayName, Category category) {
		super(bind, name, displayName, category);
		this.setColor(ColorUtils.generateColor());
		this.setState(true);
		
		this.packet = new Setting("Packet", this, false);
		
		mc.hackedClient.getSettingManager().addSetting(this.packet);
	}
	
	@EventTarget
	public void onItemSlow(ItemSlowEvent event) {
		event.setCancelled(true);
	}
	
	@EventTarget
	public void onUpdatePre(UpdateEvent event) {
		if(event.isPre()) {
			if(mc.thePlayer.isBlocking() && (mc.thePlayer.moveForward != 0.0f || mc.thePlayer.moveStrafing != 0.0f) && this.packet.getValBoolean()) {
				mc.getNetHandler().addToSendQueue(new C07PacketPlayerDigging(Action.RELEASE_USE_ITEM, BlockPos.ORIGIN, EnumFacing.DOWN));
			}
		} else {
			if(mc.thePlayer.isBlocking() && (mc.thePlayer.moveForward != 0.0f || mc.thePlayer.moveStrafing != 0.0f) && this.packet.getValBoolean()) {
				mc.getNetHandler().addToSendQueue(new C08PacketPlayerBlockPlacement(mc.thePlayer.inventory.getCurrentItem()));
			}
		}
	}

}
