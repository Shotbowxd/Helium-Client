package rip.helium.gui.click;

import java.io.IOException;
import java.util.ArrayList;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;
import rip.helium.ClientSupport;
import rip.helium.gui.click.component.Component;
import rip.helium.gui.click.component.Frame;
import rip.helium.gui.click.component.components.sub.Console;
import rip.helium.gui.click.component.components.sub.FrameParentCheckbox;
import rip.helium.gui.click.component.components.sub.FrameParentModeButton;
import rip.helium.gui.click.component.components.sub.FrameParentSlider;
import rip.helium.module.Module.Category;
import rip.helium.module.modules.combat.Targeting;
import rip.helium.module.modules.render.ClickGUI;
import rip.helium.module.modules.render.Colors;

public class ClickGUIScreen extends GuiScreen implements ClientSupport {

	public static ArrayList<Frame> frames;
	public static int color = 0xffe53935;
	
	private Minecraft mc;
	
	public ClickGUIScreen() {
		this.mc = Minecraft.getMinecraft();
		this.frames = new ArrayList<Frame>();
		int frameY = 4;
		
		for(Category category : Category.values()) {
			Frame frame = new Frame(category);
			frame.setX(4);
			frame.setY(frameY);
			frames.add(frame);
			frameY += frame.getBarHeight() + 4; 
		}

		/*/Frame consoleFrame = new Frame("Console");
		consoleFrame.setX(136);
		consoleFrame.setY(4);
		consoleFrame.setWidth(450);
		frames.add(consoleFrame);
		consoleFrame.components.add(new Console(consoleFrame));/*/
		
		Frame targetingFrame = new Frame("Targeting");
		targetingFrame.setX(136);
		targetingFrame.setY(20);
		//frames.add(targetingFrame);
		targetingFrame.components.add(new FrameParentCheckbox(((Targeting)mc.hackedClient.getModuleManager().getModule("Targeting")).players, targetingFrame, targetingFrame.getBarHeight() + 1));
		targetingFrame.components.add(new FrameParentCheckbox(((Targeting)mc.hackedClient.getModuleManager().getModule("Targeting")).monsters, targetingFrame, targetingFrame.getBarHeight() + 13));
		targetingFrame.components.add(new FrameParentCheckbox(((Targeting)mc.hackedClient.getModuleManager().getModule("Targeting")).animals, targetingFrame, targetingFrame.getBarHeight() + 25));
		targetingFrame.components.add(new FrameParentCheckbox(((Targeting)mc.hackedClient.getModuleManager().getModule("Targeting")).villagers, targetingFrame, targetingFrame.getBarHeight() + 37));
		targetingFrame.components.add(new FrameParentCheckbox(((Targeting)mc.hackedClient.getModuleManager().getModule("Targeting")).golems, targetingFrame, targetingFrame.getBarHeight() + 49));
		
		Frame uiFrame = new Frame("UI");
		uiFrame.setX(136);
		uiFrame.setY(36);
		frames.add(uiFrame);
		uiFrame.components.add(new FrameParentSlider(((Colors)mc.hackedClient.getModuleManager().getModule("Colors")).hudR, uiFrame, targetingFrame.getBarHeight() + 1));
		uiFrame.components.add(new FrameParentSlider(((Colors)mc.hackedClient.getModuleManager().getModule("Colors")).hudG, uiFrame, targetingFrame.getBarHeight() + 13));
		uiFrame.components.add(new FrameParentSlider(((Colors)mc.hackedClient.getModuleManager().getModule("Colors")).hudB, uiFrame, targetingFrame.getBarHeight() + 25));
		uiFrame.components.add(new FrameParentSlider(((Colors)mc.hackedClient.getModuleManager().getModule("Colors")).clickR, uiFrame, targetingFrame.getBarHeight() + 37));
		uiFrame.components.add(new FrameParentSlider(((Colors)mc.hackedClient.getModuleManager().getModule("Colors")).clickG, uiFrame, targetingFrame.getBarHeight() + 49));
		uiFrame.components.add(new FrameParentSlider(((Colors)mc.hackedClient.getModuleManager().getModule("Colors")).clickB, uiFrame, targetingFrame.getBarHeight() + 61));
		uiFrame.components.add(new FrameParentModeButton(((ClickGUI)mc.hackedClient.getModuleManager().getModule("ClickGUI")).mode, uiFrame, ((ClickGUI)mc.hackedClient.getModuleManager().getModule("ClickGUI")), targetingFrame.getBarHeight() + 73));
	}
	
	@Override
	public void initGui() {
		/*if (OpenGlHelper.shadersSupported && mc.getRenderViewEntity() instanceof net.minecraft.entity.player.EntityPlayer) {
			if (mc.entityRenderer.theShaderGroup != null)
				mc.entityRenderer.theShaderGroup.deleteShaderGroup(); 
		    mc.entityRenderer.loadShader(new ResourceLocation("shaders/post/blur.json"));
		} */
	}
	
	@Override
	public void onGuiClosed() {
		/*if (mc.entityRenderer.theShaderGroup != null) {
			mc.entityRenderer.theShaderGroup.deleteShaderGroup();
			mc.entityRenderer.theShaderGroup = null;
		} */
	}
	
	@Override
	public void drawScreen(int mouseX, int mouseY, float partialTicks) {
		this.drawDefaultBackground();
		for(Frame frame : frames) {
			frame.renderFrame(clientFont);
			frame.updatePosition(mouseX, mouseY);
			for(Component comp : frame.getComponents()) {
				comp.updateComponent(mouseX, mouseY);
			}
		}
	}
	
	@Override
    protected void mouseClicked(final int mouseX, final int mouseY, final int mouseButton) throws IOException {
		for(Frame frame : frames) {
			if(frame.isWithinHeader(mouseX, mouseY) && mouseButton == 0) {
				frame.setDrag(true);
				frame.dragX = mouseX - frame.getX();
				frame.dragY = mouseY - frame.getY();
			}
			if(frame.isWithinHeader(mouseX, mouseY) && mouseButton == 1) {
				frame.setOpen(!frame.isOpen());
			}
			if(frame.isOpen()) {
				if(!frame.getComponents().isEmpty()) {
					for(Component component : frame.getComponents()) {
						component.mouseClicked(mouseX, mouseY, mouseButton);
					}
				}
			}
		}
	}
	
	@Override
	protected void keyTyped(char typedChar, int keyCode) {
		for(Frame frame : frames) {
			if(frame.isOpen() && keyCode != 1) {
				if(!frame.getComponents().isEmpty()) {
					for(Component component : frame.getComponents()) {
						component.keyTyped(typedChar, keyCode);
					}
				}
			}
		}
		if (keyCode == 1) {
            this.mc.displayGuiScreen(null);
        }
	}

	
	@Override
    protected void mouseReleased(int mouseX, int mouseY, int state) {
		for(Frame frame : frames) {
			frame.setDrag(false);
		}
		for(Frame frame : frames) {
			if(frame.isOpen()) {
				if(!frame.getComponents().isEmpty()) {
					for(Component component : frame.getComponents()) {
						component.mouseReleased(mouseX, mouseY, state);
					}
				}
			}
		}
	}
	
	@Override
	public boolean doesGuiPauseGame() {
		return true;
	}
}
