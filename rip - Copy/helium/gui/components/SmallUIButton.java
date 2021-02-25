package rip.helium.gui.components;

import java.awt.Color;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiButton;

public class SmallUIButton extends GuiButton {
	
    public SmallUIButton(final int buttonId, final int x, final int y, final String buttonText) {
        this(buttonId, x, y, 100, 20, buttonText);

    }

    public SmallUIButton(final int buttonId, final int x, final int y, final int widthIn, final int heightIn, final String buttonText) {
        super(buttonId, x, y, widthIn, heightIn, buttonText);
    }

    @Override
    public void drawButton(Minecraft mc, int mouseX, int mouseY) {
        int y = 0;
        if (this.visible) {
        	this.hovered = mouseX >= this.xPosition && mouseY >= this.yPosition && mouseX < this.xPosition + this.width && mouseY < this.yPosition + this.height;
            final Color a = new Color(0, 0, 0, this.hovered ? 170 : 200);
            int i = 0;
            final FontRenderer var4 = mc.fontRendererObj;
            Gui.drawRect(this.xPosition, this.yPosition - 2, this.xPosition + this.width, this.yPosition + 2 + this.height, a.getRGB());
            this.drawCenteredString(var4, this.displayString, this.xPosition + this.width / 2, this.yPosition + (this.height - 8) / 2, new Color(255, 255, 255).getRGB());

            i += 12;
        }
    }
}