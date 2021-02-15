package rip.helium.command.commands;

import rip.helium.command.Command;
import rip.helium.utils.client.ClientUtils;

public class UnknownCommand extends Command {
	
	public void runCommand(String[] args) {
		ClientUtils.addConsoleMessage("Unknown command. Type \"help\" for a list of commands");
	}
	
}
