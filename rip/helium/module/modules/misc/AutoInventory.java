package rip.helium.module.modules.misc;

import java.util.ArrayList;

import org.lwjgl.input.Keyboard;

import net.minecraft.client.gui.inventory.GuiContainerCreative;
import net.minecraft.client.gui.inventory.GuiInventory;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.item.Item;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemAxe;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemBow;
import net.minecraft.item.ItemFishingRod;
import net.minecraft.item.ItemFood;
import net.minecraft.item.ItemPickaxe;
import net.minecraft.item.ItemPotion;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemSword;
import net.minecraft.item.ItemTool;
import net.minecraft.network.play.client.C0BPacketEntityAction;
import net.minecraft.network.play.client.C0DPacketCloseWindow;
import rip.helium.event.EventTarget;
import rip.helium.event.events.impl.network.PacketSendEvent;
import rip.helium.event.events.impl.player.UpdateEvent;
import rip.helium.module.Module;
import rip.helium.setting.Setting;
import rip.helium.utils.client.Action;
import rip.helium.utils.client.Timer;
import rip.helium.utils.player.ContainerUtils;
import rip.helium.utils.player.ItemUtils;
import rip.helium.utils.render.ColorUtils;

public class AutoInventory extends Module {

	private Setting spoof, onlyInvProperty, clean, equipArmor, swordSlot, dropDelay, equipDelay, autoSwordSlot;
	
	private final ArrayList<Action> clickQueue;
	public boolean cleaning;
	public boolean equipping;
	public boolean swappingSword;
	
	public Timer dropTimer;
	private Timer equipTimer;
	private boolean guiOpenedByMod;
	private boolean openedinv = false;
	
	public AutoInventory(int bind, String name, String displayName, Category category) {
		super(bind, name, displayName, category);
		this.setColor(ColorUtils.generateColor());
		
		this.spoof = new Setting("SpoofOpen", this, true);
		this.onlyInvProperty = new Setting("OnlyInInventory", this, !this.spoof.getValBoolean());
		this.clean = new Setting("Clean", this, false);
		this.equipArmor = new Setting("EquipArmor", this, false);
		this.swordSlot = new Setting("SwordSlot", this, false);
		this.dropDelay = new Setting("DropDelay", this, 150, 0, 350, false);
		this.equipDelay = new Setting("EquipDelay", this, 150, 0, 350, false);
		this.swordSlot = new Setting("SwordSlot", this, 1, 1, 9, true);
		
		mc.hackedClient.getSettingManager().addSetting(this.spoof);
		mc.hackedClient.getSettingManager().addSetting(this.onlyInvProperty);
		mc.hackedClient.getSettingManager().addSetting(this.clean);
		mc.hackedClient.getSettingManager().addSetting(this.equipArmor);
		mc.hackedClient.getSettingManager().addSetting(this.swordSlot);
		mc.hackedClient.getSettingManager().addSetting(this.dropDelay);
		mc.hackedClient.getSettingManager().addSetting(this.equipDelay);
		mc.hackedClient.getSettingManager().addSetting(this.swordSlot);
		
		clickQueue = new ArrayList<>();
		
		dropTimer = new Timer();
		equipTimer = new Timer();
	}
	
	@Override
	public void onEnable() {
		super.onEnable();
		cleaning = false;
        equipping = false;
        guiOpenedByMod = false;
        clickQueue.clear();
	}
	
	@EventTarget
	public void onPacketSend(PacketSendEvent event) {
		if (event.getPacket() instanceof C0DPacketCloseWindow) {
            openedinv = false;
        }
        if (event.getPacket() instanceof C0BPacketEntityAction) {
            C0BPacketEntityAction packet = (C0BPacketEntityAction) event.getPacket();
            if (packet.getAction() == C0BPacketEntityAction.Action.OPEN_INVENTORY) {
                openedinv = true;
            }
        }
	}
	
	@EventTarget
	public void onUpdate(UpdateEvent event) {
		if(!mc.thePlayer.isUsingItem()) {
			if (onlyInvProperty.getValBoolean()
                    && !(mc.currentScreen instanceof GuiInventory)
                    && !(mc.currentScreen instanceof GuiContainerCreative))
                return;

            if (!clickQueue.isEmpty()) {
                clickQueue.get(0).execute();
                clickQueue.remove(clickQueue.get(0));
            } else {
                if (!switchSwordSlot()) {
                    swappingSword = false;
                    if (!equipArmor()) {
                        equipping = false;
                        if (!clean()) {
                            cleaning = false;
                        }
                    }
                }
            }

            if (guiOpenedByMod && !cleaning && !equipping) {
                mc.displayGuiScreen(null);
                guiOpenedByMod = false;
                for (KeyBinding keyBinding : new KeyBinding[]{
                        mc.gameSettings.keyBindForward, mc.gameSettings.keyBindBack,
                        mc.gameSettings.keyBindLeft, mc.gameSettings.keyBindRight,
                        mc.gameSettings.keyBindJump, mc.gameSettings.keyBindSprint}) {
                    KeyBinding.setKeyBindState(keyBinding.getKeyCode(), Keyboard.isKeyDown(keyBinding.getKeyCode()));
                }
            }
		}
	}
	
