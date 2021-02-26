package rip.helium.gui.click.component;

import java.awt.Color;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import org.lwjgl.opengl.GL11;

import rip.helium.ClientSupport;
import rip.helium.gui.click.component.components.Button;
import rip.helium.module.Module;
import rip.helium.module.Module.Category;
import rip.helium.module.modules.render.ClickGUI;
import rip.helium.module.modules.render.Colors;
import rip.helium.utils.render.Render2DUtils;
import rip.helium.utils.render.font.MinecraftFontRenderer;

public class Frame implements ClientSupport {

	public ArrayList<Component> components;
	private boolean open;
	private int width;
	private int y;
	private int x;
	private int barHeight;
	private boolean isDragging;
	private String name;
	public int dragX;
	public int dragY;
	public int mouseX;
	public int mouseY;
	
	public Frame(Category cat) {
		this.components = new ArrayList<Component>();
		this.name = cat.name();
		this.width = 128;
		this.x = 5;
		this.y = 5;
		this.barHeight = 12;
		this.dragX = 0;
		this.open = false;
		this.isDragging = false;
		int tY = this.barHeight;
		
		
		/**
		 * 		public ArrayList<Module> getModulesInCategory(Category categoryIn){
		 * 			ArrayList<Module> mods = new ArrayList<Module>();
		 * 			for(Module m : this.modules){
		 * 				if(m.getCategory() == categoryIn)
		 * 					mods.add(m);
		 * 			}
		 * 			return mods;
		 * 		}
		 */
		
		ArrayList<Module> list = new ArrayList<Module>();
		
		for(Module mod : mc.hackedClient.getModuleManager().getModules()) {
			if(!mod.getName().equalsIgnoreCase("ClickGUI") && !mod.getName().equalsIgnoreCase("Targeting") && !mod.getName().equalsIgnoreCase("Colors")) {
				list.add(mod);
			}
			
		}
		
		Collections.sort(list, new Comparator<Module>() {
            @Override
            public int compare(Module m1, Module m2) {
                String s1 = m1.getDisplayName();
                String s2 = m2.getDisplayName();
                return s1.compareToIgnoreCase(s2);
            }

        });
		
		for(Module mod: list) {
			if(mod.getCategory() == cat) {
				Button modButton = new Button(mod, this, tY);
				this.components.add(modButton);
				tY += 12;
			}
		}
	}
	
	public Frame(String str) {
		this.components = new ArrayList<Component>();
		this.width = 128;
		this.x = 5;
		this.y = 5;
		this.barHeight = 12;
		this.dragX = 0;
		this.open = false;
		this.isDragging = false;
		int tY = this.barHeight;
		this.name = str;
		
		
		/**
		 * 		public ArrayList<Module> getModulesInCategory(Category categoryIn){
		 * 			ArrayList<Module> mods = new ArrayList<Module>();
		 * 			for(Module m : this.modules){
		 * 				if(m.getCategory() == categoryIn)
		 * 					mods.add(m);
		 * 			}
		 * 			return mods;
		 * 		}
		 */
	}
	
	public ArrayList<Component> getComponents() {
		return components;
	}
	
	public void setX(int newX) {
		this.x = newX;
	}
	
	public void setY(int newY) {
		this.y = newY;
	}
	
	public void setDrag(boolean drag) {
		this.isDragging = drag;
	}
	
	public boolean isOpen() {
		return open;
	}
	
	public void setOpen(boolean open) {
		this.open = open;
	}
	
	public void renderFrame(MinecraftFontRenderer fontRenderer) {
		int tx = this.x;
		int ty = this.y;
		switch(((ClickGUI)mc.hackedClient.getModuleManager().getModule("ClickGUI")).mode.getValString()) {
		case "Michael":
			Render2DUtils.drawBorderedRect(this.x, this.y, this.x + this.width, this.y + this.barHeight, 1, new Color((int)((Colors)mc.hackedClient.getModuleManager().getModule("Colors")).clickR.getValDouble(), (int)((Colors)mc.hackedClient.getModuleManager().getModule("Colors")).clickG.getValDouble(), (int)((Colors)mc.hackedClient.getModuleManager().getModule("Colors")).clickB.getValDouble(), 255).getRGB(), new Color((int)((Colors)mc.hackedClient.getModuleManager().getModule("Colors")).clickR.getValDouble(), (int)((Colors)mc.hackedClient.getModuleManager().getModule("Colors")).clickG.getValDouble(), (int)((Colors)mc.hackedClient.getModuleManager().getModule("Colors")).clickB.getValDouble(), 255).getRGB());
			break;
		case "Slick":
			Render2DUtils.drawRect(this.x, this.y, this.x + this.width, this.y + this.barHeight + 1, new Color((int)((Colors)mc.hackedClient.getModuleManager().getModule("Colors")).clickR.getValDouble(), (int)((Colors)mc.hackedClient.getModuleManager().getModule("Colors")).clickG.getValDouble(), (int)((Colors)mc.hackedClient.getModuleManager().getModule("Colors")).clickB.getValDouble(), 255).getRGB());
			break;
		}
		GL11.glPushMatrix();
		//GL11.glScalef(0.5f,0.5f, 0.5f);
		fontRenderer.drawStringWithShadow(this.name, (this.x + 2), (this.y + 2.5f), 0xFFFFFFFF);
		//fontRenderer.drawStringWithShadow(this.open ? "-" : "+", (this.x + this.width - 10) + 2, (this.y + 2.5f), -1);
		GL11.glPopMatrix();
		if(this.open) {
			if(!this.components.isEmpty()) {
				//Gui.drawRect(this.x, this.y + this.barHeight, this.x + 1, this.y + this.barHeight + (12 * components.size()), new Color(0, 200, 20, 150).getRGB());
				//Gui.drawRect(this.x, this.y + this.barHeight + (12 * components.size()), this.x + this.width, this.y + this.barHeight + (12 * components.size()) + 1, new Color(0, 200, 20, 150).getRGB());
				//Gui.drawRect(this.x + this.width, this.y + this.barHeight, this.x + this.width - 1, this.y + this.barHeight + (12 * components.size()), new Color(0, 200, 20, 150).getRGB());
				for(Component component : components) {
					component.renderComponent();
					
				}
			}
		}
	}
	
	public void refresh() {
		int off = this.barHeight;
		for(Component comp : components) {
			comp.setOff(off);
			off += comp.getHeight();
		}
	}
	
	public int getX() {
		return x;
	}
	
	public int getY() {
		return y;
	}
	
	public int getWidth() {
		return width;
	}
	
	public void setWidth(int width) {
		this.width = width;
	}

	public int getBarHeight() {
		return this.barHeight;
	}
	
	public void updatePosition(int mouseX, int mouseY) {
		if(this.isDragging) {
			this.setX(mouseX - dragX);
			this.setY(mouseY - dragY);
			this.mouseX = mouseX;
			this.mouseY = mouseY;
		}
	}
	
	public boolean isWithinHeader(int x, int y) {
		if(x >= this.x && x <= this.x + this.width && y >= this.y && y <= this.y + this.barHeight) {
			return true;
		}
		return false;
	}
	
}
