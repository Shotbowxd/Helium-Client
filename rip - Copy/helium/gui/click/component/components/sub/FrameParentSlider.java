package rip.helium.gui.click.component.components.sub;

import java.math.BigDecimal;
import java.math.RoundingMode;

import org.lwjgl.opengl.GL11;

import net.minecraft.client.gui.Gui;
import rip.helium.gui.click.component.Component;
import rip.helium.gui.click.component.Frame;
import rip.helium.module.modules.render.ClickGUI;
import rip.helium.setting.Setting;
import rip.helium.utils.render.Render2DUtils;

public class FrameParentSlider extends Component {

	private boolean hovered;

	private Setting set;
	private Frame parent;
	private int offset;
	private int x;
	private int y;
	private boolean dragging = false;

	private double renderWidth;
	
	public FrameParentSlider(Setting value, Frame parent, int offset) {
		this.set = value;
		this.parent = parent;
		this.x = parent.getX() + parent.getWidth();
		this.y = parent.getY() + offset;
		this.offset = offset;
	}
	
	@Override
	public void renderComponent() {
		switch(((ClickGUI)mc.hackedClient.getModuleManager().getModule("ClickGUI")).mode.getValString()) {
		case "Michael":
			Render2DUtils.drawBorderedRect(parent.getX(), parent.getY() + 1 + offset, parent.getX() + parent.getWidth(), parent.getY() + offset + 13, 1, 0x88333333, this.hovered ? 0x88222222 : 0x88111111);			
			break;
		case "Slick":
			Render2DUtils.drawRect(parent.getX(), parent.getY() + 1 + offset, parent.getX() + parent.getWidth(), parent.getY() + offset + 13, this.hovered ? 0x88222222 : 0x88111111);
			break;
		}
		 final int drag = (int)(this.set.getValDouble() / this.set.getMax() * this.parent.getWidth());
		Gui.drawRect(parent.getX(), parent.getY() + 1 + offset, parent.getX() + (int) renderWidth, parent.getY() + offset + 13, hovered ? 0x88555555 : 0x88444444);
		//Gui.drawRect(parent.getX(), parent.getY() + offset, parent.getX() + 2, parent.getY() + offset + 12, 0xFF111111);
		GL11.glPushMatrix();
		//GL11.glScalef(0.5f,0.5f, 0.5f);
		clientFont.drawStringWithShadow(this.set.getName() + ": " + this.set.getValDouble() , (parent.getX() + 2), (parent.getY() + offset + 2) , -1);
		
		GL11.glPopMatrix();
	}
	
	@Override
	public void setOff(int newOff) {
		offset = newOff;
	}
	
	@Override
	public void updateComponent(int mouseX, int mouseY) {
		this.hovered = isMouseOnButtonD(mouseX, mouseY) || isMouseOnButtonI(mouseX, mouseY);
		this.y = parent.getY() + offset;
		this.x = parent.getX();
		
		double diff = Math.min(this.parent.getWidth(), Math.max(0, mouseX - this.x));

		double min = set.getMin();
		double max = set.getMax();
		
		renderWidth = this.parent.getWidth() * (set.getValDouble() - min) / (max - min);
		
		if (dragging) {
			if (diff == 0) {
				set.setValDouble(set.getMin());
			}
			else {
				double newValue = roundToPlace(((diff / this.parent.getWidth()) * (max - min) + min), 2);
				set.setValDouble(newValue);
			}
		}
	}
	
	private static double roundToPlace(double value, int places) {
        if (places < 0) {
            throw new IllegalArgumentException();
        }
        BigDecimal bd = new BigDecimal(value);
        bd = bd.setScale(places, RoundingMode.HALF_UP);
        return bd.doubleValue();
    }
	
	@Override
	public void mouseClicked(int mouseX, int mouseY, int button) {
		if(isMouseOnButtonD(mouseX, mouseY) && button == 0 && this.parent.isOpen()) {
			dragging = true;
		}
		if(isMouseOnButtonI(mouseX, mouseY) && button == 0 && this.parent.isOpen()) {
			dragging = true;
		}
	}
	
	@Override
	public void mouseReleased(int mouseX, int mouseY, int mouseButton) {
		dragging = false;
	}
	
	public boolean isMouseOnButtonD(int x, int y) {
		if(x > this.x && x < this.x + (parent.getWidth() / 2 + 1) && y > this.y && y < this.y + 12) {
			return true;
		}
		return false;
	}
	
	public boolean isMouseOnButtonI(int x, int y) {
		if(x > this.x + parent.getWidth() / 2 && x < this.x + parent.getWidth() && y > this.y && y < this.y + 12) {
			return true;
		}
		return false;
	}
}