	private boolean switchSwordSlot() {
        if (swordSlot.getValBoolean()) {
            for (int i = 9; i < 45; i++) {
                if (i == 35 + autoSwordSlot.getValDouble())
                    continue;

                if (!mc.thePlayer.inventoryContainer.getSlot(i).getHasStack() || !(mc.thePlayer.inventoryContainer.getSlot(i).getStack().getItem() instanceof ItemSword))
                    continue;

                ItemStack stackInSlot = mc.thePlayer.inventoryContainer.getSlot(i).getStack();

                if (!mc.thePlayer.inventoryContainer.getSlot(35 + (int)autoSwordSlot.getValDouble()).getHasStack()) {
                    int finalI1 = i;
                    if (!openedinv) {
                        mc.thePlayer.sendQueue.addToSendQueueNoEvent(new C0BPacketEntityAction(mc.thePlayer, C0BPacketEntityAction.Action.OPEN_INVENTORY));
                    }

                    clickQueue.add(() -> mc.playerController.windowClick(mc.thePlayer.inventoryContainer.windowId, finalI1, 0, 0, mc.thePlayer));
                    clickQueue.add(() -> mc.playerController.windowClick(mc.thePlayer.inventoryContainer.windowId, 35 + (int)autoSwordSlot.getValDouble(), 0, 0, mc.thePlayer));

                    if (openedinv) {
                        mc.thePlayer.sendQueue.addToSendQueueNoEvent(new C0DPacketCloseWindow(0));
                    }
                    return true;
                } else {
                    ItemStack stackInWantedSlot = mc.thePlayer.inventoryContainer.getSlot(35 + (int)autoSwordSlot.getValDouble()).getStack();
                    if (ItemUtils.compareDamage(stackInSlot, stackInWantedSlot) == stackInSlot) {
                        int finalI = i;
                        mc.thePlayer.sendQueue.addToSendQueueNoEvent(new C0BPacketEntityAction(mc.thePlayer, C0BPacketEntityAction.Action.OPEN_INVENTORY));

                        clickQueue.add(() -> mc.playerController.windowClick(mc.thePlayer.inventoryContainer.windowId, finalI, 0, 0, mc.thePlayer));
                        clickQueue.add(() -> mc.playerController.windowClick(mc.thePlayer.inventoryContainer.windowId, 35 + (int)autoSwordSlot.getValDouble(), 0, 0, mc.thePlayer));
                        clickQueue.add(() -> mc.playerController.windowClick(mc.thePlayer.inventoryContainer.windowId, finalI, 0, 0, mc.thePlayer));

                        mc.thePlayer.sendQueue.addToSendQueueNoEvent(new C0DPacketCloseWindow(0));

                        return true;
                    }
                }
            }
        }
        return false;
    }

