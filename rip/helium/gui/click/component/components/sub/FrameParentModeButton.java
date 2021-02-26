package rip.helium.gui.click.component.components.sub;

import org.lwjgl.opengl.GL11;

import rip.helium.gui.click.component.Component;
import rip.helium.gui.click.component.Frame;
import rip.helium.module.Module;
import rip.helium.module.modules.render.ClickGUI;
import rip.helium.setting.Setting;
import rip.helium.utils.render.Render2DUtils;

public class FrameParentModeButton extends Component {

	private boolean hovered;
	private Frame parent;
	private Setting set;
	private int offset;
	private int x;
	private int y;
	private Module mod;

	private int modeIndex;
	
	public FrameParentModeButton(Setting set, Frame parent, Module mod, int offset) {
		this.set = set;
		this.parent = parent;
		this.mod = mod;
		this.x = parent.getX() + parent.getWidth();
		this.y = parent.getY() + offset;
		this.offset = offset;
		this.modeIndex = 0;
	}
	
	@Override
	public void setOff(int newOff) {
		offset = newOff;
	}
	
	@Override
	public void renderComponent() {
		switch(((ClickGUI)mc.hackedClient.getModuleManager().getModule("ClickGUI")).mode.getValString()) {
		case "Michael":
			Render2DUtils.drawBorderedRect(parent.getX(), parent.getY() + 1 + offset, parent.getX() + (parent.getWidth() * 1), parent.getY() + offset + 13, 1, 0x88333333, this.hovered ? 0x88222222 : 0x88111111);
			break;
		case "Slick":
			Render2DUtils.drawRect(parent.getX(), parent.getY() + 1 + offset, parent.getX() + (parent.getWidth() * 1), parent.getY() + offset + 13, this.hovered ? 0x88222222 : 0x88111111);
			break;
		}
		//Gui.drawRect(parent.getX(), parent.getY() + offset, parent.getX() + 2, parent.getY() + offset + 12, 0xFF111111);
		GL11.glPushMatrix();
		//GL11.glScalef(0.5f,0.5f, 0.5f);
		clientFont.drawStringWithShadow(this.set.getName() + ": " + set.getValString(), (parent.getX() + 2), (parent.getY() + offset + 2), -1);
		GL11.glPopMatrix();
	}
	
	@Override
	public void updateComponent(int mouseX, int mouseY) {
		this.hovered = isMouseOnButton(mouseX, mouseY);
		this.y = parent.getY() + offset;
		this.x = parent.getX();
	}
	
	@Override
	public void mouseClicked(int mouseX, int mouseY, int button) {
		if(isMouseOnButton(mouseX, mouseY) && button == 0 && this.parent.isOpen()) {
			int maxIndex = set.getOptions().size();

			if(modeIndex + 1 > maxIndex)
				modeIndex = 1;
			else
				modeIndex++;

			set.setValString(set.getOptions().get(modeIndex - 1));
		}
	}
	
	public boolean isMouseOnButton(int x, int y) {
		if(x > this.x && x < this.x + 128 && y > this.y && y < this.y + 12) {
			return true;
		}
		return false;
	}
}