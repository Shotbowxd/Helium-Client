package rip.helium.module.modules.movement;

import java.util.Objects;

import org.lwjgl.input.Keyboard;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiChat;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.inventory.GuiChest;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.network.play.client.C0BPacketEntityAction;
import net.minecraft.network.play.client.C0DPacketCloseWindow;
import net.minecraft.network.play.client.C0EPacketClickWindow;
import rip.helium.event.EventTarget;
import rip.helium.event.events.impl.network.PacketSendEvent;
import rip.helium.event.events.impl.player.InputUpdateEvent;
import rip.helium.event.events.impl.player.UpdateEvent;
import rip.helium.gui.click.ClickGUIScreen;
import rip.helium.module.Module;
import rip.helium.setting.Setting;
import rip.helium.utils.client.Timer;
import rip.helium.utils.render.ColorUtils;

public class ScreenWalk extends Module {

	public boolean closed, megalul, cancancel;
	private Setting desync;
	private Timer timer;
	
	public ScreenWalk(int bind, String name, String displayName, Category category) {
		super(bind, name, displayName, category);
		this.setColor(ColorUtils.generateColor());
		this.setState(true);
		
		this.desync = new Setting("Desync", this, false);
		this.timer = new Timer();
	}
	
	@EventTarget
	public void onPacketSend(PacketSendEvent event) {
		if (desync.getValBoolean() && !(Minecraft.getMinecraft().currentScreen instanceof GuiChest)) {
            if (event.getPacket() instanceof C0BPacketEntityAction) {
                C0BPacketEntityAction packet = (C0BPacketEntityAction) event.getPacket();
                if (packet.getAction() == C0BPacketEntityAction.Action.OPEN_INVENTORY) {
                    closed = false;
                }
            }

            if (event.getPacket() instanceof C0EPacketClickWindow) {
                mc.thePlayer.sendQueue.addToSendQueue(new C0BPacketEntityAction(mc.thePlayer, C0BPacketEntityAction.Action.OPEN_INVENTORY));
                closed = false;
                megalul = true;
                event.setCancelled(cancancel);
            }
        }
	}
	
	@EventTarget
	public void onInput(InputUpdateEvent event) {
		KeyBinding[] moveKeys = {mc.gameSettings.keyBindRight, mc.gameSettings.keyBindLeft,
                mc.gameSettings.keyBindBack, mc.gameSettings.keyBindForward, mc.gameSettings.keyBindJump,
                mc.gameSettings.keyBindSprint,};
        if ((mc.currentScreen instanceof GuiScreen)
                && !(mc.currentScreen instanceof GuiChat) && !(mc.currentScreen instanceof ClickGUIScreen)) {
            KeyBinding[] array;
            int length = (array = moveKeys).length;
            for (int i = 0; i < length; i++) {
                KeyBinding key = array[i];
                key.pressed = Keyboard.isKeyDown(key.getKeyCode());
            }
        } else if (Objects.isNull(mc.currentScreen)) {
            KeyBinding[] array2;
            int length2 = (array2 = moveKeys).length;
            for (int j = 0; j < length2; j++) {
                KeyBinding bind = array2[j];
                try {
                    if (!Keyboard.isKeyDown(bind.getKeyCode())) {
                        KeyBinding.setKeyBindState(bind.getKeyCode(), false);
                    }
                } catch (Exception e) {

                }
            }
        }
	}
	
	@EventTarget
	public void onUpdatePre(UpdateEvent event) {
		if (desync.getValBoolean() && !(Minecraft.getMinecraft().currentScreen instanceof GuiChest)) {
            if (!closed) {
                if (timer.hasPassed(51)) {
                    mc.thePlayer.sendQueue.addToSendQueue(new C0DPacketCloseWindow(0));
                    closed = true;
                    timer.updateLastTime();
                }
            } else {
                if (megalul) {
                    if (timer.hasPassed(52)) {
                        cancancel = true;
                        timer.updateLastTime();
                    } else {
                        cancancel = false;
                    }
                }
            }
        }
	}

}
