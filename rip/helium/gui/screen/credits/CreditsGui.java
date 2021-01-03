package rip.helium.gui.screen.credits;


import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.GuiYesNoCallback;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.input.Keyboard;
import rip.helium.buttons.UIButton;
import rip.helium.gui.screen.MainMenuGui;
import rip.helium.utils.Draw;
import rip.helium.utils.font.Fonts;

import java.awt.*;
import java.io.IOException;

public class CreditsGui extends GuiScreen implements GuiYesNoCallback
{
    private GuiScreen parentScreen;

    public CreditsGui(final GuiScreen parent) {
        this.parentScreen = parent;
    }

    @Override
    public void initGui() {
        super.initGui();
    }

    @Override
    protected void actionPerformed(final GuiButton button) throws IOException {
    }

    @Override
    public void drawScreen(final int mouseX, final int mouseY, final float partialTicks) {
        GlStateManager.pushMatrix();
        GlStateManager.disableAlpha();
        GlStateManager.enableAlpha();
        Gui.drawRect(0, 0, this.width, this.height, new Color(0, 0, 0, 150).getRGB());
        this.drawGradientRect(0, 0, this.width, this.height, new Color(0, 0, 0).getRGB(), new Color(0, 0, 0, 120).getRGB());
        this.drawGradientRect(0, 0, this.width, this.height, new Color(0, 0, 0, 0).getRGB(), new Color(0, 0, 0, 120).getRGB());
        GlStateManager.popMatrix();
        GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);
        Draw.drawImg(new ResourceLocation("client/Background.jpg"), 1.0, 1.0, this.width, this.height);
        final int logoPositionY = this.height / 2 - 130;
        Fonts.bf28.drawStringWithShadow("Credits: ", 15.0f, 10.0f, new Color(255, 64, 37).getRGB());
        Fonts.bf20.drawStringWithShadow("Client Base:", 15.0f, 30.0f, new Color(255, 64, 37).getRGB());
        Fonts.bf20.drawStringWithShadow("Vaziak, Spec, AnthonyJ", 15.0f, 40.0f, new Color(255, 64, 37).getRGB());
        Fonts.bf20.drawStringWithShadow("Viper and Ghostly Disabler:", 15.0f, 70.0f, new Color(255, 64, 37).getRGB());
        Fonts.bf20.drawStringWithShadow("Dort", 15.0f, 80.0f, new Color(255, 64, 37).getRGB());
        super.drawScreen(mouseX, mouseY, partialTicks);
    }

    @Override
    protected void keyTyped(final char typedChar, final int keyCode) throws IOException {
        if (keyCode == 1) {
            Minecraft.getMinecraft().displayGuiScreen(this.parentScreen);
        }
        super.keyTyped(typedChar, keyCode);
    }
}