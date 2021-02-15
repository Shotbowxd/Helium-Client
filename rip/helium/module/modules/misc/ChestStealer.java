package rip.helium.module.modules.misc;

import java.util.ArrayList;

import net.minecraft.block.BlockChest;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.inventory.GuiChest;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemAxe;
import net.minecraft.item.ItemBow;
import net.minecraft.item.ItemFishingRod;
import net.minecraft.item.ItemPickaxe;
import net.minecraft.item.ItemPotion;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemSword;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import rip.helium.event.EventTarget;
import rip.helium.event.events.impl.client.RunTickEvent;
import rip.helium.event.events.impl.player.UpdateEvent;
import rip.helium.module.Module;
import rip.helium.setting.Setting;
import rip.helium.utils.client.Timer;
import rip.helium.utils.player.ContainerUtils;
import rip.helium.utils.player.GuiUtils;
import rip.helium.utils.player.ItemUtils;
import rip.helium.utils.render.ColorUtils;

public class ChestStealer extends Module {

	private Setting delay;
	private Setting ignoreCustomChest;
	private Setting harmfulPotion, worseSword, duplicateTool, worseArmor;
	
	private final ArrayList<BlockPos> lootedChestPositions;
    public boolean stealing;
	private Timer timer;
	private Timer takeTimer;
	private double theDelay;
	
	public ChestStealer(int bind, String name, String displayName, Category category) {
		super(bind, name, displayName, category);
		this.setColor(ColorUtils.generateColor());
		
		this.delay = new Setting("Delay", this, 150, 0, 250, false);
		this.ignoreCustomChest = new Setting("IgnoreCustomChest", this, true);
		this.harmfulPotion = new Setting("HarmfulPotion", this, false);
		this.worseSword = new Setting("WorseSword", this, false);
		this.duplicateTool = new Setting("DuplicateTool", this, false);
		this.worseArmor = new Setting("WorseArmor", this, false);
		
		mc.hackedClient.getSettingManager().addSetting(this.delay);
		mc.hackedClient.getSettingManager().addSetting(this.ignoreCustomChest);
		mc.hackedClient.getSettingManager().addSetting(this.harmfulPotion);
		mc.hackedClient.getSettingManager().addSetting(this.worseSword);
		mc.hackedClient.getSettingManager().addSetting(this.duplicateTool);
		mc.hackedClient.getSettingManager().addSetting(this.worseArmor);
		
		this.timer = new Timer();
		this.takeTimer = new Timer();
		
		lootedChestPositions = new ArrayList<>();
	}
	
	@Override
	public void onEnable() {
		super.onEnable();
		theDelay = this.delay.getValDouble();
	}
	
	@EventTarget
	public void onRunTick(RunTickEvent event) {
		if (mc.theWorld == null) {
            lootedChestPositions.clear();
        }
	}
	
