package rip.helium.module.modules.movement;

import java.util.ArrayList;

import net.minecraft.block.Block;
import net.minecraft.block.BlockAir;
import net.minecraft.block.BlockHopper;
import net.minecraft.network.play.client.C03PacketPlayer;
import net.minecraft.network.play.server.S02PacketChat;
import net.minecraft.network.play.server.S08PacketPlayerPosLook;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.BlockPos;
import net.minecraft.util.MathHelper;
import rip.helium.event.EventTarget;
import rip.helium.event.events.impl.client.RunTickEvent;
import rip.helium.event.events.impl.network.PacketReceiveEvent;
import rip.helium.event.events.impl.player.BlockPushEvent;
import rip.helium.event.events.impl.player.BoundingBoxEvent;
import rip.helium.event.events.impl.player.MoveEvent;
import rip.helium.event.events.impl.player.UpdateEvent;
import rip.helium.event.events.impl.render.InsideBlockRenderEvent;
import rip.helium.module.Module;
import rip.helium.setting.Setting;
import rip.helium.utils.client.Timer;
import rip.helium.utils.entity.PlayerUtils;
import rip.helium.utils.render.ColorUtils;

public class Phase extends Module {

	private Setting mode;
	private Timer timer;
    private double distance = 2.5;
    private int moveUnder;
	
	public Phase(int bind, String name, Category category) {
		super(bind, name, category);
		this.setColor(ColorUtils.generateColor());
		
		ArrayList<String> modes = new ArrayList<String>();
		modes.add("Vanilla");
		modes.add("Aris");
		modes.add("Skip");
		
		this.mode = new Setting("Mode", this, "Vanilla", modes);
		
		mc.hackedClient.getSettingManager().addSetting(this.mode);
		
		this.timer = new Timer();
	}
	
	public boolean isInsideBlock() {
        for (int x = MathHelper.floor_double(
                mc.thePlayer.getEntityBoundingBox().minX); x < MathHelper.floor_double(mc.thePlayer.getEntityBoundingBox().maxX) + 1; ++x) {
            for (int y = MathHelper.floor_double(
                    mc.thePlayer.getEntityBoundingBox().minY + 1.0D); y < MathHelper.floor_double(mc.thePlayer.getEntityBoundingBox().maxY)
                    + 2; ++y) {
                for (int z = MathHelper.floor_double(
                        mc.thePlayer.getEntityBoundingBox().minZ); z < MathHelper.floor_double(mc.thePlayer.getEntityBoundingBox().maxZ)
                        + 1; ++z) {
                    Block block = mc.theWorld.getBlockState(new BlockPos(x, y, z)).getBlock();
                    if (block != null && !(block instanceof BlockAir)) {
                        AxisAlignedBB boundingBox = block.getCollisionBoundingBox(mc.theWorld, new BlockPos(x, y, z),
                                mc.theWorld.getBlockState(new BlockPos(x, y, z)));
                        if (block instanceof BlockHopper) {
                            boundingBox = new AxisAlignedBB(x, y, z, x + 1, y + 1, z + 1);
                        }

                        if (boundingBox != null && mc.thePlayer.getEntityBoundingBox().intersectsWith(boundingBox))
                            return true;
                    }
                }
            }
        }
        return false;
    }
	
	@EventTarget
	public void onInsideBlockRender(InsideBlockRenderEvent event) {
		event.setCancelled(true);
	}
	
	@EventTarget
	public void onTick(RunTickEvent event) {
		if(mc.thePlayer == null) return;
		switch(this.mode.getValString()) {
		case "Vanilla":
            break;
		}
	}
	
	@EventTarget
	public void onBB(BoundingBoxEvent event) {
		switch(this.mode.getValString()) {
		case "Vanilla":
			if (isInsideBlock() || mc.thePlayer.isCollidedHorizontally) {
				event.setBoundingBox(null);
				//moveUnder = 69;
            }
            break;
		case "Aris":
			if((isInsideBlock() && mc.gameSettings.keyBindJump.pressed || !isInsideBlock() && event.getBoundingBox() != null && event.getBoundingBox().maxY > mc.thePlayer.getEntityBoundingBox().minY)) {
	            if (PlayerUtils.isMoving()) {
	                PlayerUtils.setMoveSpeed(0.625F);
	            }
	            mc.thePlayer.motionY = 0;
	            event.setBoundingBox(null);
			}
			break;
		case "Skip":
			if ((event.getBoundingBox() != null) && (event.getBoundingBox().maxY > mc.thePlayer.boundingBox.minY) && (mc.thePlayer.isSneaking())) {
				event.setBoundingBox(null);
            }
			break;
		}
	}
	
	@EventTarget
	public void onPacketReceive(PacketReceiveEvent event) {
		if (event.getPacket() instanceof S02PacketChat) {
            S02PacketChat packet = (S02PacketChat) event.getPacket();
            if (packet.getChatComponent().getUnformattedText().contains("You cannot go past the border.")) {
                event.setCancelled(true);
            }
        }
        if (this.mode.getValString().equalsIgnoreCase("Vanilla") && event.getPacket() instanceof S08PacketPlayerPosLook && moveUnder == 2) {
            moveUnder = 1;
        }
        if (this.mode.getValString().equalsIgnoreCase("Vanilla") && event.getPacket() instanceof S08PacketPlayerPosLook && moveUnder == 69) {
            moveUnder = 1488;
        }
        if (event.getPacket() instanceof S08PacketPlayerPosLook) {
            S08PacketPlayerPosLook packet = (S08PacketPlayerPosLook) event.getPacket();
            packet.setPitch(mc.thePlayer.rotationPitch);
            packet.setYaw(mc.thePlayer.rotationYaw);

            if (moveUnder == 2) {
                moveUnder = 1;
            }
        }
	}
	
