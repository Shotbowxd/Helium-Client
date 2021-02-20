package rip.helium.gui.chat;

import static rip.helium.ClientSupport.mc;

import java.io.IOException;
import java.util.Iterator;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ChatLine;
import net.minecraft.client.gui.GuiNewChat;
import net.minecraft.client.gui.GuiUtilRenderComponents;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.potion.Potion;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.IChatComponent;
import net.minecraft.util.MathHelper;
import rip.helium.ClientSupport;
import rip.helium.utils.render.Render2DUtils;

public class GuiCustomChat extends GuiNewChat implements ClientSupport {
	public int x;
	public int y;
	public int dragX;
	public int dragY;
	public int y1;
	private boolean dragging;
	  
	public GuiCustomChat(Minecraft mcIn) {
	    super(mcIn);
	}
	  
	public void resetScroll() {
	    super.resetScroll();
	    this.dragging = false;
	}
	  
	public void drawChat(int p_146230_1_) {
	    if ((mc).gameSettings.chatVisibility != EntityPlayer.EnumChatVisibility.HIDDEN) {
	    	int var2 = getLineCount();
	    	boolean var3 = true;
	    	int var4 = 0;
	    	int var5 = this.field_146253_i.size();
	    	float var6 = (mc).gameSettings.chatOpacity * 0.9F + 0.1F;
	    	if (var5 > 0) {
	    		float var7 = getChatScale();
	    		GlStateManager.pushMatrix();
	    		float yOffset = 20.0F;
	    		if (!(mc).thePlayer.capabilities.isCreativeMode) {
	    			yOffset = 6.0F;
	    			if ((mc).thePlayer.getTotalArmorValue() > 0)
	    				yOffset -= 10.0F; 
	    			if ((mc).thePlayer.getActivePotionEffect(Potion.absorption) != null)
	    				yOffset -= 10.0F; 
	    		} 
	    		GlStateManager.translate(2.0F, yOffset, 0.0F);
	    		GlStateManager.scale(var7, var7, 1.0F);
	    		int var9;
	        	for (var9 = 0; var9 + this.scrollPos < this.field_146253_i.size() && var9 < var2; var9++) {
	        		ChatLine var10 = this.field_146253_i.get(var9 + this.scrollPos);
	        		if (var10 != null) {
	        			int var11 = p_146230_1_ - var10.getUpdatedCounter();
	        			if (var11 < 200 || var3)
	        				var4++; 
	        		} 
	        	} 
	        	GlStateManager.translate(0.0F, -8.0F, 0.0F);
	        	int height = -1;
	        	if (!getChatOpen() && 
	        			var4 > 0)
	        		height = -var4 * 9; 
	        	height = -var2 * 9;
	        	if (height != -1)
	        		//Render2DUtils.drawRect(0 + this.x, height + 2 + this.y, getChatWidth() + 3 + this.x, 4 + this.y, -2147483648); 
	        	GlStateManager.translate(1.0F, 8.0F, 0.0F);
	        	for (var9 = 0; var9 + this.scrollPos < this.field_146253_i.size() && var9 < var2; var9++) {
	        		ChatLine var10 = this.field_146253_i.get(var9 + this.scrollPos);
	        		if (var10 != null) {
	        			int var11 = p_146230_1_ - var10.getUpdatedCounter();
	        			if (var11 < 200 || var3) {
	        				double var12 = var11 / 200.0D;
	        				var12 = 1.0D - var12;
	        				var12 *= 10.0D;
	        				var12 = MathHelper.clamp_double(var12, 0.0D, 1.0D);
	        				var12 *= var12;
	        				int var14 = (int)(255.0D * var12);
	        				if (var3)
	        					var14 = 255; 
	        				var14 = (int)(var14 * var6);
	        				byte var15 = 0;
	        				int var16 = -var9 * 9;
	        				this.y1 = var16;
	        				String var17 = var10.getChatComponent().getFormattedText();
	        				GlStateManager.enableBlend();
	        				mc.fontRendererObj.drawStringWithShadow(var17, var15 + this.x, (var16 - 14) + this.y, 16777215 + (var14 << 24));
	        				GlStateManager.disableAlpha();
	        				GlStateManager.disableBlend();
	        			} 
	        		} 
	        	} 
	        	if (var3) {
	        		var9 = (mc).fontRendererObj.FONT_HEIGHT;
	        		GlStateManager.translate(-3.0F, 0.0F, 0.0F);
	        		int var18 = var5 * var9 + var5;
	        		int var11 = var4 * var9 + var4;
	        		int var19 = this.scrollPos * var11 / var5;
	        		int var13 = var11 * var11 / var18;
	        		if (var18 != var11) {
	        			int var14 = (var19 > 0) ? 170 : 96;
	        			int var20 = this.isScrolled ? 13382451 : 3355562;
	        			//drawRect(0, -var19, 2, -var19 - var13, var20 + (var14 << 24));
	        			//drawRect(2, -var19, 1, -var19 - var13, 13421772 + (var14 << 24));
	        		} 
	        	} 
	        	GlStateManager.popMatrix();
	    	} 
	    } 
	}
	  
