package rip.helium.gui.click.component.components;

import java.awt.*;
import java.util.ArrayList;

import org.lwjgl.opengl.GL11;

import rip.helium.gui.click.component.Component;
import rip.helium.gui.click.component.Frame;
import rip.helium.gui.click.component.components.sub.Checkbox;
import rip.helium.gui.click.component.components.sub.Keybind;
import rip.helium.gui.click.component.components.sub.ModeButton;
import rip.helium.gui.click.component.components.sub.Slider;
import rip.helium.gui.click.component.components.sub.VisibleButton;
import rip.helium.module.Module;
import rip.helium.module.modules.render.ClickGUI;
import rip.helium.module.modules.render.Colors;
import rip.helium.setting.Setting;
import rip.helium.utils.render.Render2DUtils;

public class Button extends Component {

	public Module mod;
	public Frame parent;
	public int offset;
	private boolean isHovered;
	private ArrayList<Component> subcomponents;
	public boolean open;
	private int height;
	
	public Button(Module mod2, Frame parent, int offset) {
		this.mod = mod2;
		this.parent = parent;
		this.offset = offset;
		this.subcomponents = new ArrayList<Component>();
		this.open = false;
		height = 12;
		int opY = offset + 12;
		if(mc.hackedClient.getSettingManager().getSettingsByMod(mod2) != null) {
			for(Setting s : mc.hackedClient.getSettingManager().getSettingsByMod(mod2)){
				if(s.isCombo()){
					this.subcomponents.add(new ModeButton(s, this, mod2, opY));
					opY += 12;
				}
				if(s.isSlider()){
					this.subcomponents.add(new Slider(s, this, opY));
					opY += 12;
				}
				if(s.isCheck()){
					this.subcomponents.add(new Checkbox(s, this, opY));
					opY += 12;
				}
			}
		}
		this.subcomponents.add(new Keybind(this, opY));
		this.subcomponents.add(new VisibleButton(this, mod2, opY));
	}
	
	@Override
	public void setOff(int newOff) {
		offset = newOff;
		int opY = offset + 12;
		for(Component comp : this.subcomponents) {
			comp.setOff(opY);
			opY += 12;
		}
	}
	
	@Override
	public void renderComponent() {
		switch(((ClickGUI)mc.hackedClient.getModuleManager().getModule("ClickGUI")).mode.getValString()) {
		case "Michael":
			Render2DUtils.drawBorderedRect(parent.getX(), this.parent.getY() + this.offset + 1, parent.getX() + parent.getWidth(), this.parent.getY() + 13 + this.offset, 1, 0x88333333, this.isHovered ? 0x88222222 : 0x88111111);
			break;
		case "Slick":
			Render2DUtils.drawRect(parent.getX(), this.parent.getY() + this.offset + 1, parent.getX() + parent.getWidth(), this.parent.getY() + 13 + this.offset, this.isHovered ? new Color(53,53,53).getRGB() : new Color(32, 32, 32).getRGB());
		}
		GL11.glPushMatrix();
		//GL11.glScalef(0.5f,0.5f, 0.5f);
		clientFont.drawStringWithShadow(this.mod.getDisplayName(), (parent.getX() + 2), (parent.getY() + offset + 3), this.mod.getState() ? new Color((int)((Colors)mc.hackedClient.getModuleManager().getModule("Colors")).clickR.getValDouble(), (int)((Colors)mc.hackedClient.getModuleManager().getModule("Colors")).clickG.getValDouble(), (int)((Colors)mc.hackedClient.getModuleManager().getModule("Colors")).clickB.getValDouble(), 255).getRGB() : -1);
		if(this.subcomponents.size() > 1)
			//clientFont.drawStringWithShadow(this.open ? "-" : "+", (parent.getX() + parent.getWidth() - 8), (parent.getY() + offset + 3), -1);
		GL11.glPopMatrix();
		if(this.open) {
			if(!this.subcomponents.isEmpty()) {
				for(Component comp : this.subcomponents) {
					comp.renderComponent();
				}
				//Gui.drawRect(parent.getX() + 2, parent.getY() + this.offset + 12, parent.getX() + 3, parent.getY() + this.offset + ((this.subcomponents.size() + 1) * 12), ClickGUIScreen.color);
			}
		}
	}
	
	@Override
	public int getHeight() {
		if(this.open) {
			return (12 * (this.subcomponents.size() + 1));
		}
		return 12;
	}
	
	@Override
	public void updateComponent(int mouseX, int mouseY) {
		this.isHovered = isMouseOnButton(mouseX, mouseY);
		if(!this.subcomponents.isEmpty()) {
			for(Component comp : this.subcomponents) {
				comp.updateComponent(mouseX, mouseY);
			}
		}
	}
	
	@Override
	public void mouseClicked(int mouseX, int mouseY, int button) {
		if(isMouseOnButton(mouseX, mouseY) && button == 0) {
			this.mod.toggle();
		}
		if(isMouseOnButton(mouseX, mouseY) && button == 1) {
			this.open = !this.open;
			this.parent.refresh();
		}
		for(Component comp : this.subcomponents) {
			comp.mouseClicked(mouseX, mouseY, button);
		}
	}
	
	@Override
	public void mouseReleased(int mouseX, int mouseY, int mouseButton) {
		for(Component comp : this.subcomponents) {
			comp.mouseReleased(mouseX, mouseY, mouseButton);
		}
	}
	
	@Override
	public void keyTyped(char typedChar, int key) {
		for(Component comp : this.subcomponents) {
			comp.keyTyped(typedChar, key);
		}
	}
	
	public boolean isMouseOnButton(int x, int y) {
		if(x > parent.getX() && x < parent.getX() + parent.getWidth() && y > this.parent.getY() + this.offset && y < this.parent.getY() + 12 + this.offset) {
			return true;
		}
		return false;
	}
}