	@EventTarget
	public void onMove(MoveEvent event) {
		switch(this.mode.getValString()) {
		case "Vanilla":
			if (isInsideBlock()) {
                if (mc.gameSettings.keyBindJump.isKeyDown()) {
                    event.setY(mc.thePlayer.motionY = 1.2);
                } else if (mc.gameSettings.keyBindSneak.isKeyDown()) {
                    event.setY(mc.thePlayer.motionY = -1.2);
                } else {
                    event.setY(mc.thePlayer.motionY = 0.0);
                }
                PlayerUtils.setMoveSpeed(event, 0.75f);
            }
			break;
		case "Skip":
			if ((isInsideBlock()) && (mc.thePlayer.isSneaking())) {
                float yaw = mc.thePlayer.rotationYaw;
                mc.thePlayer.boundingBox.offsetAndUpdate(1.5 * Math.cos(Math.toRadians(yaw + 90.0F)), 0.0D, 1.5 * Math.sin(Math.toRadians(yaw + 90.0F)));


			}
			break;
		}
	}
	
	@EventTarget
	public void onUpdatePre(UpdateEvent event) {
		if (this.mode.getValString().equalsIgnoreCase("Vanilla") && mc.gameSettings.keyBindSneak.isPressed() && !isInsideBlock()) {
            mc.thePlayer.sendQueue.getNetworkManager().sendPacket(new C03PacketPlayer.C04PacketPlayerPosition(mc.thePlayer.posX, mc.thePlayer.posY - 2.0, mc.thePlayer.posZ, true));
            moveUnder = 2;
		} else if (mode.getValString().equalsIgnoreCase("Skip")) {
            if (mc.thePlayer.isSneaking())
                if (mc.thePlayer.isCollidedHorizontally && !event.isPre()) {
                    double x = -MathHelper.sin(PlayerUtils.getDirection()) * 1.5,
                            z = MathHelper.cos(PlayerUtils.getDirection()) * 1.5;

                }
        }
	}
	
	@EventTarget
	public void onUpdatePost(UpdateEvent event) {
		 if(event.isPre()) {
			 
		 } else {
			 if(this.mode.getValString().equalsIgnoreCase("Aris")) {
				 if (mc.thePlayer.isSneaking()) {
	                 if (!mc.thePlayer.isOnLadder()) {
	                     PlayerUtils.setMoveSpeed(mc.thePlayer.isCollidedHorizontally ? .3 : .05);
	                     mc.thePlayer.getEntityBoundingBox().offset(
	                             1.2 * Math.cos(Math.toRadians(mc.thePlayer.rotationYaw + 90.0f)), 0.0,
	                             1.2 * Math.sin(Math.toRadians(mc.thePlayer.rotationYaw + 90.0f)));

	                     if (mc.getCurrentServerData() != null
	                             && !mc.getCurrentServerData().serverIP.toLowerCase().contains("hypixel")) {
	                         double offset = 1.35;
	                         Number playerYaw = PlayerUtils.getDir(mc.thePlayer.rotationYaw);
	                         mc.thePlayer.sendQueue.addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(
	                                 mc.thePlayer.posX - Math.sin(playerYaw.doubleValue()) * offset,
	                                 mc.thePlayer.posY + .3,
	                                 mc.thePlayer.posZ + Math.cos(playerYaw.doubleValue()) * offset, true));
	                         mc.thePlayer.setPositionAndUpdate(
	                                 mc.thePlayer.posX - Math.sin(playerYaw.doubleValue()) * offset, mc.thePlayer.posY,
	                                 mc.thePlayer.posZ + Math.cos(playerYaw.doubleValue()) * offset);
	                     } else {
	                         double offset = 1.2;
	                         Number playerYaw = PlayerUtils.getDir(mc.thePlayer.rotationYaw);
	                         mc.thePlayer.sendQueue.addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(
	                                 mc.thePlayer.posX - Math.sin(playerYaw.doubleValue()) * offset,
	                                 mc.thePlayer.posY + .3,
	                                 mc.thePlayer.posZ + Math.cos(playerYaw.doubleValue()) * offset, true));
	                         mc.thePlayer.setPositionAndUpdate(
	                                 mc.thePlayer.posX - Math.sin(playerYaw.doubleValue()) * offset, mc.thePlayer.posY,
	                                 mc.thePlayer.posZ + Math.cos(playerYaw.doubleValue()) * offset);
	                         mc.thePlayer.posX = mc.thePlayer.posX - Math.sin(playerYaw.doubleValue()) * offset;
	                         mc.thePlayer.posZ = mc.thePlayer.posZ + Math.cos(playerYaw.doubleValue()) * offset;
	                     }
	                 }
				 }
			 }
		 }
	}
	
	@EventTarget
	public void onPush(BlockPushEvent event) {
		if(this.mode.getValString().equalsIgnoreCase("Aris")) {
			event.setCancelled(true);
	        mc.thePlayer.motionY = 0;
		}
		if(this.mode.getValString().equalsIgnoreCase("Vanilla")) {
			event.setCancelled(true);
		}
	}
	
}