	public IChatComponent getChatComponent(int p_146236_1_, int p_146236_2_) {
	    if (!getChatOpen())
	    	return null; 
	    ScaledResolution var3 = new ScaledResolution(mc);
	    int var4 = var3.getScaleFactor();
	    float var5 = getChatScale();
	    int var6 = p_146236_1_ / var4 - 3;
	    int var7 = p_146236_2_ / var4 - 27;
	    var6 = MathHelper.floor_float(var6 / var5) + this.x;
	    var7 = MathHelper.floor_float(var7 / var5) + this.y;
	    if (var6 >= 0 && var7 >= 0) {
	    	int var8 = Math.min(getLineCount(), this.field_146253_i.size());
	    	if (var6 <= MathHelper.floor_float(getChatWidth() / getChatScale()) && var7 < (mc).fontRendererObj.FONT_HEIGHT * var8 + var8) {
	    		int yOffset = 0;
	    		if (!(mc).thePlayer.capabilities.isCreativeMode) {
	    			yOffset = 2;
	    			if ((mc).thePlayer.getTotalArmorValue() > 0)
	    				yOffset++; 
	    			if ((mc).thePlayer.getActivePotionEffect(Potion.absorption) != null)
	    				yOffset++; 
	    		} 
	    		int var9 = var7 / (mc).fontRendererObj.FONT_HEIGHT + this.scrollPos - yOffset;
	    		if (var9 >= 0 && var9 < this.field_146253_i.size()) {
	    			ChatLine var10 = this.field_146253_i.get(var9);
	    			int var11 = 0;
	    			Iterator<IChatComponent> var12 = var10.getChatComponent().iterator();
	    			while (var12.hasNext()) {
	    				IChatComponent var13 = var12.next();
	    				if (var13 instanceof ChatComponentText) {
	    					var11 += (mc).fontRendererObj.getStringWidth(GuiUtilRenderComponents.func_178909_a(((ChatComponentText)var13).getChatComponentText_TextValue(), false));
	    					if (var11 > var6)
	    						return var13; 
	    				} 
	    			} 
	    		} 
	    		return null;
	    	} 
	    	return null;
	    } 
	    return null;
	}
	
	public void mouseClicked(int par1, int par2, int par3) throws IOException {
	    if (par3 == 0) {
	    	if (isMouseOverChat(par1, par2) && par3 == 0) {
	    		this.dragging = true;
	    		this.dragX = this.x - par1;
	    		this.dragY = this.y - par2;
	    	} 
	    	System.out.println("dragging: " + this.dragging + ", dragX: " + this.dragX + ", dragY: " + this.dragY);
	    } 
	    onDrag(par1, par2);
	    super.mouseClicked(par1, par2, par3);
	}
	  
	public void mouseReleased(int par1, int par2, int par3) {
	    this.dragging = false;
	    super.mouseReleased(par1, par2, par3);
	}
	  
	public void onDrag(int par1, int par2) {
	    this.x = par1 + this.dragX;
	    this.x = par2 + this.dragY;
	}
	  
	public int getChatHeight() {
	    return calculateChatboxHeight(mc.gameSettings.chatHeightFocused);
	}
	  
	private boolean isMouseOverChat(int mouseX, int mouseY) {
	    if(x >= this.x && x <= this.x + this.getChatWidth() && y >= this.y && y <= this.y + this.getChatHeight()) {
	    	return true;
	    }
	    return false;
	}
	  
	public int f() {
	    return a((mc.gameSettings).chatWidth);
	}
	  
	public static final int a(float par0) {
	    short var1 = 320;
	    byte var2 = 40;
	    return d(par0 * (var1 - var2) + var2);
	}
	  
	public static int d(float par0) {
	    int var1 = (int)par0;
	    return (par0 < var1) ? (var1 - 1) : var1;
	}
}
