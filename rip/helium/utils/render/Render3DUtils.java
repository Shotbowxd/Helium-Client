package rip.helium.utils.render;

import org.lwjgl.opengl.GL11;

import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.AxisAlignedBB;
import rip.helium.ClientSupport;

public class Render3DUtils implements ClientSupport {
	
	public static void enableGL3D(float lineWidth) {
		GL11.glDisable(3008);
		GL11.glEnable(3042);
		GL11.glBlendFunc(770, 771);
		GL11.glDisable(3553);
		GL11.glDisable(2929);
		GL11.glDepthMask(false);
		GL11.glEnable(2884);
		mc.entityRenderer.disableLightmap();
		GL11.glEnable(2848);
		GL11.glHint(3154, 4354);
		GL11.glHint(3155, 4354);
	}
		  
	public static void disableGL3D() {
		GL11.glEnable(3553);
		GL11.glEnable(2929);
		GL11.glDisable(3042);
		GL11.glEnable(3008);
		GL11.glDepthMask(true);
		GL11.glCullFace(1029);
		GL11.glDisable(2848);
		GL11.glHint(3154, 4352);
		GL11.glHint(3155, 4352);
	}
	
	public static void startDrawing() {
		GL11.glEnable(3042);
	    GL11.glEnable(3042);
	    GL11.glBlendFunc(770, 771);
	    GL11.glEnable(2848);
	    GL11.glDisable(3553);
	    GL11.glDisable(2929);
	    mc.entityRenderer.setupCameraTransform(mc.timer.renderPartialTicks, 0);
	}
	
	public static void stopDrawing() {
		GL11.glDisable(3042);
	    GL11.glEnable(3553);
	    GL11.glDisable(2848);
	    GL11.glDisable(3042);
	    GL11.glEnable(2929);
	}
	
	public static double getDiff(double lastI, double i, float ticks, double ownI) {
	    return lastI + (i - lastI) * ticks - ownI;
	}
	
	public static void drawEsp(EntityLivingBase ent, float pTicks, float red, float green, float blue, float alpha) {
	    if (!ent.isEntityAlive())
	    	return; 
	    double x = getDiff(ent.lastTickPosX, ent.posX, pTicks, RenderManager.renderPosX);
	    double y = getDiff(ent.lastTickPosY, ent.posY, pTicks, RenderManager.renderPosY);
	    double z = getDiff(ent.lastTickPosZ, ent.posZ, pTicks, RenderManager.renderPosZ);
	    boundingBox((Entity)ent, x, y, z, red, green, blue, alpha);
	}
	
	public static void boundingBox(AxisAlignedBB bb, float red, float green, float blue, float alpha) {
	    GlStateManager.pushMatrix();
	    GL11.glLineWidth(1.0F);
	    GlStateManager.disableDepth();
	    GlStateManager.disableLighting();
	    GlStateManager.disableFog();
	    GlStateManager.disableTexture2D();
	    GlStateManager.color(red, green, blue, alpha);
	    drawOutlinedBoundingBox(bb);
	    drawLines(bb);
	    GlStateManager.popMatrix();
	}
	
	public static void boundingBox(Entity entity, double x, double y, double z, float red, float green, float blue, float alpha) {
	    GlStateManager.pushMatrix();
	    GL11.glLineWidth(1.0F);
	    AxisAlignedBB var11 = entity.getEntityBoundingBox();
	    AxisAlignedBB var12 = new AxisAlignedBB(var11.minX - entity.posX + x, var11.minY - entity.posY + y, var11.minZ - entity.posZ + z, var11.maxX - entity.posX + x, var11.maxY - entity.posY + y, var11.maxZ - entity.posZ + z);
	    GlStateManager.disableDepth();
	    GlStateManager.disableLighting();
	    GlStateManager.disableFog();
	    GlStateManager.disableTexture2D();
	    GlStateManager.color(red, green, blue, alpha);
	    drawOutlinedBoundingBox(var12);
	    drawLines(var12);
	    GlStateManager.popMatrix();
	}
	
