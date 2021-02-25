package rip.helium.command;

import java.util.ArrayList;

import rip.helium.ClientSupport;
import rip.helium.command.commands.BindCommand;
import rip.helium.command.commands.FriendCommand;
import rip.helium.command.commands.TeleportCommand;
import rip.helium.command.commands.ToggleCommand;
import rip.helium.command.commands.UnknownCommand;
import rip.helium.command.commands.VClipCommand;
import rip.helium.event.EventManager;
import rip.helium.event.EventTarget;
import rip.helium.event.events.impl.player.ChatMessageEvent;

public class CommandManager implements ClientSupport {
	
	private ArrayList<Command> commandList = new ArrayList<Command>();
	
	public ArrayList<Command> getCommandList() {
		return this.commandList;
	}
	
	private final UnknownCommand unknownCommand = new UnknownCommand();
	  
	public CommandManager() {
		EventManager.register(this);
		
		commandList.add(new FriendCommand());
		commandList.add(new ToggleCommand());
		commandList.add(new BindCommand());
		commandList.add(new VClipCommand());
		commandList.add(new TeleportCommand());
	}
	  
	public Command getCommandFromMessage(String message) {
	    for (Command command: this.commandList) {
	    	if (command.getNames() == null)
	    		return (Command)new UnknownCommand(); 
	    	String[] names;
	    	for (int length = (names = command.getNames()).length, i = 0; i < length; i++) {
	    		String name = names[i];
	    		if (message.split(" ")[0].equalsIgnoreCase(name))
	    			return command; 
	    	} 
	    } 
	    return (Command)unknownCommand;
	}
	
	
	
}
