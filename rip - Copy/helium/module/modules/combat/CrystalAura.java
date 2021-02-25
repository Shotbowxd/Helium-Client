package rip.helium.module.modules.combat;

import java.util.ArrayList;
import java.util.Comparator;

import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityEnderCrystal;
import net.minecraft.network.play.client.C02PacketUseEntity;
import net.minecraft.network.play.client.C02PacketUseEntity.Action;
import rip.helium.event.EventTarget;
import rip.helium.event.events.impl.player.UpdateEvent;
import rip.helium.module.Module;
import rip.helium.utils.entity.UPlayer;
import rip.helium.utils.render.ColorUtils;

public class CrystalAura extends Module {

	public CrystalAura(int bind, String name, String displayName, Category category) {
		super(bind, name, displayName, category);
		this.setColor(ColorUtils.generateColor());
	}
	
	@EventTarget
	public void onUpdate(UpdateEvent event) {
		for(Entity e: mc.theWorld.loadedEntityList) {
			if(e instanceof EntityEnderCrystal) {
				EntityEnderCrystal ec = (EntityEnderCrystal) e;
				ArrayList<EntityEnderCrystal> list = new ArrayList<EntityEnderCrystal>();
				if(mc.thePlayer.getDistanceToEntity(e) <= 5) {
					list.add(ec);
					list.sort(Comparator.comparingDouble(UPlayer::getDistanceToEntity));
					float[] rotations = (((KillAura)mc.hackedClient.getModuleManager().getModule("KillAura")).getRotations(e, event));
					event.setYaw(rotations[0]);
					event.setPitch(rotations[1]);
					mc.getNetHandler().addToSendQueue(new C02PacketUseEntity(ec, Action.ATTACK));
					mc.thePlayer.swingItem();
				} else {
					if(list.contains(ec)) {
						list.remove(ec);
					}
				}
			}
		}
	}

}
