package rip.helium.utils.client;

import net.minecraft.util.ChatComponentText;
import rip.helium.ClientSupport;
import rip.helium.gui.click.component.components.sub.Console;

public class ClientUtils implements ClientSupport {
	
	public static void addConsoleMessage(String s) {
		Console.lines.add("\247d[" + mc.hackedClient.getName() + "]\2477: \247r" + s);
	}
	
}
