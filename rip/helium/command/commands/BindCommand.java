package rip.helium.command.commands;

import java.awt.event.KeyEvent;

import rip.helium.command.Command;
import rip.helium.module.Module;
import rip.helium.utils.client.ClientUtils;

public class BindCommand extends Command {
	
	public BindCommand() {
		this.setNames(new String[] {"bind", "b"});
	}
	
	public void runCommand(String[] args) {
	    String modName = "";
	    String keyName = "";
	    if (args.length > 1) {
	    	modName = args[1];
	    	if (args.length > 2)
	    		keyName = args[2]; 
	    } 
	    Module module = mc.hackedClient.getModuleManager().getModule(modName);
	    if (module.getName().equalsIgnoreCase("null") || module.getName().equalsIgnoreCase("Targeting") || module.getName().equalsIgnoreCase("ClickGUI") || module.getName().equalsIgnoreCase("Colors")) {
	    	ClientUtils.addConsoleMessage("Invalid module.");
	    	return;
	    } 
	    if (keyName == "") {
	    	ClientUtils.addConsoleMessage(String.valueOf(String.valueOf(module.getDisplayName())) + "'s bind has been cleared.");
	    	module.setBind(0);
	    	mc.hackedClient.getModuleManager().saveConfig(mc.hackedClient.getGson());
	    	return;
	    } 
	    try {
	    	module.setBind((int)KeyEvent.class.getField("VK_" + keyName.toUpperCase()).getInt(null));
		    mc.hackedClient.getModuleManager().saveConfig(mc.hackedClient.getGson());
			if ((int)KeyEvent.class.getField("VK_" + keyName.toUpperCase()).getInt(null) == 0) {
				ClientUtils.addConsoleMessage("Invalid key entered, Bind cleared.");
			} else {
				ClientUtils.addConsoleMessage(String.valueOf(String.valueOf(module.getDisplayName())) + " bound to " + keyName);
			}
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (NoSuchFieldException e) {
			e.printStackTrace();
		} catch (SecurityException e) {
			e.printStackTrace();
		} 
	}
	  
	public String getHelp() {
	    return "Bind - bind <b> (module) (key) - Bind a module to a key.";
	}
	
}