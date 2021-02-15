package rip.helium;

import java.awt.Font;

import net.minecraft.client.Minecraft;
import net.minecraft.util.ResourceLocation;
import rip.helium.utils.render.font.MinecraftFontRenderer;

public interface ClientSupport {
	
	public Minecraft mc = Minecraft.getMinecraft();
	
	public MinecraftFontRenderer clientFont = new MinecraftFontRenderer(fontFromTTF(new ResourceLocation("dotexe/notosansregular.ttf"), 18, Font.PLAIN), true, false);
	public MinecraftFontRenderer clientFontBig = new MinecraftFontRenderer(fontFromTTF(new ResourceLocation("dotexe/notosansregular.ttf"), 28, Font.PLAIN), true, false);

    public static Font fontFromTTF(ResourceLocation fontLocation, float fontSize, int fontType) {
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
