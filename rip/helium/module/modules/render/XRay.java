package rip.helium.module.modules.render;

import rip.helium.event.EventTarget;
import rip.helium.event.events.impl.render.BlockCullEvent;
import rip.helium.event.events.impl.render.OverrideStairBlockLayerEvent;
import rip.helium.event.events.impl.render.SetAlphaMultiplierEvent;
import rip.helium.event.events.impl.render.SetVisibilityEvent;
import rip.helium.event.events.impl.render.ShouldSideBeRenderedEvent;
import rip.helium.module.Module;
import rip.helium.utils.render.ColorUtils;

public class XRay extends Module {

	private boolean[] blocks = new boolean[4096];
	
	private int oldAO;
	private float oldGamma;
	
	public XRay(int bind, String name, String displayName, Category category) {
		super(bind, name, displayName, category);
		this.setColor(ColorUtils.generateColor());
		this.setHidden(true);
		
		blocks[14] = true;
	    blocks[15] = true;
	    blocks[16] = true;
	    blocks[21] = true;
	    blocks[56] = true;
	    blocks[73] = true;
	    blocks[129] = true;
	    blocks[153] = true;
	}
	
	@Override
	public void onToggle() {
		super.onToggle();
		mc.renderGlobal.loadRenderers();
	}
	
	@Override
	public void onDisable() {
		super.onDisable();
		mc.gameSettings.ambientOcclusion = oldAO;
		mc.gameSettings.gammaSetting = oldGamma;
	}
	
	@Override
	public void onEnable() {
		super.onEnable();
		oldAO = mc.gameSettings.ambientOcclusion;
		mc.gameSettings.ambientOcclusion = 0;
		oldGamma = mc.gameSettings.gammaSetting;
		mc.gameSettings.gammaSetting = 1000;
	}
	
	public boolean contains(int id) {
		return blocks[id];
	}
	
	@EventTarget
	public void onRenderSide(ShouldSideBeRenderedEvent event) {
		event.setRendered(blocks[event.getBlock().getIdFromBlock(event.getBlock())]);
	}
	
	@EventTarget
	public void onSetAlphaMultiplier(SetAlphaMultiplierEvent event) {
		//event.setMultiplier(0x40000000);
	}
	
	@EventTarget
	public void onSetVisibility(SetVisibilityEvent event) {
		event.setVisible(true);
		event.setShouldSet(true);
	}
	
	@EventTarget
	public void onOverrideStairBlockLayer(OverrideStairBlockLayerEvent event) {
		event.setCancelled(true);
	}
	
	@EventTarget
	public void onBlockCull(BlockCullEvent event) {
		event.setCancelled(true);
	}

}
