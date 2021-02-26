package rip.helium;

import java.awt.Font;

import net.minecraft.client.Minecraft;
import net.minecraft.util.ResourceLocation;
import rip.helium.utils.render.font.MinecraftFontRenderer;

public interface ClientSupport {
	
	Minecraft mc = Minecraft.getMinecraft();
	
	MinecraftFontRenderer clientFont = new MinecraftFontRenderer(fontFromTTF(new ResourceLocation("dotexe/notosansregular.ttf"), 18, Font.PLAIN), true, false);
	MinecraftFontRenderer clientFontBig = new MinecraftFontRenderer(fontFromTTF(new ResourceLocation("dotexe/notosansregular.ttf"), 28, Font.PLAIN), true, false);

	MinecraftFontRenderer verdana = new MinecraftFontRenderer(fontFromTTF(new ResourceLocation("dotexe/Verdana.ttf"), 16, Font.PLAIN), true, false);
    MinecraftFontRenderer tahoma = new MinecraftFontRenderer(fontFromTTF(new ResourceLocation("dotexe/Tahoma.ttf"), 16, Font.PLAIN), true, false);

    MinecraftFontRenderer sexFont = new MinecraftFontRenderer(fontFromTTF(new ResourceLocation("dotexe/font.ttf"), 16, Font.PLAIN), true, false);

    static Font fontFromTTF(ResourceLocation fontLocation, float fontSize, int fontType) {
        Font output = null;
        try {
            output = Font.createFont(fontType, Minecraft.getMinecraft().getResourceManager().getResource(fontLocation).getInputStream());
            output = output.deriveFont(fontSize);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return output;
    }
	
}
