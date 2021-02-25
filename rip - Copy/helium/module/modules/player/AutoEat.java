package rip.helium.module.modules.player;

import net.minecraft.item.ItemFood;
import net.minecraft.item.ItemStack;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.C09PacketHeldItemChange;
import rip.helium.event.EventTarget;
import rip.helium.event.events.impl.player.UpdateEvent;
import rip.helium.module.Module;
import rip.helium.utils.render.ColorUtils;

public class AutoEat extends Module {

	public AutoEat(int bind, String name, String displayName, Category category) {
		super(bind, name, displayName, category);
		this.setColor(ColorUtils.generateColor());
	}
	
	int packets = 100;
	int slotBefore;
	int bestSlot;
	public int eating;
	boolean wasUsing = false;
	
	@EventTarget
	public void onUpdate(UpdateEvent event) {
		this.wasUsing = mc.gameSettings.keyBindUseItem.pressed;
	    if (eating < 41) {
	    	eating++;
	    	if (eating <= 1) {
	    		mc.thePlayer.inventory.currentItem = this.bestSlot;
	    		mc.thePlayer.sendQueue.addToSendQueue((Packet)new C09PacketHeldItemChange(this.bestSlot));
	    	} 
	    	mc.gameSettings.keyBindUseItem.pressed = true;
	    	if (eating >= 38) {
	    		mc.gameSettings.keyBindUseItem.unpressKey();
	    		if (this.slotBefore != -1)
	    			mc.thePlayer.inventory.currentItem = this.slotBefore; 
	    		this.slotBefore = -1;
	    	} 
	    	return;
	    } 
	    float bestRestoration = 0.0F;
	    this.bestSlot = -1;
	    int PrevSlot = mc.thePlayer.inventory.currentItem;
	    for (int i = 0; i < 9; i++) {
	    	ItemStack item = mc.thePlayer.inventory.getStackInSlot(i);
	    	if (item != null) {
	    		float restoration = 0.0F;
	    		if (item.getItem() instanceof ItemFood && !(item.getItem() instanceof net.minecraft.item.ItemAppleGold))
	    			restoration = ((ItemFood)item.getItem()).getSaturationModifier(item); 
	    		if (restoration > bestRestoration) {
	    			bestRestoration = restoration;
	    			this.bestSlot = i;
	    		} 
	    	} 	
	    } 
	    if (this.bestSlot == -1)
	    	return; 
	    if (this.bestSlot == -1 || mc.thePlayer.getFoodStats().getFoodLevel() >= 19)
	    	return; 
	    this.slotBefore = mc.thePlayer.inventory.currentItem;
	    if (this.slotBefore == -1)
	    	return; 
	    mc.thePlayer.inventory.currentItem = PrevSlot;
	    mc.thePlayer.stopUsingItem();
	    mc.thePlayer.inventory.currentItem = PrevSlot;
	    eating = 0;
	}
}
