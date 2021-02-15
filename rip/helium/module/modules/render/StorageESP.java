package rip.helium.module.modules.render;

import org.lwjgl.opengl.GL11;

import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityChest;
import net.minecraft.tileentity.TileEntityDispenser;
import net.minecraft.tileentity.TileEntityEnderChest;
import net.minecraft.tileentity.TileEntityFurnace;
import net.minecraft.util.AxisAlignedBB;
import rip.helium.event.EventTarget;
import rip.helium.event.events.impl.render.Render3DEvent;
import rip.helium.module.Module;
import rip.helium.setting.Setting;
import rip.helium.utils.render.ColorUtils;
import rip.helium.utils.render.Render3DUtils;

public class StorageESP extends Module {

	private Setting chest;
	private Setting dispenser;
	private Setting enderChest;
	private Setting furnace;
	
	public StorageESP(int bind, String name, String displayName, Category category) {
		super(bind, name, displayName, category);
		this.setColor(ColorUtils.generateColor());
		this.setHidden(true);
		
		this.chest = new Setting("Chest", this, true);
		this.dispenser = new Setting("Dispenser", this, false);
		this.enderChest = new Setting("Ender Chest", this, false);
		this.furnace = new Setting("Furnace", this, true);
		
		mc.hackedClient.getSettingManager().addSetting(this.chest);
		mc.hackedClient.getSettingManager().addSetting(this.dispenser);
		mc.hackedClient.getSettingManager().addSetting(this.enderChest);
		mc.hackedClient.getSettingManager().addSetting(this.furnace);
	}
	
	@EventTarget
	public void onRender3D(Render3DEvent event) {
		GlStateManager.pushMatrix();
	    for (Object o : (mc.theWorld).loadedTileEntityList) {
	    	TileEntity ent = (TileEntity)o;
	    	if ((ent instanceof TileEntityChest || ent instanceof TileEntityDispenser || ent instanceof TileEntityEnderChest || ent instanceof TileEntityFurnace) && (
	        !(ent instanceof TileEntityChest) || this.chest.getValBoolean()) && (
	        !(ent instanceof TileEntityDispenser) || this.dispenser.getValBoolean()) && (
	        !(ent instanceof TileEntityEnderChest) || this.enderChest.getValBoolean()) && (
	        !(ent instanceof TileEntityFurnace) || this.furnace.getValBoolean())) {
	    		GL11.glPushMatrix();
	    		drawEsp(ent);
	    		GL11.glPopMatrix();
	    	} 
	    }
	    
	    GlStateManager.popMatrix();
	}
	
	private void drawEsp(TileEntity ent) {
		double x1 = ent.getPos().getX() - RenderManager.renderPosX;
		double y1 = ent.getPos().getY() - RenderManager.renderPosY;
		double z1 = ent.getPos().getZ() - RenderManager.renderPosZ;
		float[] color = getColor(ent);
		AxisAlignedBB box = new AxisAlignedBB(x1, y1, z1, x1 + 1.0D, y1 + 1.0D, z1 + 1.0D);
		if (ent instanceof TileEntityChest) {
			TileEntityChest chest = TileEntityChest.class.cast(ent);
		    if (chest.adjacentChestZPos != null) {
		    	box = new AxisAlignedBB(x1 + 0.0625D, y1, z1 + 0.0625D, x1 + 0.9375D, y1 + 0.875D, z1 + 1.9375D);
		    } else if (chest.adjacentChestXPos != null) {
		        box = new AxisAlignedBB(x1 + 0.0625D, y1, z1 + 0.0625D, x1 + 1.9375D, y1 + 0.875D, z1 + 0.9375D);
		    } else {
		    	if (chest.adjacentChestZPos != null || chest.adjacentChestXPos != null || chest.adjacentChestZNeg != null || chest.adjacentChestXNeg != null)
		    		return; 
		        box = new AxisAlignedBB(x1 + 0.0625D, y1, z1 + 0.0625D, x1 + 0.9375D, y1 + 0.875D, z1 + 0.9375D);
		    }
		} else if (ent instanceof TileEntityEnderChest) {
		    box = new AxisAlignedBB(x1 + 0.0625D, y1, z1 + 0.0625D, x1 + 0.9375D, y1 + 0.875D, z1 + 0.9375D);
		} 
		Render3DUtils.boundingBox(box, color[0], color[1], color[2], 255f);
	}
	
	private float[] getColor(TileEntity ent) {
		if (ent instanceof TileEntityChest)
			return new float[] { 0.0F, 0.5F, 0.5F }; 
		if (ent instanceof TileEntityDispenser)
		    return new float[] { 0.5F, 0.5F, 0.5F }; 
		if (ent instanceof TileEntityEnderChest)
		    return new float[] { 0.3F, 0.0F, 0.3F }; 
		if (ent instanceof TileEntityFurnace)
			return new float[] { 0.5F, 0.0F, 0.0F };
		return new float[] { 1.0F, 1.0F, 1.0F };
	}

}