	@EventTarget
	public void onUpdatePre(UpdateEvent event) {
		if (mc.currentScreen instanceof GuiChest) {
            GuiChest chest = (GuiChest) mc.currentScreen;
            if (chest.lowerChestInventory.getName().equals("Enchant Item")) {
                ItemStack stack = chest.lowerChestInventory.getStackInSlot(33);
                boolean contains = false;
                for (String s : stack.getTooltip(mc.thePlayer, true)) {
                    if (s.contains("Blessing V")) {
                        contains = true;
                        break;
                    }
                }

                if (timer.hasPassed(500)) {
                    if (!contains) {
                        InventoryPlayer inventoryplayer = mc.thePlayer.inventory;
                        if ((inventoryplayer.getItemStack() != null
                                && inventoryplayer.getItemStack().getDisplayName().toLowerCase().contains("book"))

                                || (chest.lowerChestInventory.getStackInSlot(13) != null
                                && chest.lowerChestInventory.getStackInSlot(13).getDisplayName().toLowerCase().contains("book")))
                            ;

                        mc.playerController.windowClick(chest.inventorySlots.windowId, 13, 0, 0, mc.thePlayer);
                    } else {
                        mc.playerController.windowClick(chest.inventorySlots.windowId, 33, 0, 0, mc.thePlayer);
                    }
                    timer.updateLastTime();
                }

                return;
            }

            BlockPos pos = mc.objectMouseOver.getBlockPos();
            if (pos != null && mc.theWorld.getBlockState(pos).getBlock() instanceof BlockChest) {
                lootedChestPositions.add(pos);
                BlockChest blockChest = (BlockChest) mc.theWorld.getBlockState(mc.objectMouseOver.getBlockPos()).getBlock();
                for (EnumFacing enumfacing : EnumFacing.Plane.HORIZONTAL.facings()) {
                    if (mc.theWorld.getBlockState(pos.offset(enumfacing)).getBlock() == blockChest) {
                        lootedChestPositions.add(pos.offset(enumfacing));
                    }
                }
            }

            boolean noMoreItems = true;
            for (int index = 0; index < chest.lowerChestInventory.getSizeInventory(); index++) {
                ItemStack stack = chest.lowerChestInventory.getStackInSlot(index);
                if (stack == null)
                    continue;

                if (!harmfulPotion.getValBoolean()) {
                    if (stack.getItem() instanceof ItemPotion) {
                        if (ItemUtils.isPotionNegative(stack))
                            continue;
                    }
                }

                if (!worseSword.getValBoolean()) {
                    if (stack.getItem() instanceof ItemSword) {
                        boolean shouldContinue = false;
                        for (int i = 0; i < 44; i++) {
                            if (i == index) continue;
                            if (mc.thePlayer.inventoryContainer.getSlot(i).getHasStack()) {
                                ItemStack stackAtIndex = mc.thePlayer.inventoryContainer.getSlot(i).getStack();
                                if (stackAtIndex.getItem() instanceof ItemSword) {
                                    if (ItemUtils.compareDamage(stack, stackAtIndex) == stackAtIndex) {
                                        shouldContinue = true;
                                        break;
                                    }
                                }
                            }
                        }
                        if (shouldContinue)
                            continue;
                    }
                }

                if (!duplicateTool.getValBoolean()) {
                    if (stack.getItem() instanceof ItemAxe || stack.getItem() instanceof ItemBow || stack.getItem() instanceof ItemFishingRod || stack.getItem() instanceof ItemPickaxe || Item.getIdFromItem(stack.getItem()) == 346) {
                        boolean shouldContinue = false;
                        for (int i = 44; i > 0; i--) {
                            if (i == index) continue;
                            if (mc.thePlayer.inventoryContainer.getSlot(i).getHasStack()) {
                                ItemStack stackAtIndex = mc.thePlayer.inventoryContainer.getSlot(i).getStack();
                                if ((stackAtIndex.getItem() instanceof ItemSword || stackAtIndex.getItem() instanceof ItemAxe || stackAtIndex.getItem() instanceof ItemBow || stackAtIndex.getItem() instanceof ItemFishingRod || stackAtIndex.getItem() instanceof ItemAxe || stackAtIndex.getItem() instanceof ItemPickaxe || Item.getIdFromItem(stackAtIndex.getItem()) == 346)) {
                                    if (Item.getIdFromItem(stackAtIndex.getItem()) == Item.getIdFromItem(stack.getItem())) {
                                        shouldContinue = true;
                                        break;
                                    }
                                }
                            }
                        }
                        if (shouldContinue)
                            continue;
                    }
                }

                if (!worseArmor.getValBoolean()) {
                    if (stack.getItem() instanceof ItemArmor) {
                        int equipmentType = ContainerUtils.getArmorItemsEquipSlot(stack, true);
                        if (equipmentType != -1) {
                            if (mc.thePlayer.getEquipmentInSlot(equipmentType) != null) {
                                if (ItemUtils.compareProtection(stack, mc.thePlayer.getEquipmentInSlot(equipmentType)) == mc.thePlayer.getEquipmentInSlot(equipmentType)) {
                                    continue;
                                }
                            } else {
                                boolean shouldContinue = false;
                                for (int i = 44; i > 0; i--) {
                                    if (i == index) continue;
                                    if (mc.thePlayer.inventoryContainer.getSlot(i).getHasStack()) {
                                        ItemStack stackAtIndex = mc.thePlayer.inventoryContainer.getSlot(i).getStack();
                                        if (stackAtIndex.getItem() instanceof ItemArmor) {
                                            if (ContainerUtils.getArmorItemsEquipSlot(stackAtIndex, true) == equipmentType) {
                                                if (ItemUtils.compareProtection(stack, stackAtIndex) == stackAtIndex) {
                                                    shouldContinue = true;
                                                    break;
                                                }
                                            }
                                        }
                                    }
                                }
                                if (shouldContinue)
                                    continue;
                            }
                        }
                    }
                }

                noMoreItems = false;

                if (takeTimer.hasPassed(delay.getValDouble())) {
                    mc.playerController.windowClick(chest.inventorySlots.windowId, index, 0, 1, mc.thePlayer);
                    takeTimer.updateLastTime();
                    stealing = true;
                    theDelay += 25;
                    if (theDelay >= (this.delay.getValDouble() + (this.delay.getValDouble() / 2))) {
                    	theDelay = this.delay.getValDouble();
                    }
                    return;
                }
            }

            if (noMoreItems) {
                GuiUtils.closeScreenAndReturn();
                stealing = false;
            }
        }
	}
	
}
