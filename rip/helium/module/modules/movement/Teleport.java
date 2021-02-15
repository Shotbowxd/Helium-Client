package rip.helium.module.modules.movement;

import org.lwjgl.input.Mouse;

import net.minecraft.network.play.client.C0CPacketInput;
import net.minecraft.network.play.server.S02PacketChat;
import net.minecraft.network.play.server.S45PacketTitle;
import rip.helium.event.EventTarget;
import rip.helium.event.events.impl.network.PacketReceiveEvent;
import rip.helium.event.events.impl.network.PacketSendEvent;
import rip.helium.event.events.impl.player.MoveEvent;
import rip.helium.event.events.impl.player.UpdateEvent;
import rip.helium.module.Module;
import rip.helium.utils.client.Timer;
import rip.helium.utils.entity.PlayerUtils;
import rip.helium.utils.misc.Vec3d;
import rip.helium.utils.render.ColorUtils;

public class Teleport extends Module {

	private final Timer timer = new Timer();
    private Vec3d target;
    private int stage;
	
	public Teleport(int bind, String name, Category category) {
		super(bind, name, category);
		this.setColor(ColorUtils.generateColor());
	}
	
	@Override
	public void onEnable() {
		super.onEnable();
		this.stage = 0;
	}
	
	@EventTarget
	public void onPacketSend(PacketSendEvent event) {
		if (this.stage == 1 && !this.timer.hasPassed(6000L))
            event.setCancelled(true);
	}
	
	@EventTarget
	public void onPacketReceive(PacketReceiveEvent event) {
		if (this.stage == 1 && !this.timer.hasPassed(6000L) && (event.getPacket() instanceof S02PacketChat ||
                event.getPacket() instanceof S45PacketTitle))
            event.setCancelled(true);
	}
	
	@EventTarget
	public void onUpdatePre(UpdateEvent event) {
		int x = mc.objectMouseOver.getBlockPos().getX();
        int y = mc.objectMouseOver.getBlockPos().getY() + 1;
        int z = mc.objectMouseOver.getBlockPos().getZ();
        switch (this.stage) {
            case 0:
                if (Mouse.isButtonDown(1) && !mc.thePlayer.isSneaking() && mc.inGameHasFocus) {
                    this.timer.updateLastTime();
                    this.target = new Vec3d(x, y, z);
                    this.stage = 1;
                }
                break;
            case 1:
                if (this.timer.hasPassed(6000L)) {
                    mc.thePlayer.sendQueue.addToSendQueueNoEvent(new C0CPacketInput(0.0F, 0.0F, true, true));
                    mc.thePlayer.setPosition(this.target.xCoord, this.target.yCoord,
                            target.zCoord);
                    this.stage = 0;
                }
                break;
        }
        if (mc.thePlayer.hurtTime == 9)
            this.timer.updateLastTime();
	}
	
	@EventTarget
	public void onMove(MoveEvent event) {
		if ((((this.stage == 1) ? 1 : 0) & (!this.timer.hasPassed(6000L) ? 1 : 0)) != 0)
            PlayerUtils.setMoveSpeed(event, 0.0D);
	}
	
}