    private boolean equipArmor() {
        if (equipArmor.getValBoolean()) {

            for (int i = 9; i < 45; i++) {
                if (!mc.thePlayer.inventoryContainer.getSlot(i).getHasStack())
                    continue;

                ItemStack stackInSlot = mc.thePlayer.inventoryContainer.getSlot(i).getStack();

                if (!(stackInSlot.getItem() instanceof ItemArmor))
                    continue;

                if (ContainerUtils.getArmorItemsEquipSlot(stackInSlot, false) == -1)
                    continue;

                if (mc.thePlayer.getEquipmentInSlot(ContainerUtils.getArmorItemsEquipSlot(stackInSlot, true)) == null) {
                    System.out.println("No stack in slot : " + stackInSlot.getUnlocalizedName());
                    if (equipTimer.hasPassed(equipDelay.getValDouble())) {
                        int finalI = i;
                        mc.thePlayer.sendQueue.addToSendQueueNoEvent(new C0BPacketEntityAction(mc.thePlayer, C0BPacketEntityAction.Action.OPEN_INVENTORY));


                        clickQueue.add(() -> mc.playerController.windowClick(mc.thePlayer.inventoryContainer.windowId, finalI, 0, 0, mc.thePlayer));
                        clickQueue.add(() -> mc.playerController.windowClick(mc.thePlayer.inventoryContainer.windowId, ContainerUtils.getArmorItemsEquipSlot(stackInSlot, false), 0, 0, mc.thePlayer));

                        mc.thePlayer.sendQueue.addToSendQueueNoEvent(new C0DPacketCloseWindow(0));

                        return true;
                    }
                } else {
                    ItemStack stackInEquipmentSlot = mc.thePlayer.getEquipmentInSlot(ContainerUtils.getArmorItemsEquipSlot(stackInSlot, true));
                    if (ItemUtils.compareProtection(stackInSlot, stackInEquipmentSlot) == stackInSlot) {
                        System.out.println("Stack in slot : " + stackInSlot.getUnlocalizedName());
                        if (equipTimer.hasPassed(equipDelay.getValDouble())) {
                            int finalI1 = i;
                            mc.thePlayer.sendQueue.addToSendQueueNoEvent(new C0BPacketEntityAction(mc.thePlayer, C0BPacketEntityAction.Action.OPEN_INVENTORY));


                            clickQueue.add(() -> mc.playerController.windowClick(mc.thePlayer.inventoryContainer.windowId, finalI1, 0, 0, mc.thePlayer));
                            clickQueue.add(() -> mc.playerController.windowClick(mc.thePlayer.inventoryContainer.windowId, ContainerUtils.getArmorItemsEquipSlot(stackInSlot, false), 0, 0, mc.thePlayer));
                            clickQueue.add(() -> mc.playerController.windowClick(mc.thePlayer.inventoryContainer.windowId, finalI1, 0, 0, mc.thePlayer));

                            mc.thePlayer.sendQueue.addToSendQueueNoEvent(new C0DPacketCloseWindow(0));

                            return true;
                        }
                    }
                }
            }
        }
        return false;
    }

    public boolean clean() {
        if (clean.getValBoolean()) {
            if (mc.thePlayer == null)
                return false;

            ArrayList<Integer> uselessItem = new ArrayList<Integer>();
            for (int i = 0; i < 45; i++) {

                if (!mc.thePlayer.inventoryContainer.getSlot(i).getHasStack())
                    continue;

                ItemStack stackInSlot = mc.thePlayer.inventoryContainer.getSlot(i).getStack();

                if (mc.thePlayer.inventory.armorItemInSlot(0) == stackInSlot
                        || mc.thePlayer.inventory.armorItemInSlot(1) == stackInSlot
                        || mc.thePlayer.inventory.armorItemInSlot(2) == stackInSlot
                        || mc.thePlayer.inventory.armorItemInSlot(3) == stackInSlot)
                    continue;

                if (isGarbo(i))
                    uselessItem.add(i);

            }

            if (uselessItem.size() > 0) {
                cleaning = true;
                if (dropTimer.hasPassed(dropDelay.getValDouble())) {
                    if (!(mc.thePlayer.inventory.currentItem == uselessItem.get(0))) {

                        mc.thePlayer.sendQueue.addToSendQueueNoEvent(new C0BPacketEntityAction(mc.thePlayer, C0BPacketEntityAction.Action.OPEN_INVENTORY));

                        mc.playerController.windowClick(mc.thePlayer.inventoryContainer.windowId, uselessItem.get(0), 1, 4, mc.thePlayer);

                        mc.thePlayer.sendQueue.addToSendQueueNoEvent(new C0DPacketCloseWindow(0));

                    }
                    uselessItem.remove(0);
                    dropTimer.updateLastTime();
                }
                return true;
            }
        }
        return false;
    }

