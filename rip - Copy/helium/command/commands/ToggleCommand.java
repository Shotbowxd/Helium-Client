package rip.helium.command.commands;

import rip.helium.command.Command;
import rip.helium.module.Module;
import rip.helium.utils.client.ClientUtils;

public class ToggleCommand extends Command {
	
	public ToggleCommand() {
		this.setNames(new String[]{"toggle", "t"});
	}
	
	public void runCommand(String[] args) {
		String modName = "";
	    if (args.length > 1)
	    	modName = args[1]; 
	    Module module = mc.hackedClient.getModuleManager().getModule(modName);
	    if (module.getName().equalsIgnoreCase("null") || module.getName().equalsIgnoreCase("Targeting") || module.getName().equalsIgnoreCase("ClickGUI") || module.getName().equalsIgnoreCase("Colors")) {
	    	ClientUtils.addConsoleMessage("Invalid Module.");
	    	return;
	    } 
	    module.toggle();
	    ClientUtils.addConsoleMessage(String.valueOf(String.valueOf(module.getDisplayName())) + " is now " + (module.getState() ? "\247aenabled" : "\247cdisabled"));
	    mc.hackedClient.getModuleManager().saveConfig(mc.hackedClient.getGson());
	}
	  
	  
	public String getHelp() {
		return "Toggle - toggle <t, tog> (module) - Toggles a module on or off";
	}
}