	public static void drawOutlinedBoundingBox(AxisAlignedBB p_147590_0_) {
		GL11.glEnable(2848);
	    GL11.glBegin(1);
	    GL11.glVertex3d(p_147590_0_.minX, p_147590_0_.minY, p_147590_0_.minZ);
	    GL11.glVertex3d(p_147590_0_.maxX, p_147590_0_.minY, p_147590_0_.minZ);
	    GL11.glVertex3d(p_147590_0_.maxX, p_147590_0_.minY, p_147590_0_.maxZ);
	    GL11.glVertex3d(p_147590_0_.minX, p_147590_0_.minY, p_147590_0_.maxZ);
	    GL11.glVertex3d(p_147590_0_.minX, p_147590_0_.minY, p_147590_0_.minZ);
	    GL11.glVertex3d(p_147590_0_.minX, p_147590_0_.maxY, p_147590_0_.minZ);
	    GL11.glVertex3d(p_147590_0_.maxX, p_147590_0_.maxY, p_147590_0_.minZ);
	    GL11.glVertex3d(p_147590_0_.maxX, p_147590_0_.maxY, p_147590_0_.maxZ);
	    GL11.glVertex3d(p_147590_0_.minX, p_147590_0_.maxY, p_147590_0_.maxZ);
	    GL11.glVertex3d(p_147590_0_.minX, p_147590_0_.maxY, p_147590_0_.minZ);
	    GL11.glVertex3d(p_147590_0_.minX, p_147590_0_.minY, p_147590_0_.minZ);
	    GL11.glVertex3d(p_147590_0_.minX, p_147590_0_.maxY, p_147590_0_.minZ);
	    GL11.glVertex3d(p_147590_0_.maxX, p_147590_0_.minY, p_147590_0_.minZ);
	    GL11.glVertex3d(p_147590_0_.maxX, p_147590_0_.maxY, p_147590_0_.minZ);
	    GL11.glVertex3d(p_147590_0_.maxX, p_147590_0_.minY, p_147590_0_.maxZ);
	    GL11.glVertex3d(p_147590_0_.maxX, p_147590_0_.maxY, p_147590_0_.maxZ);
	    GL11.glVertex3d(p_147590_0_.minX, p_147590_0_.minY, p_147590_0_.maxZ);
	    GL11.glVertex3d(p_147590_0_.minX, p_147590_0_.maxY, p_147590_0_.maxZ);
	    GL11.glEnd();
	    GL11.glDisable(2848);
	}
	
	public static void drawLines(AxisAlignedBB bb) {
		GL11.glEnable(2848);
	    GL11.glBegin(1);
	    GL11.glVertex3d(bb.minX, bb.minY, bb.minZ);
	    GL11.glVertex3d(bb.minX, bb.maxY, bb.maxZ);
	    GL11.glVertex3d(bb.maxX, bb.minY, bb.minZ);
	    GL11.glVertex3d(bb.minX, bb.maxY, bb.maxZ);
	    GL11.glVertex3d(bb.maxX, bb.minY, bb.maxZ);
	    GL11.glVertex3d(bb.minX, bb.maxY, bb.maxZ);
	    GL11.glVertex3d(bb.maxX, bb.minY, bb.minZ);
	    GL11.glVertex3d(bb.minX, bb.maxY, bb.minZ);
	    GL11.glVertex3d(bb.maxX, bb.minY, bb.minZ);
	    GL11.glVertex3d(bb.minX, bb.minY, bb.maxZ);
	    GL11.glVertex3d(bb.maxX, bb.maxY, bb.minZ);
	    GL11.glVertex3d(bb.minX, bb.maxY, bb.maxZ);
	    GL11.glEnd();
	    GL11.glDisable(2848);
	}
	
}
