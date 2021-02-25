package rip.helium.module.modules.combat;

import net.minecraft.client.gui.inventory.GuiChest;
import net.minecraft.item.ItemPotion;
import net.minecraft.item.ItemStack;
import net.minecraft.network.play.client.C03PacketPlayer;
import net.minecraft.network.play.client.C08PacketPlayerBlockPlacement;
import net.minecraft.network.play.client.C09PacketHeldItemChange;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.BlockPos;
import rip.helium.event.EventTarget;
import rip.helium.event.events.impl.player.UpdateEvent;
import rip.helium.module.Module;
import rip.helium.utils.client.Timer;
import rip.helium.utils.misc.MathUtils;
import rip.helium.utils.render.ColorUtils;

public class AutoPot extends Module {

	private Timer timer;
	
	public AutoPot(int bind, String name, String displayName, Category category) {
		super(bind, name, displayName, category);
		this.setColor(ColorUtils.generateColor());
		
		this.timer = new Timer();
	}
	
	@EventTarget
	public void onUpdatePre(UpdateEvent event) {
		if (updateCounter() == 0 || !(mc.thePlayer != null) || !(mc.theWorld != null)) {
            return;
        }
        if (mc.thePlayer != null) {
            if (mc.thePlayer.getHealth() <= 12 && timer.hasPassed(MathUtils.getRandomInRange(150, 300))) {
                if (doesHotbarHavePots()) {
                    float pitch = event.getPitch();
                    event.setPitch(88);
                    for (int index = 36; index < 45; index++) {
                        final ItemStack stack = mc.thePlayer.inventoryContainer.getSlot(index).getStack();
                        if (stack == null) {
                            continue;
                        }

                        if (isStackSplashHealthPot(stack)) {
                            final int oldslot = mc.thePlayer.inventory.currentItem;
                            mc.getNetHandler().addToSendQueue(new C09PacketHeldItemChange(index - 36));
                            mc.thePlayer.sendQueue.addToSendQueue(new C03PacketPlayer.C06PacketPlayerPosLook(mc.thePlayer.posX, mc.thePlayer.posY, mc.thePlayer.posZ, event.getYaw(), 87, mc.thePlayer.onGround));
                            mc.getNetHandler().addToSendQueue(
                                    new C08PacketPlayerBlockPlacement(new BlockPos(-1, -1, -1), 255, stack, 0, 0, 0));
                            mc.getNetHandler().addToSendQueue(new C09PacketHeldItemChange(oldslot));
                            mc.thePlayer.sendQueue.addToSendQueue(new C03PacketPlayer.C06PacketPlayerPosLook(mc.thePlayer.posX, mc.thePlayer.posY, mc.thePlayer.posZ, event.getYaw(), event.getPitch(), mc.thePlayer.onGround));
                            break;
                        }
                    }
                    timer.updateLastTime();
                    event.setPitch(90);
                } else {
                    getPotsFromInventory();
                }
            }
        }
	}
	
	private int updateCounter() {
        int counter = 0;
        for (int index = 9; index < 45; index++) {
            final ItemStack stack = mc.thePlayer.inventoryContainer.getSlot(index).getStack();
            if (stack == null) {
                continue;
            }
            if (isStackSplashHealthPot(stack)) {
                counter += stack.stackSize;
            }
        }
        return counter;
    }

    private boolean doesHotbarHavePots() {
        for (int index = 36; index < 45; index++) {
            final ItemStack stack = mc.thePlayer.inventoryContainer.getSlot(index).getStack();
            if (stack == null) {
                continue;
            }
            if (isStackSplashHealthPot(stack)) {
                return true;
            }
        }
        return false;
    }

    private void getPotsFromInventory() {
        if (mc.currentScreen instanceof GuiChest) {
            return;
        }
        for (int index = 9; index < 36; index++) {
            final ItemStack stack = mc.thePlayer.inventoryContainer.getSlot(index).getStack();
            if (stack == null) {
                continue;
            }
            if (isStackSplashHealthPot(stack)) {
                mc.playerController.windowClick(0, index, 0, 1, mc.thePlayer);
                break;
            }
        }
    }

    private boolean isStackSplashHealthPot(ItemStack stack) {
        if (stack == null) {
            return false;
        }
        if (stack.getItem() instanceof ItemPotion) {
            final ItemPotion potion = (ItemPotion) stack.getItem();
            if (ItemPotion.isSplash(stack.getItemDamage())) {
                for (final Object o : potion.getEffects(stack)) {
                    final PotionEffect effect = (PotionEffect) o;
                    if (effect.getPotionID() == Potion.heal.id) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

}
