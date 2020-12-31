package rip.helium.cheat.impl.player;

import me.hippo.systems.lwjeb.annotation.Collect;
import net.minecraft.network.play.client.C03PacketPlayer;
import net.minecraft.util.AxisAlignedBB;
import rip.helium.cheat.Cheat;
import rip.helium.cheat.CheatCategory;
import rip.helium.event.minecraft.PlayerUpdateEvent;
import rip.helium.utils.property.impl.DoubleProperty;

public class Regen extends Cheat {
    public int waittime;
    DoubleProperty health = new DoubleProperty("Health", "You'll start regenerating health after you go below this.", null, 15, 0, 20, 0.5, null);

    public Regen() {
        super("Regen", "Makes you heal faster", CheatCategory.PLAYER);
        registerProperties(health);
    }

    @Collect
    public void onPlayerUpdate(PlayerUpdateEvent playerUpdateEvent) {
        if (mc.thePlayer.getHealth() <= health.getValue()) {
            for (int i = 0; i < 20; i++) {
                if (!mc.thePlayer.onGround) {
                    mc.getNetHandler().addToSendQueue(new C03PacketPlayer(true));
                }
            }
        }
    }

    public double getGroundLevel() {
        for (int i = (int) Math.round(getPlayer().posY); i > 0; --i) {
            AxisAlignedBB box = getPlayer().getEntityBoundingBox().addCoord(0, 0, 0);
            box.minY = i - 1;
            box.maxY = i;
            if (isColliding(box) && box.minY <= getPlayer().posY) {
                return i;
            }
        }
        return 0;
    }

    private boolean isColliding(AxisAlignedBB box) {
        return mc.theWorld.checkBlockCollision(box);
    }
}