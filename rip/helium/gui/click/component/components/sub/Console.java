package rip.helium.gui.click.component.components.sub;

import java.util.ArrayList;

import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.GL11;

import rip.helium.command.Command;
import rip.helium.command.CommandManager;
import rip.helium.gui.click.component.Component;
import rip.helium.gui.click.component.Frame;
import rip.helium.utils.render.Render2DUtils;

public class Console extends Component {

	public static ArrayList<String> lines = new ArrayList<>();
	String command = "";
	private boolean hovered;
	private Frame parent;
	private int x;
	private int y;
	
	public Console(Frame parent) {
		this.parent = parent;
		this.x = parent.getX() + parent.getWidth();
		this.y = parent.getY();
	}
	
	@Override
	public void renderComponent() {
		Render2DUtils.drawBorderedRect(parent.getX(), parent.getY() + parent.getBarHeight() + 1, parent.getX() + (parent.getWidth() * 1), parent.getY() + parent.getBarHeight() + 250, 1, 0x88333333, this.hovered ? 0x88222222 : 0x88111111);
		if (lines.size() > 23)
			lines.remove(0);
		int lineY = parent.getY() + parent.getBarHeight() + 2;
		for (String s: lines) {
			clientFont.drawStringWithShadow(s, parent.getX() + 2, lineY, -1);
		    lineY += 10;
		}
		clientFont.drawStringWithShadow("> " + this.command + "_", parent.getX() + 2, + parent.getY() + 250, 0xFFFFFFFF);
		//Gui.drawRect(parent.parent.getX(), parent.parent.getY() + offset, parent.parent.getX() + 2, parent.parent.getY() + offset + 12, 0xFF111111);
		GL11.glPushMatrix();
		//GL11.glScalef(0.5f,0.5f, 0.5f);
		//clientFont.drawStringWithShadow(binding ? "Press a key..." : ("Key: " + Keyboard.getKeyName(this.parent.mod.getBind())), (parent.parent.getX() + 2), (parent.parent.getY() + offset + 2), -1);
		GL11.glPopMatrix();
	}
	
	@Override
	public void updateComponent(int mouseX, int mouseY) {
		this.hovered = isMouseOnButton(mouseX, mouseY);
		this.y = parent.getY();
		this.x = parent.getX();
	}
	
	@Override
	public void mouseClicked(int mouseX, int mouseY, int button) {
		
	}
	
	@Override
	public void keyTyped(char typedChar, int key) {
		if(this.hovered) {
			if(key == Keyboard.KEY_RETURN) {
				String[] args = this.command.split(" ");
			    Command commandFromMessage = mc.hackedClient.getCommandManager().getCommandFromMessage(this.command);
			    commandFromMessage.runCommand(args);
				this.command = "";
			} else if(key == Keyboard.KEY_BACK) {
				if (this.command.length() > 0)
			        this.command = this.command.substring(0, this.command.length() - 1); 
			} else if (key != 41 && key != 15 && key != 56 && key != 184 && key != 29 && key != 157 && key != 42 && key != 54) {
			      this.command = String.valueOf(this.command) + typedChar;
		    } 
		}
	}
	
	public boolean isMouseOnButton(int x, int y) {
		if(x > this.x && x < this.x + parent.getWidth() && y > (this.y + parent.getBarHeight()) && y < this.y + 250) {
			return true;
		}
		return false;
	}
}