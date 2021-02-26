package rip.helium.gui.click.component.components.sub;

import org.lwjgl.opengl.GL11;

import net.minecraft.client.gui.Gui;
import rip.helium.gui.click.component.Component;
import rip.helium.gui.click.component.Frame;
import rip.helium.gui.click.component.components.Button;
import rip.helium.module.modules.render.ClickGUI;
import rip.helium.setting.Setting;
import rip.helium.utils.render.Render2DUtils;

import java.awt.*;

public class Checkbox extends Component {

	private boolean hovered;
	private Setting op;
	private Button parent;
	private int offset;
	private int x;
	private int y;
	
	public Checkbox(Setting option, Button button, int offset) {
		this.op = option;
		this.parent = button;
		this.x = button.parent.getX() + button.parent.getWidth();
		this.y = button.parent.getY() + button.offset;
		this.offset = offset;
	}
	
	@Override
	public void renderComponent() {
		switch(((ClickGUI)mc.hackedClient.getModuleManager().getModule("ClickGUI")).mode.getValString()) {
		case "Michael":
			Render2DUtils.drawBorderedRect(parent.parent.getX(), parent.parent.getY() + 1 + offset, parent.parent.getX() + (parent.parent.getWidth() * 1), parent.parent.getY() + offset + 13, 1, 0x88333333, this.hovered ? 0x88222222 : 0x88111111);
			break;
		case "Slick":
			Render2DUtils.drawRect(parent.parent.getX(), parent.parent.getY() + 1 + offset, parent.parent.getX() + (parent.parent.getWidth() * 1), parent.parent.getY() + offset + 13, this.hovered ? new Color(53,53,53).getRGB() : new Color(32, 32, 32).getRGB());
			break;
		}
		//Gui.drawRect(parent.parent.getX(), parent.parent.getY() + offset, parent.parent.getX() + 2, parent.parent.getY() + offset + 12, 0xFF111111);
		GL11.glPushMatrix();
		//GL11.glScalef(0.5f,0.5f, 0.5f);
		clientFont.drawStringWithShadow(this.op.getName(), (parent.parent.getX() + 10 + 4), (parent.parent.getY() + offset + 2), -1);
		GL11.glPopMatrix();
		Gui.drawRect(parent.parent.getX() + 3, parent.parent.getY() + offset + 3, parent.parent.getX() + 9, parent.parent.getY() + offset + 9, 0xFF999999);
		if(this.op.getValBoolean())
			Gui.drawRect(parent.parent.getX() + 4, parent.parent.getY() + offset + 4, parent.parent.getX() + 8, parent.parent.getY() + offset + 8, 0xFF666666);
	}
	
	@Override
	public void setOff(int newOff) {
		offset = newOff;
	}
	
	@Override
	public void updateComponent(int mouseX, int mouseY) {
		this.hovered = isMouseOnButton(mouseX, mouseY);
		this.y = parent.parent.getY() + offset;
		this.x = parent.parent.getX();
	}
	
	@Override
	public void mouseClicked(int mouseX, int mouseY, int button) {
		if(isMouseOnButton(mouseX, mouseY) && button == 0 && this.parent.open) {
			this.op.setValBoolean(!op.getValBoolean());;
		}
	}
	
	public boolean isMouseOnButton(int x, int y) {
		if(x > this.x && x < this.x + 128 && y > this.y && y < this.y + 12) {
			return true;
		}
		return false;
	}
}
