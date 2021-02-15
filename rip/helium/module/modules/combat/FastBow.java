package rip.helium.module.modules.combat;

import org.lwjgl.input.Mouse;

import net.minecraft.item.ItemBow;
import net.minecraft.network.play.client.C03PacketPlayer.C06PacketPlayerPosLook;
import net.minecraft.network.play.client.C07PacketPlayerDigging;
import net.minecraft.network.play.client.C07PacketPlayerDigging.Action;
import net.minecraft.network.play.client.C08PacketPlayerBlockPlacement;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import rip.helium.event.EventTarget;
import rip.helium.event.events.impl.player.UpdateEvent;
import rip.helium.module.Module;
import rip.helium.utils.render.ColorUtils;

public class FastBow extends Module {

	public FastBow(int bind, String name, String displayName, Category category) {
		super(bind, name, displayName, category);
		this.setColor(ColorUtils.generateColor());
	}
	
	@EventTarget
	public void onUpdatePre(UpdateEvent event) {
		//mc.timer.timerSpeed = 1.9f;
		if(mc.thePlayer.onGround && mc.thePlayer.getCurrentEquippedItem() != null && mc.thePlayer.getCurrentEquippedItem().getItem() instanceof ItemBow && Mouse.isButtonDown(1) && mc.currentScreen == null) {
			mc.getNetHandler().addToSendQueue(new C08PacketPlayerBlockPlacement(mc.thePlayer.inventory.getCurrentItem()));
			if(mc.thePlayer.ticksExisted %3 == 0) {
				for(int i = 0; i < 20; i++) {
					mc.getNetHandler().addToSendQueue(new C06PacketPlayerPosLook(mc.thePlayer.posX, mc.thePlayer.posY + 1.1E-9D, mc.thePlayer.posZ, mc.thePlayer.rotationYaw, mc.thePlayer.rotationPitch, true));
				}
			}
			mc.thePlayer.sendQueue.addToSendQueue(new C07PacketPlayerDigging(Action.RELEASE_USE_ITEM, BlockPos.ORIGIN, EnumFacing.DOWN));
			mc.playerController.onStoppedUsingItem(mc.thePlayer);
		}
	}
	
	@Override
	public void onDisable() {
		super.onDisable();
		//mc.timer.timerSpeed = 1.0f;
	}

}
