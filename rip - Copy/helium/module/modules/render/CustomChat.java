package rip.helium.module.modules.render;

import net.minecraft.client.gui.GuiNewChat;
import rip.helium.gui.chat.GuiCustomChat;
import rip.helium.module.Module;
import rip.helium.utils.render.ColorUtils;

public class CustomChat extends Module {

	public CustomChat(int bind, String name, String displayName, Category category) {
		super(bind, name, displayName, category);
		this.setColor(ColorUtils.generateColor());
		this.setHidden(true);
		this.setState(true);
	}
	
	@Override
	public void onEnable() {
	    GuiCustomChat guiCustomChat = new GuiCustomChat(mc);
	    GuiNewChat guiNewChat = copyLines(mc.ingameGUI.getChatGUI(), (GuiNewChat)guiCustomChat);
	    mc.ingameGUI.setChatGui(guiNewChat);
	    super.onEnable();
	}
	  
	@Override
	public void onDisable() {
	    GuiNewChat chat = new GuiNewChat(mc);
	    chat = copyLines(mc.ingameGUI.getChatGUI(), chat);
	    mc.ingameGUI.setChatGui(chat);
	    super.onDisable();
	}
	  
	private GuiNewChat copyLines(GuiNewChat oldChat, GuiNewChat newChat) {
	    for (Object o: oldChat.getChatLines())
	      newChat.getChatLines().add(o); 
	    for (Object o: oldChat.getField_146253_i())
	      newChat.getField_146253_i().add(o); 
	    for (String o: oldChat.getSentMessages())
	      newChat.getSentMessages().add(o); 
	    return newChat;
	}

}
