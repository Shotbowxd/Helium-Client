package rip.helium.module.modules.movement;

import rip.helium.event.EventTarget;
import rip.helium.event.events.impl.player.UpdateEvent;
import rip.helium.module.Module;
import rip.helium.utils.render.ColorUtils;

public class Sprint extends Module {

	private boolean shouldSkipNextUpdate;
	
	public Sprint(int bind, String name, Category category) {
		super(bind, name, category);
		this.setColor(ColorUtils.generateColor());
		this.setState(true);
		shouldSkipNextUpdate = false;
	}
	
	@EventTarget
	public void onUpdatePre(UpdateEvent event) {
		if (!shouldSkipNextUpdate) {
            mc.thePlayer.setSprinting(
                    !mc.thePlayer.isCollidedHorizontally
                            && !mc.thePlayer.isSneaking()
                            && mc.thePlayer.getFoodStats().getFoodLevel() > 5
                            && mc.gameSettings.keyBindForward.pressed);
        } else {
            shouldSkipNextUpdate = false;
        }
	}
	
}
