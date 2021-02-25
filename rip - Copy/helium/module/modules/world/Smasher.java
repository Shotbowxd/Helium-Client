package rip.helium.module.modules.world;

import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.network.play.client.C07PacketPlayerDigging;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import rip.helium.event.EventTarget;
import rip.helium.event.events.impl.player.UpdateEvent;
import rip.helium.module.Module;
import rip.helium.utils.render.ColorUtils;

public class Smasher extends Module {

	public Smasher(int bind, String name, Category category) {
		super(bind, name, category);
		this.setColor(ColorUtils.generateColor());
	}
	
	@EventTarget
	public void onUpdatePre(UpdateEvent event) {
		final boolean breaking = false;
        for (int radius = 7, x = -radius; x < radius; ++x) {
            for (int y = radius; y > -radius; --y) {
                for (int z = -radius; z < radius; ++z) {
                    final int xPos = (int) mc.thePlayer.posX + x;
                    final int yPos = (int) mc.thePlayer.posY + y;
                    final int zPos = (int) mc.thePlayer.posZ + z;
                    final BlockPos blockPos = new BlockPos(xPos, yPos, zPos);
                    final Block block = mc.theWorld.getBlockState(blockPos).getBlock();
                    if ((block.getBlockState().getBlock() == Block.getBlockById(92) || block.getBlockState().getBlock() == Blocks.bed) && mc.thePlayer.ticksExisted % 3 == 0) {
                        mc.thePlayer.sendQueue.addToSendQueue(new C07PacketPlayerDigging(C07PacketPlayerDigging.Action.START_DESTROY_BLOCK, blockPos, EnumFacing.NORTH));
                        mc.thePlayer.sendQueue.addToSendQueue(new C07PacketPlayerDigging(C07PacketPlayerDigging.Action.STOP_DESTROY_BLOCK, blockPos, EnumFacing.NORTH));
                    }
                }
            }
        }
	}

}
