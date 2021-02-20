package rip.helium.module.modules.combat;

import java.util.ArrayList;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import rip.helium.event.EventTarget;
import rip.helium.event.events.impl.player.UpdateEvent;
import rip.helium.module.Module;
import rip.helium.utils.render.ColorUtils;

public class AntiBot extends Module {

	public AntiBot(int bind, String name, String displayName, Category category) {
		super(bind, name, displayName, category);
		this.setColor(ColorUtils.generateColor());
	}
	
	public ArrayList<EntityPlayer> bots = new ArrayList<>();
	
	@EventTarget
	public void onUpdatePost(UpdateEvent event) {
		if(event.isPre()) {
			if (mc.getCurrentServerData() != null && mc.theWorld != null && (mc.getCurrentServerData().serverIP.contains("hypixel") || mc.getCurrentServerData().serverIP.contains("emeraldcraft"))) {
				if (mc.thePlayer.ticksExisted % 600 == 0) {
		            bots.clear();
		        }
			}
		} else {
			if (mc.getCurrentServerData() != null && mc.theWorld != null && (mc.getCurrentServerData().serverIP.contains("hypixel") || mc.getCurrentServerData().serverIP.contains("emeraldcraft"))) {
	            for (Entity entity : mc.theWorld.loadedEntityList) {
	                if (entity instanceof EntityPlayer) {
	                    if (entity != mc.thePlayer) {
	                        //EntityPlayer entity = (EntityPlayer) entities;
	                        if (entity != mc.thePlayer) {

	                            if (mc.thePlayer.getDistanceToEntity(entity) < 10) {
	                                if (!entity.getDisplayName().getFormattedText().startsWith("§") || entity.isInvisible() || entity.getDisplayName().getFormattedText().toLowerCase().contains("npc")) {
	                                    bots.add((EntityPlayer) entity);
	                                    //ClientBase.chat(entity.getName() + " has big gay");
	                                }
	                            }
	                        }
	                        if (bots.contains(entity) && !entity.isInvisible()) {
	                            bots.remove(entity);

	                        }
	                    } else {
	                        bots.remove(entity);
	                    }
	                }

	            }
	        } else if (mc.getCurrentServerData() != null && mc.getCurrentServerData().serverIP.contains("mineplex")) {
	            for (final EntityPlayer object2 : mc.theWorld.playerEntities) {
	                EntityPlayer entity = object2;
	                if (!(object2.getHealth() == Double.NaN)) {
	                    bots.add(object2);
	                }
	            }
	        }
		}
	}

}
