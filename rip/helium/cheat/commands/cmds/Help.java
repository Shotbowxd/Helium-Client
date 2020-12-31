package rip.helium.cheat.commands.cmds;

import rip.helium.ChatUtil;
import rip.helium.Helium;
import rip.helium.cheat.commands.Command;

public class Help extends Command {

    public Help() {
        super("help", "?");
    }

    @Override
    public void run(String[] args) {
        ChatUtil.chat("§7§m----------------------------------------");
        ChatUtil.chat("§c§lHelium §7- §f" + Helium.client_build);
        ChatUtil.chat("§7§oDeveloped by Kansio & Pulse!");
        ChatUtil.chat(" ");
        ChatUtil.chat("§c§lCommands:");
        ChatUtil.chat("-friend");
        ChatUtil.chat("-vclip");
        ChatUtil.chat("-tp");
        ChatUtil.chat("-bind\n-toggle\n-focus");
        ChatUtil.chat("§7§m----------------------------------------");
    }
}
