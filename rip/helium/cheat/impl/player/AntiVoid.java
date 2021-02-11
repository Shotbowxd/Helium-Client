package rip.helium.cheat.impl.player;

import me.hippo.systems.lwjeb.annotation.Collect;
import net.minecraft.network.play.client.C03PacketPlayer;
import net.minecraft.util.AxisAlignedBB;
import org.apache.commons.lang3.RandomUtils;
import rip.helium.Helium;
import rip.helium.cheat.Cheat;
import rip.helium.cheat.CheatCategory;
import rip.helium.event.minecraft.PlayerMoveEvent;
import rip.helium.utils.Stopwatch;

public class AntiVoid extends Cheat {

    private boolean shouldSave;
    private final Stopwatch timer;

    public AntiVoid() {
        super("AntiVoid", "Lags you back up when you fall into the void.", CheatCategory.PLAYER);
        timer = new Stopwatch();
    }

    @Collect
    public void onMove(PlayerMoveEvent e) {
        if (!isBlockUnder() && mc.thePlayer.fallDistance > 3) {
            mc.getNetHandler().addToSendQueueNoEvent(new C03PacketPlayer.C06PacketPlayerPosLook(mc.thePlayer.posX, mc.thePlayer.posY + RandomUtils.nextFloat(11F, 12F), mc.thePlayer.posZ,
                    mc.thePlayer.rotationYaw, mc.thePlayer.rotationPitch, false));
        }
    }

    private boolean isBlockUnder() {
        for (int offset = 0; offset < mc.thePlayer.posY + mc.thePlayer.getEyeHeight(); offset += 2) {
            AxisAlignedBB boundingBox = mc.thePlayer.getEntityBoundingBox().offset(0, -offset, 0);
            if (!mc.theWorld.getCollidingBoundingBoxes(mc.thePlayer, boundingBox).isEmpty())
                return true;
        }
        return false;
    }


}
