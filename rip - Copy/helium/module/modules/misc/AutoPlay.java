package rip.helium.module.modules.misc;

import java.util.ArrayList;
import java.util.Objects;

import net.minecraft.client.gui.GuiDownloadTerrain;
import net.minecraft.network.play.server.S02PacketChat;
import rip.helium.event.EventTarget;
import rip.helium.event.events.impl.network.PacketReceiveEvent;
import rip.helium.module.Module;
import rip.helium.setting.Setting;
import rip.helium.utils.client.ClientUtils;
import rip.helium.utils.client.Timer;
import rip.helium.utils.misc.MathUtils;
import rip.helium.utils.render.ColorUtils;

public class AutoPlay extends Module {

	private Setting mode;
	
	public AutoPlay(int bind, String name, String displayName, Category category) {
		super(bind, name, displayName, category);
		this.setColor(ColorUtils.generateColor());
		
		ArrayList<String> modes = new ArrayList<String>();
		modes.add("Doubles");
		modes.add("Solo");
		
		this.mode = new Setting("Mode", this, "Doubles", modes);
		
		mc.hackedClient.getSettingManager().addSetting(this.mode);
	}
	
	@EventTarget
	public void onPacketReceive(PacketReceiveEvent event) {
		if (event.getPacket() instanceof S02PacketChat) {
            S02PacketChat packet = (S02PacketChat) event.getPacket();
            if (packet.getChatComponent().getUnformattedText().contains("here!") || mc.thePlayer.isSpectator()) {
                if (!(mc.currentScreen instanceof GuiDownloadTerrain) || Objects.nonNull(mc.thePlayer)) {
                	Timer timer = new Timer();
                    boolean solo = mode.getValString().equalsIgnoreCase("Solo");
                    if (timer.hasPassed(MathUtils.getRandomInRange(4000, 5000))) {
                        if (solo) {
                            mc.thePlayer.sendChatMessage("/play solo_insane");
                            ClientUtils.addConsoleMessage("AutoPlay: Joined a solo insane match.");
                        } else {
                            mc.thePlayer.sendChatMessage("/play doubles_insane");
                            ClientUtils.addConsoleMessage("AutoPlay: Joined a doubles insane match.");
                        }
                    }
                }
            }
        }
	}
	
	

}
