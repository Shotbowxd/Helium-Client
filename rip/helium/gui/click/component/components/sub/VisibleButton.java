package rip.helium.gui.click.component.components.sub;

import org.lwjgl.opengl.GL11;

import rip.helium.gui.click.component.Component;
import rip.helium.gui.click.component.components.Button;
import rip.helium.module.Module;
import rip.helium.utils.render.Render2DUtils;

public class VisibleButton extends Component { // Remove this class if you don't want it (it's kinda useless)

	private boolean hovered;
	private Button parent;
	private int offset;
	private int x;
	private int y;
	private Module mod;
	
	public VisibleButton(Button button, Module mod, int offset) {
		this.parent = button;
		this.mod = mod;
		this.x = button.parent.getX() + button.parent.getWidth();
		this.y = button.parent.getY() + button.offset;
		this.offset = offset;
	}
	
	@Override
	public void setOff(int newOff) {
		offset = newOff;
	}
	
	@Override
	public void renderComponent() {
		Render2DUtils.drawBorderedRect(parent.parent.getX(), parent.parent.getY() + 1 + offset, parent.parent.getX() + (parent.parent.getWidth() * 1), parent.parent.getY() + offset + 13, 1, 0x88333333, this.hovered ? 0x88222222 : 0x88111111);
		//Gui.drawRect(parent.parent.getX(), parent.parent.getY() + offset, parent.parent.getX() + 2, parent.parent.getY() + offset + 12, 0xFF111111);
		GL11.glPushMatrix();
		//GL11.glScalef(0.5f,0.5f, 0.5f);
		clientFont.drawStringWithShadow("Visible: " + !mod.isHidden(), (parent.parent.getX() + 2), (parent.parent.getY() + offset + 2), -1);
		GL11.glPopMatrix(); //													    mod.visible is a public boolean variable in the Module.java class. If it's == false, the mod won't show up in the ArrayList
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
			mod.setHidden(!mod.isHidden());
		}
	}
	
	public boolean isMouseOnButton(int x, int y) {
		if(x > this.x && x < this.x + 128 && y > this.y && y < this.y + 12) {
			return true;
		}
		return false;
	}
}
