package rip.helium.command.commands;

import rip.helium.command.Command;
import rip.helium.utils.client.ClientUtils;

public class VClipCommand extends Command {
	
	public VClipCommand() {
		this.setNames(new String[] {"vclip"});
	}
	
	public void runCommand(String[] args) {
	    try {
	    	mc.thePlayer.noClip = true;
	    	this.mc.thePlayer.setBoundingBox(this.mc.thePlayer.getEntityBoundingBox().offset(0.0D, Double.parseDouble(args[1]), 0.0D));
	    	mc.thePlayer.noClip = false;
	    	ClientUtils.addConsoleMessage("Clipped " + Integer.parseInt(args[1]) + " on the Y axis");
	    } catch (Exception e) {
	    	ClientUtils.addConsoleMessage("Invalid argument!");
	    }
	}
	  
	public String getHelp() {
	    return "VClip - vclip <blocks> - Teleports the player the set amount on the Y axis";
	}
}

