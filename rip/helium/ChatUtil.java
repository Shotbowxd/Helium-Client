package rip.helium;

import net.minecraft.client.Minecraft;
import net.minecraft.util.ChatComponentText;

public class ChatUtil {


    public static void chat(String msg) {
        Minecraft.getMinecraft().thePlayer
                .addChatMessage(new ChatComponentText("§7> §f" + msg));
    }

    public static void chatNoPrefix(String msg) {
        Minecraft.getMinecraft().thePlayer
                .addChatMessage(new ChatComponentText("§7> §f" + msg));
    }

}
