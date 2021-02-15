package rip.helium.module.modules.movement;

import java.util.ArrayList;

import rip.helium.event.EventTarget;
import rip.helium.event.events.impl.network.PacketReceiveEvent;
import rip.helium.event.events.impl.network.PacketSendEvent;
import rip.helium.event.events.impl.player.MoveEvent;
import rip.helium.module.Module;
import rip.helium.module.modules.movement.speed.BhopMode;
import rip.helium.module.modules.movement.speed.FasthopMode;
import rip.helium.module.modules.movement.speed.GroundMode;
import rip.helium.module.modules.movement.speed.ViperMode;
import rip.helium.setting.Setting;
import rip.helium.utils.client.Timer;
import rip.helium.utils.render.ColorUtils;

public class Speed extends Module {

	private Setting mode;
	
	Timer ncpTimer;
	
	private BhopMode bhop;
	private FasthopMode fasthop;
	private GroundMode ground;
	private ViperMode viper;
	
	public Speed(int bind, String name, Category category) {
		super(bind, name, category);
		this.setColor(ColorUtils.generateColor());
		
		ArrayList<String> modes = new ArrayList<String>();
		modes.add("Bhop");
		modes.add("Fasthop");
		modes.add("Ground");
		modes.add("Viper");
		
		this.mode = new Setting("Mode", this, "Ground", modes);
		
		mc.hackedClient.getSettingManager().addSetting(mode);
		
		this.ncpTimer = new Timer();
		
		this.bhop = new BhopMode();
		this.fasthop = new FasthopMode();
		this.ground = new GroundMode();
		this.viper = new ViperMode();
	}
	
	@EventTarget
	public void onPacketSend(PacketSendEvent event) {
		switch(this.mode.getValString()) {
			case "Viper":
				this.viper.onPacketSend(event);
				break;
		}
	}
	
	@EventTarget
	public void onPacketReceive(PacketReceiveEvent event) {
		switch(this.mode.getValString()) {
			case "Viper":
				this.viper.onPacketReceive(event);
				break;
		}
	}
	
	@EventTarget
	public void onMove(MoveEvent event) {
		this.setSuffix(this.mode.getValString());
		switch(this.mode.getValString()) {
			case "Bhop":
				this.bhop.onMove(event);
				break;
			case "Fasthop":
				this.fasthop.onMove(event);
				break;
			case "Ground":
				this.ground.onMove(event);
				break;
		}
	}
	
}
