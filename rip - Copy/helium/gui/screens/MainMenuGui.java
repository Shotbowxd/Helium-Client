package rip.helium.gui.screens;

import java.awt.Color;
import java.io.IOException;

import org.lwjgl.opengl.GL11;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiMultiplayer;
import net.minecraft.client.gui.GuiOptions;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.GuiSelectWorld;
import net.minecraft.client.gui.GuiYesNoCallback;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.resources.I18n;
import net.minecraft.util.ResourceLocation;
import rip.helium.ClientSupport;
import rip.helium.gui.components.BigUIButton;
import rip.helium.gui.components.SmallUIButton;
import rip.helium.utils.render.Render2DUtils;

public class MainMenuGui extends GuiScreen implements GuiYesNoCallback, ClientSupport {

    private final ResourceLocation logoPath;
    private int topButtonHeight;

    public MainMenuGui() {
        this.logoPath = new ResourceLocation("dotexe/title.png");
    }


    @Override
    public void initGui() {
        //clientFont.drawStringWithShadow("User Status: ", (float)(sr.getScaledWidth() - clientFont.getStringWidth("User Status: Free") - 4), (float)(sr.getScaledHeight() - 9), new Color(180, 180, 180).getRGB());
        final int j = height / 4 + 48;

        this.buttonList.add(new BigUIButton(1, width / 2 - 110, j, I18n.format("menu.singleplayer")));
        this.buttonList.add(new BigUIButton(2, width / 2 - 110, j + 40, I18n.format("menu.multiplayer")));
        this.buttonList.add(new BigUIButton(999, width / 2 - 110, j + 80, I18n.format("Alt Login")));
        this.buttonList.add(new SmallUIButton(0, width / 2 - 110, j + 120, I18n.format("Options")));
        this.buttonList.add(new SmallUIButton(4, width / 2 + 10, j + 120, I18n.format("Quit")));
        Minecraft.getMinecraft().func_181537_a(false);
    }

    @Override
    protected void actionPerformed(final GuiButton button) throws IOException {
        if (button.id == 999) {
        	Minecraft.getMinecraft().displayGuiScreen(new DirectLoginGui(this));
            //clip.stop();
        }
        if (button.id == 0) {
            Minecraft.getMinecraft().displayGuiScreen(new GuiOptions(this, Minecraft.getMinecraft().gameSettings));
            //clip.stop();
        }
        if (button.id == 1) {
        	Minecraft.getMinecraft().displayGuiScreen(new GuiSelectWorld(this));
            //clip.stop();
        }
        if (button.id == 2) {
        	Minecraft.getMinecraft().displayGuiScreen(new GuiMultiplayer(this));
            //clip.stop();
        }
        if (button.id == 4) {
        	Minecraft.getMinecraft().shutdown();
        }
    }

    @Override
    public void drawScreen(final int mouseX, final int mouseY, final float partialTicks) {


        GlStateManager.pushMatrix();
        GlStateManager.disableAlpha();
        GlStateManager.enableAlpha();
        GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);
        final ScaledResolution sr = new ScaledResolution(Minecraft.getMinecraft());

        Render2DUtils.drawImg(new ResourceLocation("dotexe/title.jpg"), 0.0, 0.0, width, height);
        
        GlStateManager.popMatrix();
        final int logoPositionY = this.topButtonHeight - 30;
        super.drawScreen(mouseX, mouseY, partialTicks);
        int y = 0;
        int color = 0;
        GL11.glPushMatrix();
        GL11.glScaled(5, 5, 5);
        color = 0xffe53935;

        drawCenteredString(Minecraft.getMinecraft().fontRendererObj, Minecraft.getMinecraft().hackedClient.getName(), width / 10, height / 22, color);

        GL11.glPopMatrix();
    }


    @Override
    protected void keyTyped(final char typedChar, final int keyCode) throws IOException {
        if (keyCode == 1) {
            return;
        }
        super.keyTyped(typedChar, keyCode);
    }
}