    private boolean isGarbo(int slot) {
        ItemStack stackInSlot = mc.thePlayer.inventoryContainer.getSlot(slot).getStack();
        if (stackInSlot.getItem() instanceof ItemSword) {
            for (int i = 0; i < 44; i++) {
                if (i == slot) continue;
                if (mc.thePlayer.inventoryContainer.getSlot(i).getHasStack()) {
                    ItemStack stackAtIndex = mc.thePlayer.inventoryContainer.getSlot(i).getStack();
                    if (stackAtIndex.getItem() instanceof ItemSword) {
                        if (ItemUtils.compareDamage(stackInSlot, stackAtIndex) == stackAtIndex) {
                            return true;
                        }
                    }
                }
            }
            return false;
        }

        if (stackInSlot.getItem() instanceof ItemAxe || stackInSlot.getItem() instanceof ItemBow || stackInSlot.getItem() instanceof ItemFishingRod || stackInSlot.getItem() instanceof ItemPickaxe || Item.getIdFromItem(stackInSlot.getItem()) == 346) {
            for (int i = 44; i > 0; i--) {
                if (i == slot) continue;
                if (mc.thePlayer.inventoryContainer.getSlot(i).getHasStack()) {
                    ItemStack stackAtIndex = mc.thePlayer.inventoryContainer.getSlot(i).getStack();
                    if ((stackAtIndex.getItem() instanceof ItemSword || stackAtIndex.getItem() instanceof ItemAxe || stackAtIndex.getItem() instanceof ItemBow || stackAtIndex.getItem() instanceof ItemFishingRod || stackAtIndex.getItem() instanceof ItemAxe || stackAtIndex.getItem() instanceof ItemPickaxe || Item.getIdFromItem(stackAtIndex.getItem()) == 346)) {
                        if (Item.getIdFromItem(stackAtIndex.getItem()) == Item.getIdFromItem(stackInSlot.getItem())) {
                            return true;
                        }
                    }
                }
            }
            return false;
        }

        if (stackInSlot.hasDisplayName())
            return false;

        if (isBestArmorItem(stackInSlot))
            return false;

        if (stackInSlot.getItem() instanceof ItemFood)
            return false;

        if (stackInSlot.getItem() instanceof ItemBlock)
            return false;

        if (stackInSlot.getItem() instanceof ItemPotion) {
            return ItemUtils.isPotionNegative(stackInSlot);
        }

        if (stackInSlot.getItem() instanceof ItemTool) {
            return true;
        }

        if (Item.getIdFromItem(stackInSlot.getItem()) == 367)
            return true; // rotten flesh
        if (Item.getIdFromItem(stackInSlot.getItem()) == 259)
            return true; // flint & steel
        if (Item.getIdFromItem(stackInSlot.getItem()) == 262)
            return true; // arrow
        if (Item.getIdFromItem(stackInSlot.getItem()) == 264)
            return true; // diamond
        if (Item.getIdFromItem(stackInSlot.getItem()) == 265)
            return true; // iron
        if (Item.getIdFromItem(stackInSlot.getItem()) == 336)
            return true; // brick
        if (Item.getIdFromItem(stackInSlot.getItem()) == 266)
            return true; // gold ingot
        if (Item.getIdFromItem(stackInSlot.getItem()) == 345)
            return true; // compass
        if (Item.getIdFromItem(stackInSlot.getItem()) == 46)
            return true; // tnt
        if (Item.getIdFromItem(stackInSlot.getItem()) == 261)
            return true; // bow
        if (Item.getIdFromItem(stackInSlot.getItem()) == 262)
            return true; // arrow
        if (Item.getIdFromItem(stackInSlot.getItem()) == 116)
            return true; // enchanting table
        if (Item.getIdFromItem(stackInSlot.getItem()) == 54)
            return true;

        return true;
    }

    private boolean isBestTool(ItemStack itemStack) {
        return false;
    }

    private boolean isBestArmorItem(ItemStack armorStack) {
        if (armorStack.getItem() instanceof ItemArmor) {
            int equipSlot = ContainerUtils.getArmorItemsEquipSlot(armorStack, true);

            if (equipSlot == -1)
                return false;

            if (mc.thePlayer.getEquipmentInSlot(equipSlot) == null) {
                for (int slotNum = 44; slotNum > 0; slotNum--) {
                    if (!mc.thePlayer.inventoryContainer.getSlot(slotNum).getHasStack())
                        continue;

                    ItemStack stackInSlot = mc.thePlayer.inventoryContainer.getSlot(slotNum).getStack();

                    if (!(stackInSlot.getItem() instanceof ItemArmor))
                        continue;

                    if (ContainerUtils.getArmorItemsEquipSlot(stackInSlot, true) == equipSlot
                            && compareArmorItems(armorStack, stackInSlot) == stackInSlot)
                        return false;
                }
            } else {
                return compareArmorItems(armorStack, mc.thePlayer.getEquipmentInSlot(equipSlot)) != mc.thePlayer.getEquipmentInSlot(equipSlot);
            }

            return true;
        }
        return false;
    }

    private ItemStack compareArmorItems(ItemStack item1, ItemStack item2) {
        if (item1 == null || item2 == null)
            return null;

        if (!(item1.getItem() instanceof ItemArmor && item2.getItem() instanceof ItemArmor))
            return null;

        if (ContainerUtils.getArmorItemsEquipSlot(item1, true) != ContainerUtils.getArmorItemsEquipSlot(item2, true))
            return null;

        double item1Protection = ItemUtils.getArmorProtection(item1);
        double item2Protection = ItemUtils.getArmorProtection(item2);

        if (item1Protection != item2Protection) {
            if (item1Protection > item2Protection)
                return item1;
            else
                return item2;
        } else {
            if (item1.getMaxDamage() > item2.getMaxDamage())
                return item2;
            else
                return item1;
        }
    }
	
}
