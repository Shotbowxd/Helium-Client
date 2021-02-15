package rip.helium.utils.render.font;

import java.awt.Color;
import java.awt.Font;

import org.lwjgl.opengl.GL11;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.UnicodeFont;
import org.newdawn.slick.font.effects.ColorEffect;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.util.ResourceLocation;;

public class UnicodeFontRenderer extends FontRenderer {
	private final UnicodeFont font;

	@SuppressWarnings("unchecked")
	public UnicodeFontRenderer(Font awtFont) {
		super(Minecraft.getMinecraft().gameSettings, new ResourceLocation("textures/font/ascii.png"), Minecraft.getMinecraft().getTextureManager(), false);

		font = new UnicodeFont(awtFont);
		font.addAsciiGlyphs();
		font.getEffects().add(new ColorEffect(Color.WHITE));
		try {
			font.loadGlyphs();
		} catch(SlickException exception) {
			throw new RuntimeException(exception);
		}
		String alphabet = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ123456789";
		FONT_HEIGHT = font.getHeight(alphabet) / 2;
	}

	public void drawString(String string, float x, float y, int color) {
		if(string == null)
			return;
		// GL11.glClear(256);
		// GL11.glMatrixMode(GL_PROJECTION);
		// GL11.glLoadIdentity();
		// IntBuffer buffer = BufferUtils.createIntBuffer(16);
		// GL11.glGetInteger(GL_VIEWPORT, buffer);
		// GL11.glOrtho(0, buffer.get(2), buffer.get(3), 0, 1000, 3000);
		// GL11.glMatrixMode(GL_MODELVIEW);
		// GL11.glLoadIdentity();
		// GL11.glTranslatef(0, 0, -2000);
		GL11.glPushMatrix();
		GL11.glScaled(0.5, 0.5, 0.5);

		boolean blend = GL11.glIsEnabled(GL11.GL_BLEND);
		boolean lighting = GL11.glIsEnabled(GL11.GL_LIGHTING);
		boolean texture = GL11.glIsEnabled(GL11.GL_TEXTURE_2D);
		if(!blend)
			GL11.glEnable(GL11.GL_BLEND);
		if(lighting)
			GL11.glDisable(GL11.GL_LIGHTING);
		if(texture)
			GL11.glDisable(GL11.GL_TEXTURE_2D);
		x *= 2;
		y *= 2;
		// GL11.glBegin(GL_LINES);
		// GL11.glVertex3d(x, y, 0);
		// GL11.glVertex3d(x + getStringWidth(string), y + FONT_HEIGHT, 0);
		// GL11.glEnd();

		font.drawString(x, y, string, new org.newdawn.slick.Color(color));

		if(texture)
			GL11.glEnable(GL11.GL_TEXTURE_2D);
		if(lighting)
			GL11.glEnable(GL11.GL_LIGHTING);
		if(!blend)
			GL11.glDisable(GL11.GL_BLEND);
		GL11.glPopMatrix();
	}
	
	
	public int drawStringWithShadow(String s, float x, float y, int color) {
		this.drawString(s, x + 0.5f, y + 0.5f, 0xff000000);
		this.drawString(s, x, y, color);
		return 0;
	}

	@Override
	public int getCharWidth(char c) {
		return getStringWidth(Character.toString(c));
	}

	@Override
	public int getStringWidth(String string) {
		return font.getWidth(string) / 2;
	}

	public int getStringHeight(String string) {
		return font.getHeight(string) / 2;
	}
}