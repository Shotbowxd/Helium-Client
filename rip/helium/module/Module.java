package rip.helium.module;

import net.minecraft.network.play.client.C01PacketChatMessage;
import rip.helium.ClientSupport;
import rip.helium.event.EventManager;

public class Module implements ClientSupport {
	
	public enum Category {
		Combat, Player, Movement, World, Render, Exploits, Misc;
	}
	
	private int bind = 0, color = 0xffffffff, animationOn = 100, animationOff = 0;
	private String name = "", displayName = "", suffix = "";
	private boolean toggled = false, hidden = false;
	private Category category;
	
	public Module(int bind, String name, Category category) {
		this.bind = bind;
		this.name = name;
		this.displayName = name;
		this.category = category;
	}
	
	public Module(int bind, String name, String displayName, Category category) {
		this.bind = bind;
		this.name = name;
		this.displayName = displayName;
		this.category = category;
	}

	public int getBind() {
		return bind;
	}

	public void setBind(int bind) {
		this.bind = bind;
	}

	public boolean isHidden() {
		return hidden;
	}

	public void setHidden(boolean hidden) {
		this.hidden = hidden;
	}

	public int getColor() {
		return color;
	}

	public void setColor(int color) {
		this.color = color;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getSuffix() {
		return suffix;
	}

	public void setSuffix(String suffix) {
		this.suffix = suffix;
	}

	public String getDisplayName() {
		return displayName;
	}

	public void setDisplayName(String displayName) {
		this.displayName = displayName;
	}
	
	public Category getCategory() {
		return this.category;
	}
	
	public void setCategory(Category category) {
		this.category = category;
	}
	
	public int getAnimationOn() {
        return animationOn;
    }

    public void setAnimationOn(int animation) {
        this.animationOn = animation;
    }
	
	public int getAnimationOff() {
		return animationOff;
	}

	public void setAnimationOff(int animationOff) {
		this.animationOff = animationOff;
	}

	public boolean getState() {
		return this.toggled;
	}
	
	public void setState(boolean state) {
		this.toggled = state;
		onToggle();
		if(state) {
			onEnable();
			if(mc.theWorld != null) {
				if(mc.hackedClient.getModuleManager().getModule("Announcer").getState()) {
					if(this.getName().equalsIgnoreCase("ClickGUI")) return;
					mc.getNetHandler().addToSendQueue(new C01PacketChatMessage("I just turned " + this.getDisplayName() + " on. #Helium"));
				}
			}
		}else {
			onDisable();
			if(mc.theWorld != null)
				if(mc.hackedClient.getModuleManager().getModule("Announcer").getState()) {
					if(this.getName().equalsIgnoreCase("ClickGUI")) return;
					mc.getNetHandler().addToSendQueue(new C01PacketChatMessage("I just turned " + this.getDisplayName() + " off. #Helium"));
				}
		}
		this.animationOn = 100;
		this.animationOff = 0;
	}
	
	public void toggle() {
		setState(!getState());
	}
	
	public void onEnable() { EventManager.register(this); }
	public void onDisable() { EventManager.unregister(this); }
	public void onToggle() { }
	
}