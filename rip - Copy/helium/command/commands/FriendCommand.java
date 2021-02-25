package rip.helium.command.commands;

import rip.helium.command.Command;
import rip.helium.utils.client.ClientUtils;

public class FriendCommand extends Command {
	
	public FriendCommand() {
		this.setNames(new String[] {"friend", "f"});
	}
	
	public void runCommand(String[] args) {
		if (args.length < 3) {
			ClientUtils.addConsoleMessage(getHelp());
			return;
		} 
		if (args[1].equalsIgnoreCase("add") || args[1].equalsIgnoreCase("a")) {
			String alias = args[2];
			if (args.length > 3) {
				alias = args[3];
				if (alias.startsWith("\"") && args[args.length - 1].endsWith("\"")) {
					alias = alias.substring(1, alias.length());
					for (int i = 4; i < args.length; i++)
						alias = String.valueOf(String.valueOf(alias)) + " " + args[i].replace("\"", ""); 
				} 
			} 
			if (mc.hackedClient.getFriendManager().isFriend(args[2]) && args.length < 3) {
				ClientUtils.addConsoleMessage(String.valueOf(String.valueOf(args[2])) + " is already your friend.");
				return;
			} 
			mc.hackedClient.getFriendManager().removeFriend(args[2]);
			mc.hackedClient.getFriendManager().addFriend(args[2], alias);
			ClientUtils.addConsoleMessage("Added " + args[2] + ((args.length > 3) ? (" as " + alias) : ""));
		} else if (args[1].equalsIgnoreCase("del") || args[1].equalsIgnoreCase("d")) {
			if (mc.hackedClient.getFriendManager().isFriend(args[2])) {
				mc.hackedClient.getFriendManager().removeFriend(args[2]);
				ClientUtils.addConsoleMessage("Removed friend: " + args[2]);
			} else {
				ClientUtils.addConsoleMessage(String.valueOf(String.valueOf(args[2])) + " is not your friend.");
			} 
		} else {
			ClientUtils.addConsoleMessage(getHelp());
		} 
	}
  
	public String getHelp() {
		return "Friend - friend <f>  (add <a> | del <d>) (name) [alias | \"alias w/ spaces\"].";
	}
	
}
